package operador;

import databaseConexion.dbConexion;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class realizarPedido {
    private JTextArea textArea1;
    private JButton agregarButton;
    private JTextField textField2;
    private JTextArea textArea2;
    private JPanel jpanel1;
    private JButton buscarButton;
    private JButton mostrarProductosButton;
    private JTextField textField1;
    private JTextArea textArea3;
    private JTextField textField3;
    private JButton limpiarButton;
    private double totalAcumulado = 0.0;
    private Map<String, Double> productosAgregados = new HashMap<>();

    public JPanel getPanel() {
        return jpanel1;
    }

    public realizarPedido() {
        cargarProductosDesdeBaseDeDatos();

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductos();
            }
        });

        mostrarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarProductosDesdeBaseDeDatos();
            }
        });

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoAPedido();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarTextArea3();
            }
        });
    }

    private void cargarProductosDesdeBaseDeDatos() {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT idproductos, nombre_producto, valor_producto FROM productos";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();

                textArea2.setText("");

                while (rs.next()) {
                    int idProducto = rs.getInt("idproductos");
                    String nombreProducto = rs.getString("nombre_producto");
                    double valorProducto = rs.getDouble("valor_producto");
                    textArea2.append(idProducto + ": " + nombreProducto + ": $" + valorProducto + "\n");
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cargar productos desde la base de datos: " + e.getMessage());
            }
        }
    }

    private void buscarProductos() {
        String textoBuscado = textField2.getText();

        if (textoBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un texto para buscar.");
            return;
        }

        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT idproductos, nombre_producto, valor_producto FROM productos " +
                        "WHERE nombre_producto LIKE ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, "%" + textoBuscado + "%");

                ResultSet rs = ps.executeQuery();

                textArea2.setText("");

                while (rs.next()) {
                    int idProducto = rs.getInt("idproductos");
                    String nombreProducto = rs.getString("nombre_producto");
                    double valorProducto = rs.getDouble("valor_producto");
                    textArea2.append(idProducto + ": " + nombreProducto + ": $" + valorProducto + "\n");
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al buscar productos en la base de datos: " + e.getMessage());
            }
        }
    }

    private void agregarProductoAPedido() {
        String producto = textField1.getText();
        String cantidadText = textField3.getText();

        if (producto.isEmpty() || cantidadText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        int cantidad = Integer.parseInt(cantidadText);
        double valorProducto = buscarValorProducto(producto);

        if (valorProducto == -1.0) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado en la base de datos.");
            return;
        }

        double totalProducto = valorProducto * cantidad;

        if (productosAgregados.containsKey(producto)) {
            cantidad += productosAgregados.get(producto);
            totalProducto += productosAgregados.get(producto) * valorProducto;
            totalAcumulado -= productosAgregados.get(producto) * valorProducto;
        }

        productosAgregados.put(producto, totalProducto);
        totalAcumulado += totalProducto;

        textArea3.setText("");

        for (Map.Entry<String, Double> entry : productosAgregados.entrySet()) {
            textArea3.append("Producto: " + entry.getKey() + ", Cantidad: " + cantidad + ", Total: $" + entry.getValue() + "\n");
        }

        textArea3.append("Total acumulado: $" + totalAcumulado + "\n");
    }

    private double buscarValorProducto(String nombreProducto) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT valor_producto FROM productos WHERE nombre_producto = ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, nombreProducto);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double valor = rs.getDouble("valor_producto");
                    conexion.close();
                    return valor;
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al buscar producto en la base de datos: " + e.getMessage());
            }
        }

        return -1.0; // Retorna -1.0 si el producto no se encontró en la base de datos
    }

    private void limpiarTextArea3() {
        textArea3.setText("");
        productosAgregados.clear();
        totalAcumulado = 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new realizarPedido();
            }
        });
    }
}
