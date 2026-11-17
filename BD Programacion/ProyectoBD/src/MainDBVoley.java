import conection.ConectionDB;
import controlador.ControladorApp;
import vista.VentanaLogin;
import vista.VentanaPrincipal;

import javax.swing.UIManager;
import java.sql.Connection;
import java.sql.SQLException;

public class MainDBVoley {

    public static void main(String[] args) {
        try (Connection conexion = ConectionDB.conectar()) {
            System.out.println("Conectado a MySQL: " + conexion.getCatalog());
        } catch (SQLException ex) {
            System.err.println("No se pudo conectar a MySQL: " + ex.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("No se pudo configurar el estilo visual del sistema: " + ex.getMessage());
        }

        VentanaLogin login = new VentanaLogin();
        VentanaPrincipal principal = new VentanaPrincipal();
        new ControladorApp(login, principal);
        login.setVisible(true);
    }
}
