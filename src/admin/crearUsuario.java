package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class crearUsuario {
    private JButton añadirButton;
    private JTextField usuarioText;
    private JTextField contraText;
    private JTextField tipoText;
    private JPanel mainPanel;

    public crearUsuario() {
        añadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                añadirUsuarioABaseDeDatos();
            }
        });
    }

    public void añadirUsuarioABaseDeDatos() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            String usuario = usuarioText.getText();
            String tipoUsuario = tipoText.getText();
            String contrasena = contraText.getText();

            Statement statement = conexion.createStatement();
            String insertQuery = "INSERT INTO usuarios (usuario, tipo_usuario, contrasena) VALUES ('" + usuario + "', '" + tipoUsuario + "', '" + contrasena + "')";
            int rowsInserted = statement.executeUpdate(insertQuery);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Usuario añadido exitosamente.");
                // Puedes limpiar los campos de texto después de añadir el usuario si lo deseas.
                usuarioText.setText("");
                tipoText.setText("");
                contraText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo añadir el usuario.");
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Crear Usuario");
        frame.setContentPane(new crearUsuario().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Container getPanel() {
        return mainPanel;
    }
}
