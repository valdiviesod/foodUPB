package user;

import java.awt.Window;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import admin.usuarios;
import databaseConexion.dbConexion;
import operador.buscarNumero;
import cocina.verCocina;
import domicilio.domiClass;

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
                    if (usuarioIngresado.toLowerCase().endsWith("@operador.com")) {
                        // Redirigir al usuario a la ventana buscarNumero
                        abrirVistaBuscarNumero();
                    } else if (usuarioIngresado.toLowerCase().endsWith("@admin.com")) {
                        // Redirigir al usuario a la ventana de usuarios
                        abrirVistaUsuarios();

                    } else if (usuarioIngresado.toLowerCase().endsWith("@domi.com")) {
                        // Redirigir al usuario a la ventana de usuarios
                        abrirVistaDomi();

                    } else if (usuarioIngresado.toLowerCase().endsWith("@cocina.com")) {
                        // Redirigir al usuario a la ventana de usuarios
                        abrirVistaVerCocina();
                    } else {
                        JOptionPane.showMessageDialog(null, "Acceso concedido");
                    }
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

    private void abrirVistaBuscarNumero() {
        // Obtiene la ventana actual
        Window window = SwingUtilities.windowForComponent(panel1);

        if (window instanceof JFrame) {
            // Cierra la ventana actual
            ((JFrame) window).dispose();
        }

        buscarNumero buscarNumeroView = new buscarNumero();
        JFrame frame = new JFrame("Vista de Búsqueda de Número");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
        frame.setContentPane(buscarNumeroView.getPanel());
        frame.pack();
        frame.setSize(800, 400); // Establece el tamaño deseado
        frame.setVisible(true);
    }


    private void abrirVistaUsuarios() {
        // Obtiene la ventana actual
        Window window = SwingUtilities.windowForComponent(panel1);

        if (window instanceof JFrame) {
            // Cierra la ventana actual
            ((JFrame) window).dispose();
        }

        // Crea una instancia de la vista "usuarios" y la muestra
        usuarios usuariosView = new usuarios();
        JFrame frame = new JFrame("Vista de Usuarios");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
        frame.setContentPane(usuariosView.getPanel());
        frame.pack();
        frame.setSize(800, 400); // Establece el tamaño deseado
        frame.setVisible(true);
    }

    private void abrirVistaVerCocina() {
        // Obtiene la ventana actual
        Window window = SwingUtilities.windowForComponent(panel1);

        if (window instanceof JFrame) {
            // Cierra la ventana actual
            ((JFrame) window).dispose();
        }

        // Crea una instancia de la vista "verCocina" y la muestra
        verCocina cocinaView = new verCocina();
        JFrame frame = new JFrame("Vista de Cocina");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
        frame.setContentPane(cocinaView.getMainPanel());
        frame.pack();
        frame.setSize(800, 400); // Establece el tamaño deseado
        frame.setVisible(true);
    }

    private void abrirVistaDomi() {
        // Obtiene la ventana actual
        Window window = SwingUtilities.windowForComponent(panel1);

        if (window instanceof JFrame) {
            // Cierra la ventana actual
            ((JFrame) window).dispose();
        }

        // Crea una instancia de la vista "domiClass" y la muestra
        domiClass domiClassView = new domiClass();
        JFrame frame = new JFrame("Vista de Domicilio");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
        frame.setContentPane(domiClassView.getPanel1());
        frame.pack();
        frame.setSize(800, 400); // Establece el tamaño deseado
        frame.setVisible(true);
    }
}
