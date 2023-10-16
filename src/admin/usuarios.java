package admin;

import databaseConexion.dbTests;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.event.ListSelectionListener;

public class usuarios {
    private JTextField fieldIdUsuario;
    private JTextField fieldNombreUsuario;
    private JTextField fieldContraUsuario;
    private JTextField fieldTipoUsuario;
    private JButton editarNombreUButton;
    private JButton editarContraButton;
    private JButton editarTipoButton;
    private JButton anadirUsuarioButton;
    private JButton eliminarUsuarioButton;
    private JTable tablaUsuarios;
    private JPanel mainPanel;
    private JScrollBar scrollBar1;

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

    public Component getPanel() {
        return mainPanel;
    }


}

