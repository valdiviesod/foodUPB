import user.loginForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Login"); // Título de la ventana
                loginForm loginForm = new loginForm(); // Instancia de la clase loginForm
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Establecer el tamaño de la ventana
                frame.setSize(800, 600);

                // Hacer que la ventana sea visible
                frame.setVisible(true);
            }
        });
    }
}
