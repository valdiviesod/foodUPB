package cocina;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import databaseConexion.dbConexion; // Importa la clase de conexión a la base de datos
import Estructuras.ColaCocina; // Importa la clase de la cola de cocina

public class verCocina {
    private JTable tablaCocina;
    private JPanel mainPanel;

    public verCocina() {
        // Llama a un método para cargar los pedidos de la base de datos en la cola de cocina
        cargarPedidosEnColaCocina();

        // Llama a un método para cargar los pedidos de la cola de cocina en la tabla
        cargarPedidosEnTablaCocina();
    }

    public void cargarPedidosEnColaCocina() {
        ColaCocina colaCocina = new ColaCocina(10);

        Connection conexion = dbConexion.obtenerConexion();
        if (conexion == null) {
            return;
        }

        try {
            Statement statement = conexion.createStatement();
            String query = "SELECT idpedido, tipo_cliente, tiempo_coccion FROM pedido p " +
                    "JOIN clientes c ON p.idclientes = c.idclientes";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idPedido = resultSet.getInt("idpedido");
                int tipoCliente = resultSet.getInt("tipo_cliente");
                int tiempoCoccion = resultSet.getInt("tiempo_coccion");

                // Inserta el pedido en la cola de cocina con la prioridad adecuada
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
    }

    public void cargarPedidosEnTablaCocina() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Pedido");
        modelo.addColumn("Prioridad");
        modelo.addColumn("Tiempo de Cocción");

        // Obtén los pedidos de la cola de cocina
        ColaCocina.Pedido pedido = ColaCocina.extraer();
        while (pedido != null) {
            modelo.addRow(new Object[] {
                    pedido.numero,
                    pedido.prioridad,
                    pedido.tiempoCoccion
            });
            pedido = ColaCocina.extraer();
        }

        tablaCocina.setModel(modelo);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
