package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class pedidos {
    private JTable tablaPedidos;
    private JPanel mainPanel;
    private JButton eliminarPedidoButton;

    public pedidos() {
        cargarDatosEnJTable();

        eliminarPedidoButton.addActionListener(e -> eliminarPedidoSeleccionado());
    }

    public void cargarDatosEnJTable() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Pedido");
        modelo.addColumn("Producto");
        modelo.addColumn("Cliente");
        modelo.addColumn("Cantidad");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT pedido.idpedido, productos.nombre_producto, clientes.nombre, pedido.cantidad FROM pedido " +
                    "INNER JOIN productos ON pedido.idproducto = productos.idproductos " +
                    "INNER JOIN clientes ON pedido.idclientes = clientes.idclientes";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[]{
                        resultSet.getInt("idpedido"),
                        resultSet.getString("nombre_producto"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("cantidad")
                });
            }

            tablaPedidos.setModel(modelo);
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

    public void eliminarPedidoSeleccionado() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int selectedRow = tablaPedidos.getSelectedRow();
            if (selectedRow >= 0) {
                int idPedido = (int) tablaPedidos.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este pedido?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Statement statement = conexion.createStatement();
                    String deleteQuery = "DELETE FROM pedido WHERE idpedido = " + idPedido;
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Pedido eliminado exitosamente.");
                        cargarDatosEnJTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el pedido.");
                    }
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

    public Container getPanel() {
        return mainPanel;
    }
}
