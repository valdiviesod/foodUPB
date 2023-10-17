package databaseConexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbTests {
    private static final String URL = "jdbc:mysql://172.17.0.1:3306/foodupb";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "Foodupb_";

    public static Connection obtenerConexion() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión exitosa a la base de datos");
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}
