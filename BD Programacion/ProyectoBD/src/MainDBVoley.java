import conection.LigaDAO;
import controlador.ControladorApp;
import vista.VentanaLogin;
import vista.VentanaPrincipal;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.sql.SQLException;

public class MainDBVoley {

    public static void main(String[] args) {
        try {
            LigaDAO.comprobarEsquema();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a MySQL o falta alguna tabla necesaria:\n"
                            + ex.getMessage()
                            + "\n\nConfigura VOLEY_DB_URL, VOLEY_DB_USER y VOLEY_DB_PASSWORD.",
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
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
