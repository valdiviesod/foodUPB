package admin;

import databaseConexion.dbTests;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class usuarios {
    private JTextField fieldIdUsuario;
    private JTextField fieldNombreUsuario;
    private JTextField fieldContraUsuario;
    private JTextField fieldTipoUsuario;
    private JButton editarButton;
    private JButton anadirUsuarioButton;
    private JButton eliminarUsuarioButton;
    private JButton guardarCambiosButton; // Nuevo botón para guardar cambios
    private JTable tablaUsuarios;
    private JPanel mainPanel;
    private JScrollBar scrollBar1;
    private JButton clientesButton;
    private JButton irAPedidosButton;
    private JButton irAProductosButton;

    public usuarios() {
        cargarDatosEnJTable();

        // Agrega un ListSelectionListener a la tabla
        tablaUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tablaUsuarios.getSelectedRow();
                if (selectedRow >= 0) {
                    // Obtiene los valores de la fila seleccionada
                    Object idUsuario = tablaUsuarios.getValueAt(selectedRow, 0);
                    Object nombreUsuario = tablaUsuarios.getValueAt(selectedRow, 1);
                    Object tipoUsuario = tablaUsuarios.getValueAt(selectedRow, 2);
                    Object contraUsuario = tablaUsuarios.getValueAt(selectedRow, 3);

                    // Actualiza los campos de texto con los valores obtenidos
                    fieldIdUsuario.setText(idUsuario.toString());
                    fieldNombreUsuario.setText(nombreUsuario.toString());
                    fieldTipoUsuario.setText(tipoUsuario.toString());
                    fieldContraUsuario.setText(contraUsuario.toString());
                }
            }
        });

        // Agrega un ActionListener al botón de "Guardar Cambios"
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambiosEnBaseDeDatos();
            }
        });

        eliminarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuarioSeleccionado();
            }
        });

        anadirUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario crearUsuarioView = new crearUsuario();
                JFrame frame = new JFrame("Añadir Usuario");
                frame.setContentPane(crearUsuarioView.getPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
                frame.pack();
                frame.setVisible(true);
            }
        });

        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de la clase clientes
                clientes clientesView = new clientes();

                // Crear un JFrame para mostrar la vista de clientes
                JFrame clientesFrame = new JFrame("Clientes");
                clientesFrame.setContentPane(clientesView.getPanel());
                clientesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
                clientesFrame.pack();
                clientesFrame.setVisible(true);
            }
        });
    }

    public void cargarDatosEnJTable() {
        Connection conexion = dbTests.obtenerConexion();
        if (conexion == null) {
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre de Usuario");
        modelo.addColumn("Tipo de Usuario");
        modelo.addColumn("Contraseña");

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT * FROM usuarios";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                modelo.addRow(new Object[] {
                        resultSet.getInt("idusuario"),
                        resultSet.getString("usuario"),
                        resultSet.getString("tipo_usuario"),
                        resultSet.getString("contrasena")
                });
            }

            tablaUsuarios.setModel(modelo);
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
        Connection conexion = dbTests.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int idUsuario = Integer.parseInt(fieldIdUsuario.getText());
            String nuevoNombreUsuario = fieldNombreUsuario.getText();
            String nuevoTipoUsuario = fieldTipoUsuario.getText();
            String nuevaContraUsuario = fieldContraUsuario.getText();

            Statement statement = conexion.createStatement();
            String updateQuery = "UPDATE usuarios SET usuario = '" + nuevoNombreUsuario + "', tipo_usuario = '" + nuevoTipoUsuario + "', contrasena = '" + nuevaContraUsuario + "' WHERE idusuario = " + idUsuario;
            int rowsUpdated = statement.executeUpdate(updateQuery);

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Cambios guardados exitosamente.");
                cargarDatosEnJTable(); // Actualizar la tabla con los nuevos datos
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

    public void eliminarUsuarioSeleccionado() {
        Connection conexion = dbTests.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int selectedRow = tablaUsuarios.getSelectedRow();
            if (selectedRow >= 0) {
                int idUsuario = (int) tablaUsuarios.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Statement statement = conexion.createStatement();
                    String deleteQuery = "DELETE FROM usuarios WHERE idusuario = " + idUsuario;
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
                        cargarDatosEnJTable(); // Actualizar la tabla después de la eliminación
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el usuario.");
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



    public Component getPanel() {
        return mainPanel;
    }
}
