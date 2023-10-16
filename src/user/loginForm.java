package user;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import databaseConexion.dbConexion;
//import databaseConexion.dbTests;

public class loginForm {
    private JPanel panel1;
    private JButton button1;
    private JTextField userTextField;
    private JPasswordField passwordPasswordField;

    public loginForm() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioIngresado = userTextField.getText();
                String contrasenaIngresada = new String(passwordPasswordField.getPassword());

                if (verificarCredenciales(usuarioIngresado, contrasenaIngresada)) {
                    JOptionPane.showMessageDialog(null, "Acceso concedido");
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas");
                }
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT usuario, contrasena FROM usuarios WHERE usuario = ? AND contrasena = ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, usuario);
                ps.setString(2, contrasena);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Usuario y contraseña coinciden en la base de datos
                    conexion.close();
                    return true;
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al verificar credenciales: " + e.getMessage());
            }
        }

        return false;
    }
}
