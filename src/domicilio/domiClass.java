package domicilio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Estructuras.ColaCocina;
import databaseConexion.dbConexion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class domiClass {
    private JTextArea textArea1;
    private JTable table1;
    private JButton verInformacionButton;
    private JPanel jpanel1;
    private JButton pedidoEntregadoButton;

    private ColaCocina colaCocina;

    public domiClass() {
        cargarPedidosEnColaCocina();
        cargarPedidosEnTablaCocina();

        verInformacionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInfoPedidoEnTextArea();
            }
        });

        pedidoEntregadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPedidoSeleccionado();
            }
        });
    }

    public void cargarPedidosEnColaCocina() {
        colaCocina = new ColaCocina(10);

        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT idpedido, tipo_cliente, tipo_pedido FROM pedido p " +
                    "JOIN clientes c ON p.idclientes = c.idclientes";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idPedido = resultSet.getInt("idpedido");
                int tipoCliente = resultSet.getInt("tipo_cliente");
                String tipoPedido = resultSet.getString("tipo_pedido");
                int tiempoCoccion;

                if ("rapido".equals(tipoPedido)) {
                    tiempoCoccion = 1 + (int)(Math.random() * 5);
                } else {
                    tiempoCoccion = 6 + (int)(Math.random() * 10);
                }

                if (tipoCliente == 1) {
                    colaCocina.insertar(idPedido, 1, tiempoCoccion);
                } else {
                    colaCocina.insertar(idPedido, 2, tiempoCoccion);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cargarPedidosEnTablaCocina() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Pedido");
        modelo.addColumn("Prioridad");
        modelo.addColumn("Tiempo de Cocción");
        modelo.addColumn("Tipo de Pedido");
        modelo.addColumn("Producto");
        modelo.addColumn("Cliente");

        ColaCocina.Pedido pedido = colaCocina.extraer();

        while (pedido != null) {
            Connection conexion = dbConexion.obtenerConexion();
            try {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT p.idpedido, c.tipo_cliente, p.tipo_pedido, pr.nombre_producto, c.nombre " +
                                "FROM pedido p " +
                                "JOIN productos pr ON p.idproducto = pr.idproductos " +
                                "JOIN clientes c ON p.idclientes = c.idclientes " +
                                "WHERE p.idpedido = ?"
                );
                ps.setInt(1, pedido.numero);

                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    String tipoPedido = resultSet.getString("tipo_pedido");
                    String producto = resultSet.getString("nombre_producto");
                    String cliente = resultSet.getString("nombre");

                    modelo.addRow(new Object[]{
                            pedido.numero,
                            pedido.prioridad,
                            pedido.tiempoCoccion,
                            tipoPedido,
                            producto,
                            cliente
                    });
                }
                resultSet.close();
                ps.close();
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            pedido = colaCocina.extraer();
        }

        table1.setModel(modelo);
    }

    public void mostrarInfoPedidoEnTextArea() {
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        int filaSeleccionada = table1.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idPedido = (int) modelo.getValueAt(filaSeleccionada, 0);
            double valorTotal = calcularValorTotalDelPedido(idPedido);
            String cliente = modelo.getValueAt(filaSeleccionada, 5).toString();
            String producto = modelo.getValueAt(filaSeleccionada, 4).toString();
            int tiempoCoccion = (int) modelo.getValueAt(filaSeleccionada, 2);

            String infoPedido = "Cliente: " + cliente + "\n";
            infoPedido += "Productos: " + producto + "\n";
            infoPedido += "Valor Total: $" + valorTotal + "\n";
            infoPedido += "Tiempo de Cocción: " + tiempoCoccion + " minutos";

            textArea1.setText(infoPedido);
        } else {
            textArea1.setText("Por favor, selecciona un pedido en la tabla.");
        }
    }

    private double calcularValorTotalDelPedido(int idPedido) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT total FROM pedido WHERE idpedido = ?"
                );
                ps.setInt(1, idPedido);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double valorTotal = rs.getDouble("total");
                    conexion.close();
                    return valorTotal;
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al calcular el valor total del pedido: " + e.getMessage());
            }
        }

        return 0.0;
    }

    private void eliminarPedidoSeleccionado() {
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        int filaSeleccionada = table1.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idPedido = (int) modelo.getValueAt(filaSeleccionada, 0);

            eliminarPedidoDeLaBaseDeDatos(idPedido);
            modelo.removeRow(filaSeleccionada);
            textArea1.setText("");
        } else {
            textArea1.setText("Por favor, selecciona un pedido en la tabla.");
        }
    }

    private void eliminarPedidoDeLaBaseDeDatos(int idPedido) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                PreparedStatement ps = conexion.prepareStatement(
                        "DELETE FROM pedido WHERE idpedido = ?"
                );
                ps.setInt(1, idPedido);
                ps.executeUpdate();
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al eliminar el pedido de la base de datos: " + e.getMessage());
            }
        }
    }

    public JPanel getPanel1() {
        return jpanel1;
    }
}
