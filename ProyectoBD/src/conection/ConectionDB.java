package conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Enlace directo JDBC a MySQL
public class ConectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/liga_voley?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";   // Reemplaza con tu contraseña de MySQL

    public static Connection conectar() {
        Connection conn = null;
        try {
            // Línea clave: Lanza la conexión activa hacia el motor MySQL
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            // Línea clave: Captura fallos de red, puertos o credenciales incorrectas
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}
