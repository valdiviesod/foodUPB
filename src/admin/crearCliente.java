package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class crearCliente {
    private JButton añadirButton;
    private JTextField telefonoText;
    private JTextField nombreText;
    private JTextField apellidoText;
    private JTextField tipoClienteText;
    private JTextField direccionText;
    private JTextField barrioText;
    private JTextField municipioText;
    private JPanel mainPanel;

    public crearCliente() {
        añadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                añadirClienteABaseDeDatos();
            }
        });
    }

    public void añadirClienteABaseDeDatos() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            int telefono = Integer.parseInt(telefonoText.getText());
            String nombre = nombreText.getText();
            String apellido = apellidoText.getText();
            String tipoCliente = tipoClienteText.getText();
            String direccion = direccionText.getText();
            String barrio = barrioText.getText();
            String municipio = municipioText.getText();

            Statement statement = conexion.createStatement();
            String insertQuery = "INSERT INTO clientes (telefono, nombre, apellido, tipo_cliente, direccion, barrio, municipio) " +
                    "VALUES ('" + telefono + "', '" + nombre + "', '" + apellido + "', '" + tipoCliente + "', '" + direccion + "', '" + barrio + "', '" + municipio + "')";
            int rowsInserted = statement.executeUpdate(insertQuery);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Cliente añadido exitosamente.");
                // Puedes limpiar los campos de texto después de añadir el cliente si lo deseas.
                telefonoText.setText("");
                nombreText.setText("");
                apellidoText.setText("");
                tipoClienteText.setText("");
                direccionText.setText("");
                barrioText.setText("");
                municipioText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo añadir el cliente.");
            }
        } catch (SQLException | NumberFormatException e) {
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
        JFrame frame = new JFrame("Crear Cliente");
        frame.setContentPane(new crearCliente().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Container getPanel() {
        return mainPanel;
    }
}
