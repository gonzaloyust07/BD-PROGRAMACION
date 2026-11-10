package conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Enlace directo JDBC a MySQL
public class ConectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/voleibol_espana?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}
