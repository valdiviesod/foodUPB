package cocina;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import databaseConexion.dbConexion; // Importa la clase de conexión a la base de datos
import Estructuras.ColaCocina; // Importa la clase de la cola de cocina

public class verCocina {
    private JTable tablaCocina;
    private JPanel mainPanel;
    private JButton marcarComoListoButton;
    private JButton recargarPaginaButton;

    public verCocina() {
        cargarPedidosEnColaCocina();

        cargarPedidosEnTablaCocina();

        marcarComoListoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarPedidoComoListo();
            }
        });
    }

    private void marcarPedidoComoListo() {
        int filaSeleccionada = tablaCocina.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, selecciona un pedido de la tabla.");
            return;
        }

        int idPedido = (int) tablaCocina.getValueAt(filaSeleccionada, 0); // Suponiendo que la columna 0 contiene el ID del pedido.

        DefaultTableModel modelo = (DefaultTableModel) tablaCocina.getModel();
        modelo.removeRow(filaSeleccionada);
    }

    public void cargarPedidosEnColaCocina() {
        ColaCocina colaCocina = new ColaCocina(10);

        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT idpedido, tipo_cliente, tipo_pedido FROM pedido p " +
                    "JOIN clientes c ON p.idclientes = c.idclientes";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idPedido = resultSet.getInt("idpedido");
                int tipoCliente = resultSet.getInt("tipo_cliente");
                String tipoPedido = resultSet.getString("tipo_pedido");
                int tiempoCoccion;

                // Asigna el tiempo de cocción según el tipo de pedido
                if ("rapido".equals(tipoPedido)) {
                    tiempoCoccion = 1 + (int)(Math.random() * 5); // Entre 1 y 5 minutos
                } else {
                    tiempoCoccion = 6 + (int)(Math.random() * 10); // Superior a 5 minutos
                }

                if (tipoCliente == 1) {
                    colaCocina.insertar(idPedido, 1, tiempoCoccion); // Prioridad 1 para clientes premium
                } else {
                    colaCocina.insertar(idPedido, 2, tiempoCoccion); // Prioridad 2 para clientes normales
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        recargarPaginaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recargarPedidosEnTablaCocina();
            }
        });

    }

    public void recargarPedidosEnTablaCocina() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCocina.getModel();
        modelo.setRowCount(0); // Borra todos los datos actuales de la tabla

        cargarPedidosEnColaCocina();
        cargarPedidosEnTablaCocina();

        JOptionPane.showMessageDialog(mainPanel, "La página se ha recargado exitosamente.");
    }


    public void cargarPedidosEnTablaCocina() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Pedido");
        modelo.addColumn("Prioridad");
        modelo.addColumn("Tiempo de Cocción");
        modelo.addColumn("Tipo de Pedido");
        modelo.addColumn("Producto");
        modelo.addColumn("Cliente");

        ColaCocina.Pedido pedido = ColaCocina.extraer();
        Connection conexion = dbConexion.obtenerConexion();

        while (pedido != null) {
            try {
                Statement statement = conexion.createStatement();
                String query = "SELECT p.idpedido, c.tipo_cliente, p.tipo_pedido, pr.nombre_producto, c.nombre " +
                        "FROM pedido p " +
                        "JOIN productos pr ON p.idproducto = pr.idproductos " +
                        "JOIN clientes c ON p.idclientes = c.idclientes " +
                        "WHERE p.idpedido = " + pedido.numero;

                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    String tipoPedido = resultSet.getString("tipo_pedido");
                    String producto = resultSet.getString("nombre_producto");
                    String cliente = resultSet.getString("nombre");

                    modelo.addRow(new Object[] {
                            pedido.numero,
                            pedido.prioridad,
                            pedido.tiempoCoccion,
                            tipoPedido,
                            producto,
                            cliente
                    });
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            pedido = ColaCocina.extraer();
        }

        tablaCocina.setModel(modelo);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
