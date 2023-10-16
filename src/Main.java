import javax.swing.*;
import admin.usuarios;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Usuarios");

                // Crear una instancia de la clase 'usuarios' para cargar los datos en la JTable
                usuarios usuariosView = new usuarios();
                usuariosView.cargarDatosEnJTable();

                // Agregar el panel de la vista de usuarios al JFrame en lugar del loginForm
                frame.getContentPane().add(usuariosView.getPanel()); // Asumiendo que usuarios tiene un método getPanel()

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 400); // Establecer el tamaño adecuado
                frame.setVisible(true);
            }
        });
    }
}
