package operador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import databaseConexion.dbConexion;

public class registroUsuario {
    private JTextField nombretxt;
    private JTextField numerotxt;
    private JTextField apellidostxt;
    private JTextField direcciontxt;
    private JTextField barriotxt;
    private JTextField municipiotxt;
    private JTextField tipoclientetxt;
    private JButton registrarClienteButton;
    private JPanel panelUsuario;

    public registroUsuario() {
        registrarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombretxt.getText();
                String numero = numerotxt.getText();
                String apellido = apellidostxt.getText();
                String direccion = direcciontxt.getText();
                String barrio = barriotxt.getText();
                String municipio = municipiotxt.getText();
                String tipoCliente = tipoclientetxt.getText();

                Connection con = dbConexion.obtenerConexion();

                if (con != null) {
                    try {
                        String sql = "INSERT INTO clientes (telefono, nombre, apellido, tipo_cliente, direccion, barrio, municipio) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, numero);
                        statement.setString(2, nombre);
                        statement.setString(3, apellido);
                        statement.setString(4, tipoCliente);
                        statement.setString(5, direccion);
                        statement.setString(6, barrio);
                        statement.setString(7, municipio);

                        statement.executeUpdate();

                        nombretxt.setText("");
                        numerotxt.setText("");
                        apellidostxt.setText("");
                        direcciontxt.setText("");
                        barriotxt.setText("");
                        municipiotxt.setText("");
                        tipoclientetxt.setText("");

                        JOptionPane.showMessageDialog(null, "Cliente registrado con éxito");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al registrar el cliente: " + ex.getMessage());
                    } finally {
                        try {
                            con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public JPanel getPanelUsuario() {
        return panelUsuario;
    }
}
