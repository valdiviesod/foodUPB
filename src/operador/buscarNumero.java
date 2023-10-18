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

                if (verificarNumeroEnBaseDeDatos(numeroBuscado)) {
                    JOptionPane.showMessageDialog(null, "El número " + numeroBuscado + " se encuentra en la base de datos.");
                } else {
                    JOptionPane.showMessageDialog(null, "El número " + numeroBuscado + " no se encuentra en la base de datos.");
                    abrirVentanaRegistroUsuario(); // Abre la ventana registroUsuario
                    cerrarVentanaActual(); // Cierra la ventana actual
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }

    private boolean verificarNumeroEnBaseDeDatos(String numero) {
        Connection conexion = dbConexion.obtenerConexion();

        if (conexion != null) {
            try {
                String consulta = "SELECT telefono FROM clientes WHERE telefono = ?";
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setString(1, numero);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // El número se encuentra en la base de datos
                    conexion.close();

                    // Redirigir a la ventana realizarPedido
                    abrirVentanaRealizarPedido();

                    return true;
                }

                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al verificar el número en la base de datos: " + e.getMessage());
            }
        }

        return false;
    }
    private void abrirVentanaRealizarPedido() {
        // Obtiene la ventana raíz de la ventana actual
        JFrame frame = (JFrame) SwingUtilities.getRoot(panel1);

        if (frame != null) {
            // Cierra la ventana actual
            frame.dispose();

            // Crea una instancia de la vista "realizarPedido" y la muestra
            realizarPedido realizarPedidoView = new realizarPedido();
            JFrame newFrame = new JFrame("Realizar Pedido");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
            newFrame.setContentPane(realizarPedidoView.getPanel()); // Asume que hay un método getPanel() en realizarPedido
            newFrame.pack();
            newFrame.setSize(800, 400); // Establece el tamaño deseado
            newFrame.setVisible(true);
        }
    }

    private void abrirVentanaRegistroUsuario() {
        registroUsuario registroUsuarioView = new registroUsuario();
        JFrame frame = new JFrame("Registro de Cliente");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana al salir
        frame.setContentPane(registroUsuarioView.getPanelUsuario());
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    private void cerrarVentanaActual() {
        SwingUtilities.getWindowAncestor(panel1).dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Buscar Número");
        frame.setContentPane(new buscarNumero().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
