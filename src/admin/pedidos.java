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
        modelo.addColumn("ID Producto");
        modelo.addColumn("ID Menú");
        modelo.addColumn("Cantidad");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT * FROM pedido";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[]{
                        resultSet.getInt("idpedido"),
                        resultSet.getInt("idproducto"),
                        resultSet.getInt("idmenu"),
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
                        cargarDatosEnJTable(); // Actualizar la tabla después de la eliminación
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
