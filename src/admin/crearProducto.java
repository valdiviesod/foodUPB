package admin;

import databaseConexion.dbConexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class crearProducto {
    private JTextField nombreText;
    private JTextField valorText;
    private JButton addProductoButton;
    private JPanel mainPanel;

    public crearProducto() {
        addProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductoABaseDeDatos();
            }
        });
    }

    public void addProductoABaseDeDatos() {
        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            String nombre = nombreText.getText();
            double valor = Double.parseDouble(valorText.getText());

            Statement statement = conexion.createStatement();
            String insertQuery = "INSERT INTO productos (nombre_producto, valor_producto) " +
                    "VALUES ('" + nombre + "', " + valor + ")";
            int rowsInserted = statement.executeUpdate(insertQuery);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Producto añadido exitosamente.");
                // Puedes limpiar los campos de texto después de añadir el producto si lo deseas.
                nombreText.setText("");
                valorText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo añadir el producto.");
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

    public Container getPanel() {
        return mainPanel;
    }
}
