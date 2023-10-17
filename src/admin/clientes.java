package admin;

import javax.swing.*;
import java.awt.*;

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

    public Container getPanel() {
        return mainPanel;
    }

}