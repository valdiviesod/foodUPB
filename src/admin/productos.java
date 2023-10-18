package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class productos {
    private JTextField idText;
    private JTextField nombreText;
    private JTextField valorText;
    private JTable tablaProductos;
    private JPanel mainPanel;
    private JButton guardarCambiosButton;
    private JButton eliminarProductoButton;
    private JButton addProductoButton;

    public productos() {
        cargarDatosEnJTable();

        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tablaProductos.getSelectedRow();
                if (selectedRow >= 0) {
                    Object idProducto = tablaProductos.getValueAt(selectedRow, 0);
                    Object nombreProducto = tablaProductos.getValueAt(selectedRow, 1);
                    Object valorProducto = tablaProductos.getValueAt(selectedRow, 2);

                    idText.setText(idProducto.toString());
                    nombreText.setText(nombreProducto.toString());
                    valorText.setText(valorProducto.toString());
                }
            }
        });

        guardarCambiosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambiosEnBaseDeDatos();
            }
        });

        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProductoSeleccionado();
            }
        });

        addProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearProducto crearProductoView = new crearProducto();
                JFrame frame = new JFrame("Añadir Usuario");
                frame.setContentPane(crearProductoView.getPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
                frame.pack();
                frame.setVisible(true);
            }
        });


    }

    public void cargarDatosEnJTable() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre de Producto");
        modelo.addColumn("Valor de Producto");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT * FROM productos";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[] {
                        resultSet.getInt("idproductos"),
                        resultSet.getString("nombre_producto"),
                        resultSet.getDouble("valor_producto")
                });
            }

            tablaProductos.setModel(modelo);
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

    public void guardarCambiosEnBaseDeDatos() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int idProducto = Integer.parseInt(idText.getText());
            String nuevoNombreProducto = nombreText.getText();
            double nuevoValorProducto = Double.parseDouble(valorText.getText());

            Statement statement = conexion.createStatement();
            String updateQuery = "UPDATE productos SET nombre_producto = '" + nuevoNombreProducto + "', valor_producto = " + nuevoValorProducto + " WHERE idproductos = " + idProducto;
            int rowsUpdated = statement.executeUpdate(updateQuery);

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cambios guardados exitosamente.");
                cargarDatosEnJTable();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudieron guardar los cambios.");
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

    public void eliminarProductoSeleccionado() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow >= 0) {
                int idProducto = (int) tablaProductos.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Statement statement = conexion.createStatement();
                    String deleteQuery = "DELETE FROM productos WHERE idproductos = " + idProducto;
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
                        cargarDatosEnJTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto.");
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
