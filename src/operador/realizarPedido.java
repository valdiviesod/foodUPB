package operador;

import databaseConexion.dbTests;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class realizarPedido {
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton realizarPedidoButton;
    private JTextField textField2;
    private JTextArea textArea2;
    private JPanel jpanel1;
    private JButton buscarButton;
    private JButton mostrarProductosButton;

    public JPanel getPanel() {
        return jpanel1;
    }

    public realizarPedido() {
        cargarMenusDesdeBaseDeDatos(); // Cargar menús al inicio
        cargarProductosDesdeBaseDeDatos(); // Cargar productos al inicio

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductos();
            }
        });

        mostrarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarProductosDesdeBaseDeDatos(); // Cargar productos nuevamente
            }
        });
    }

    private void cargarMenusDesdeBaseDeDatos() {
        Connection conexion = dbTests.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT idmenu, nombre_menu, valor_menu FROM menu";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();

                textArea1.setText(""); // Limpia el textArea1

                while (rs.next()) {
                    int idMenu = rs.getInt("idmenu");
                    String nombreMenu = rs.getString("nombre_menu");
                    double valorMenu = rs.getDouble("valor_menu");
                    textArea1.append(idMenu + ": " + nombreMenu + ": $" + valorMenu + "\n");
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cargar menús desde la base de datos: " + e.getMessage());
            }
        }
    }

    private void cargarProductosDesdeBaseDeDatos() {
        Connection conexion = dbTests.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT idproductos, nombre_producto, valor_producto FROM productos";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();

                textArea2.setText(""); // Limpia el textArea2

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

        Connection conexion = dbTests.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT idproductos, nombre_producto, valor_producto FROM productos " +
                        "WHERE nombre_producto LIKE ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, "%" + textoBuscado + "%"); // Agrega el comodín % para buscar en cualquier posición

                ResultSet rs = ps.executeQuery();

                textArea2.setText(""); // Limpia el textArea2

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new realizarPedido();
            }
        });
    }
}
