package admin;

import databaseConexion.dbTests;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class clientes {
    private JTextField idText;
    private JTextField nombreText;
    private JTextField apellidoText;
    private JTextField tipoText;
    private JTextField direccionText;
    private JTextField barrioText;
    private JTextField municipioText;
    private JTextField telefonoText;
    private JButton guardarCambiosButton;
    private JButton eliminarClienteButton;
    private JButton añadirClienteNuevoButton;
    private JTable clientesTable;
    private JPanel mainPanel;

<<<<<<< HEAD
    public Container getPanel() {
        return mainPanel;
    }
=======
    public JPanel getPanel() {
        return mainPanel;
    }

>>>>>>> 6f4e2c3d6a95b0cb49b1f6e199635fc5d42a97b5
}