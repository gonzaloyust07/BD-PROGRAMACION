import brain.GestorArchivos;
import conection.ConectionDB;
import datas.Equipo;
import vista.VentanaLogin;
import vista.VentanaPrincipal;
import controlador.ControladorApp;

import javax.swing.UIManager;
import java.sql.Connection;
import java.util.List;

public class MainDBVoley {

    public static void main(String[] args) {
        final String ARCHIVO_DATOS = "datos.dat";
        GestorArchivos gestor = new GestorArchivos();

        // 1. Comprobación y log inicial de la BD (Reutilizando tu lógica existente)
        System.out.println("🔄 Iniciando sistema gráfico... Comprobando motor MySQL...");
        Connection testConexion = ConectionDB.conectar();
        if (testConexion != null) {
            System.out.println("✅ ¡Conexión JDBC establecida con éxito! Base de datos en línea.\n");
        } else {
            System.out.println("⚠️ ADVERTENCIA: No hay conexión con MySQL (Modo Local habilitado).");
        }

        // 2. Cargar clubes desde el archivo binario (Reutilizando tu persistencia)
        List<Equipo> misClubes = gestor.cargarClubesBinario(ARCHIVO_DATOS);
        if (misClubes != null && !misClubes.isEmpty()) {
            System.out.println("📦 Datos recuperados. Equipos en memoria: " + misClubes.size());
        }

        // 3. Aplicar estética Look & Feel del Sistema Operativo (Mejora estética obligatoria del PDF)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error configurando el entorno visual Look & Feel.");
        }

        // 4. Lanzar el entorno MVC Gráfico en lugar del bucle Scanner por consola
        // Creamos las instancias de las pantallas que tienes en el paquete vista
        VentanaLogin login = new VentanaLogin();
        VentanaPrincipal principal = new VentanaPrincipal();

        // Inicializamos tu Controlador pasándole las vistas para administrar la aplicación
        new ControladorApp(login, principal);

        // Agregamos un gancho de apagado (Shutdown Hook) para asegurar que,
        // cuando el usuario cierre la ventana de Swing, los datos se guarden en el binario automáticamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔄 Detectado cierre de interfaz. Guardando datos en archivo binario...");
            gestor.guardarTodosClubesBinario(misClubes, ARCHIVO_DATOS);
            System.out.println("✅ ¡Datos respaldados con éxito! Sistema cerrado.");
        }));

        // Hacemos visible la pantalla de acceso (Login)
        login.setVisible(true);
    }
}
