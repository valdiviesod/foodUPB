package operador;

import databaseConexion.dbConexion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JButton realizarPedidoButton;
    private JTextField textField4;
    private JButton mostrarMasVendidosButton;
    private double totalAcumulado = 0.0;
    private Node<ProductoPedido> productosAgregados = null;

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

        realizarPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarPedido();
            }
        });

        mostrarMasVendidosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarProductosMasPedidos();
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

        ProductoPedido productoPedido = new ProductoPedido(producto, cantidad, totalProducto);
        productosAgregados = insertarNodo(productosAgregados, productoPedido);
        totalAcumulado += totalProducto;

        textArea3.setText("");

        Node<ProductoPedido> current = productosAgregados;
        while (current != null) {
            ProductoPedido pedido = current.data;
            textArea3.append("Producto: " + pedido.getNombre() +
                    ", Cantidad: " + pedido.getCantidad() +
                    ", Total: $" + pedido.getTotal() + "\n");
            current = current.next;
        }

        textArea3.append("Total acumulado: $" + totalAcumulado + "\n");
    }

    private Node<ProductoPedido> insertarNodo(Node<ProductoPedido> cabeza, ProductoPedido datos) {
        if (cabeza == null) {
            return new Node<>(datos);
        } else {
            Node<ProductoPedido> actual = cabeza;
            while (actual.next != null) {
                actual = actual.next;
            }
            actual.next = new Node<>(datos);
            return cabeza;
        }
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

    private void realizarPedido() {
        String nombreCliente = textField4.getText();
        String direccionCliente = "Dirección de ejemplo";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaActual = sdf.format(new Date());

        if (nombreCliente.isEmpty() || productosAgregados == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos y agregue productos.");
            return;
        }

        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                int idCliente = insertarCliente(conexion, nombreCliente, direccionCliente);

                if (idCliente == -1) {
                    JOptionPane.showMessageDialog(null, "Error al insertar/buscar el cliente.");
                    return;
                }

                Node<ProductoPedido> current = productosAgregados;
                while (current != null) {
                    String nombreProducto = current.data.getNombre();
                    double totalProducto = current.data.getTotal();
                    int cantidad = current.data.getCantidad();

                    int idProducto = obtenerIdProducto(conexion, nombreProducto);

                    if (idProducto == -1) {
                        JOptionPane.showMessageDialog(null, "Error al obtener el ID del producto.");
                        return;
                    }

                    if (!insertarPedido(conexion, idProducto, idCliente, cantidad, totalProducto, fechaActual)) {
                        JOptionPane.showMessageDialog(null, "Error al insertar el pedido.");
                        return;
                    }

                    current = current.next;
                }

                JOptionPane.showMessageDialog(null, "Pedido realizado con éxito.");

                textField4.setText("");
                limpiarTextArea3();
            } catch (SQLException e) {
                System.err.println("Error al realizar el pedido: " + e.getMessage());
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int insertarCliente(Connection conexion, String nombreCliente, String direccionCliente) throws SQLException {
        String consulta = "SELECT idclientes FROM clientes WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(consulta);
        ps.setString(1, nombreCliente);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("idclientes");
        } else {
            String insercion = "INSERT INTO clientes (nombre, direccion) VALUES (?, ?)";
            PreparedStatement psInsercion = conexion.prepareStatement(insercion, PreparedStatement.RETURN_GENERATED_KEYS);
            psInsercion.setString(1, nombreCliente);
            psInsercion.setString(2, direccionCliente);

            int filasAfectadas = psInsercion.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet generatedKeys = psInsercion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        return -1;
    }

    private int obtenerIdProducto(Connection conexion, String nombreProducto) throws SQLException {
        String consulta = "SELECT idproductos FROM productos WHERE nombre_producto = ?";
        PreparedStatement ps = conexion.prepareStatement(consulta);
        ps.setString(1, nombreProducto);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("idproductos");
        } else {
            return -1; // Producto no encontrado
        }
    }

    private boolean insertarPedido(Connection conexion, int idProducto, int idCliente, int cantidad, double totalProducto, String fecha) throws SQLException {
        String consulta = "INSERT INTO pedido (idproducto, idclientes, cantidad, Fecha, total) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conexion.prepareStatement(consulta);
        ps.setInt(1, idProducto);
        ps.setInt(2, idCliente);
        ps.setInt(3, cantidad);
        ps.setString(4, fecha);
        ps.setDouble(5, totalProducto);

        int filasAfectadas = ps.executeUpdate();

        return filasAfectadas > 0;
    }

    private void limpiarTextArea3() {
        textArea3.setText("");
        productosAgregados = null;
        totalAcumulado = 0.0;
    }

    private void mostrarProductosMasPedidos() {
        String nombreCliente = textField4.getText();

        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del cliente.");
            return;
        }

        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT p.nombre_producto, SUM(pd.cantidad) AS total_cantidad " +
                        "FROM pedido AS pd " +
                        "JOIN productos AS p ON pd.idproducto = p.idproductos " +
                        "JOIN clientes AS c ON pd.idclientes = c.idclientes " +
                        "WHERE c.nombre = ? " +
                        "GROUP BY p.nombre_producto " +
                        "ORDER BY total_cantidad DESC";

                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, nombreCliente);
                ResultSet rs = ps.executeQuery();

                textArea1.setText(""); // Limpiar el textArea1

                while (rs.next()) {
                    String nombreProducto = rs.getString("nombre_producto");
                    int totalCantidad = rs.getInt("total_cantidad");
                    textArea1.append(nombreProducto + ": Cantidad Total: " + totalCantidad + "\n");
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al obtener los productos más pedidos: " + e.getMessage());
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

    private class ProductoPedido {
        private String nombre;
        private int cantidad;
        private double total;

        public ProductoPedido(String nombre, int cantidad, double total) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.total = total;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getTotal() {
            return total;
        }
    }

    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
