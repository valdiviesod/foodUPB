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

}