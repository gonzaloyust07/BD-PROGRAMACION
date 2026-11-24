package conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionDB {
    private static final String URL_PREDETERMINADA = "jdbc:mysql://localhost:3306/voleibol_espana?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(
                configuracion("VOLEY_DB_URL", URL_PREDETERMINADA),
                configuracion("VOLEY_DB_USER", "root"),
                configuracion("VOLEY_DB_PASSWORD", "root"));
    }

    private static String configuracion(String variable, String valorPredeterminado) {
        String valor = System.getenv(variable);
        return valor == null || valor.trim().isEmpty() ? valorPredeterminado : valor.trim();
    }
}
