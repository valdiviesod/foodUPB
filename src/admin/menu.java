package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class menu {
    private JTable tablaMenu;
    private JPanel mainPanel;
    private JButton eliminarMenuButton;

    public menu() {
        cargarDatosEnJTable();

        eliminarMenuButton.addActionListener(e -> eliminarMenuSeleccionado());
    }

    public void cargarDatosEnJTable() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Menú");
        modelo.addColumn("Nombre del Menú");
        modelo.addColumn("Valor del Menú");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT * FROM menu";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[]{
                        resultSet.getInt("idmenu"),
                        resultSet.getString("nombre_menu"),
                        resultSet.getDouble("valor_menu")
                });
            }

            tablaMenu.setModel(modelo);
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

    public void eliminarMenuSeleccionado() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int selectedRow = tablaMenu.getSelectedRow();
            if (selectedRow >= 0) {
                int idMenu = (int) tablaMenu.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este menú?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Statement statement = conexion.createStatement();
                    String deleteQuery = "DELETE FROM menu WHERE idmenu = " + idMenu;
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Menú eliminado exitosamente.");
                        cargarDatosEnJTable(); // Actualizar la tabla después de la eliminación
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el menú.");
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
