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


public class clientes {
    private JTextField idText;
    private JTextField nombreText;
    private JTextField apellidoText;
    private JTextField tipoText;
    private JTextField direccionText;
    private JTextField barrioText;
    private JTextField municipioText;
    private JTextField telefonoText;
    private JButton guardarCambiosButton;
    private JButton eliminarClienteButton;
    private JButton addClienteNuevoButton;
    private JTable clientesTable;
    private JPanel mainPanel;

    public clientes() {
        cargarDatosEnJTable();

        clientesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object idCliente = clientesTable.getValueAt(selectedRow, 0);
                    Object nombreCliente = clientesTable.getValueAt(selectedRow, 1);
                    Object apellidoCliente = clientesTable.getValueAt(selectedRow, 2);
                    Object tipoCliente = clientesTable.getValueAt(selectedRow, 3);
                    Object direccionCliente = clientesTable.getValueAt(selectedRow, 4);
                    Object barrioCliente = clientesTable.getValueAt(selectedRow, 5);
                    Object municipioCliente = clientesTable.getValueAt(selectedRow, 6);
                    Object telefonoCliente = clientesTable.getValueAt(selectedRow, 7);

                    idText.setText(idCliente.toString());
                    nombreText.setText(nombreCliente.toString());
                    apellidoText.setText(apellidoCliente.toString());
                    tipoText.setText(tipoCliente.toString());
                    direccionText.setText(direccionCliente.toString());
                    barrioText.setText(barrioCliente.toString());
                    municipioText.setText(municipioCliente.toString());
                    telefonoText.setText(telefonoCliente.toString());
                }
            }
        });

        guardarCambiosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambiosEnBaseDeDatos();
            }
        });

        eliminarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarClienteSeleccionado();
            }
        });

        addClienteNuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de la clase crearCliente
                crearCliente crearClienteView = new crearCliente();

                // Crear un JFrame para mostrar la vista de crearCliente
                JFrame crearClienteFrame = new JFrame("Crear Cliente");
                crearClienteFrame.setContentPane(crearClienteView.getPanel());
                crearClienteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
                crearClienteFrame.pack();
                crearClienteFrame.setVisible(true);
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
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Tipo");
        modelo.addColumn("Dirección");
        modelo.addColumn("Barrio");
        modelo.addColumn("Municipio");
        modelo.addColumn("Teléfono");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT * FROM clientes";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[] {
                        resultSet.getInt("idclientes"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("tipo_cliente"),
                        resultSet.getString("direccion"),
                        resultSet.getString("barrio"),
                        resultSet.getString("municipio"),
                        resultSet.getString("telefono")
                });
            }

            clientesTable.setModel(modelo);
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
            int idClientes = Integer.parseInt(idText.getText());
            String nuevoNombreCliente = nombreText.getText();
            String nuevoApellidoCliente = apellidoText.getText();
            String nuevoTipoCliente = tipoText.getText();
            String nuevaDireccionCliente = direccionText.getText();
            String nuevoBarrioCliente = barrioText.getText();
            String nuevoMunicipioCliente = municipioText.getText();
            String nuevoTelefonoCliente = telefonoText.getText();

            Statement statement = conexion.createStatement();
            String updateQuery = "UPDATE clientes SET nombre = '" + nuevoNombreCliente + "', apellido = '" + nuevoApellidoCliente + "', tipo_cliente = '" + nuevoTipoCliente + "', direccion = '" + nuevaDireccionCliente + "', barrio = '" + nuevoBarrioCliente + "', municipio = '" + nuevoMunicipioCliente + "', telefono = '" + nuevoTelefonoCliente + "' WHERE idclientes = " + idClientes;
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

    public void eliminarClienteSeleccionado() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int selectedRow = clientesTable.getSelectedRow();
            if (selectedRow >= 0) {
                int idClientes = (int) clientesTable.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Statement statement = conexion.createStatement();
                    String deleteQuery = "DELETE FROM clientes WHERE idclientes = " + idClientes;
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente.");
                        cargarDatosEnJTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el cliente.");
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
