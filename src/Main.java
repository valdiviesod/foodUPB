import javax.swing.*;
import user.loginForm;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login Form");

                loginForm loginView = new loginForm();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Agregar el panel de la vista al JFrame
                frame.getContentPane().add(loginView.getPanel1());

                frame.setSize(800, 400); // Establecer el tamaño adecuado
                frame.setVisible(true);
            }
        });
    }
}
