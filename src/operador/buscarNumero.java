package operador;

import databaseConexion.dbConexion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class buscarNumero {
    private JPanel panel1;
    private JTextField textField1;
    private JButton buscarNumeroButton;

    public buscarNumero() {
        buscarNumeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroBuscado = textField1.getText();

                String nombreCliente = obtenerNombreCliente(numeroBuscado);

                if (nombreCliente != null) {
                    JOptionPane.showMessageDialog(null, "El número " + numeroBuscado + " pertenece a " + nombreCliente);
                    abrirVentanaRealizarPedido();
                } else {
                    JOptionPane.showMessageDialog(null, "El número " + numeroBuscado + " no se encuentra en la base de datos.");
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }

    private String obtenerNombreCliente(String numero) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT nombre FROM clientes WHERE telefono = ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, numero);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // El número se encuentra en la base de datos
                    String nombreCliente = rs.getString("nombre");
                    conexion.close();
                    return nombreCliente;
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al verificar el número en la base de datos: " + e.getMessage());
            }
        }

        return null;
    }

    private void abrirVentanaRealizarPedido() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        frame.dispose(); // Cierra la ventana actual

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                realizarPedido pedidoView = new realizarPedido();
                JFrame newFrame = new JFrame("Realizar Pedido");
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación por completo
                newFrame.setContentPane(pedidoView.getPanel());
                newFrame.pack();
                newFrame.setSize(800, 400);
                newFrame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Buscar Número");
        frame.setContentPane(new buscarNumero().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
