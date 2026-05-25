import brain.Operaciones;
import conection.ConectionDB;
import datas.Equipo;
import brain.GestorArchivos;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class MainDBVoley {

    public static void main(String[] args) {
        final String ARCHIVO_DATOS = "datos.dat";
        Scanner sc = new Scanner(System.in);
        GestorArchivos gestor = new GestorArchivos();

        // Comprobación de la BD
        System.out.println("🔄 Iniciando sistema... Comprobando enlace con el motor MySQL...");
        Connection testConn = ConectionDB.conectar();

        if (testConn != null) {
            System.out.println("✅¡Conexión JDBC establecida con éxito! Base de datos en línea.\n");
        } else {
            System.err.println("⚠️ Advertencia: No hay conexión con MySQL. El programa continuará en modo Local.\n");
        }

        List<Equipo> misClubes = gestor.cargarClubesBinario(ARCHIVO_DATOS);

        // Crear instancia de Operaciones
        Operaciones operaciones = new Operaciones(sc, misClubes);

        int opcion;

        do {
            mostrarMenu();
            opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 0) break;

            ejecutarOpcion(opcion, operaciones);

        } while (opcion != 0);

        // Guardado binario antes de cerrar
        System.out.println("Guardando datos...");
        gestor.guardarTodosClubesBinario(misClubes, ARCHIVO_DATOS);
        System.out.println("👋 ¡Datos guardados. Programa cerrado!");

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("========================================");
        System.out.println("         MENÚ DE GESTIÓN LIGA           ");
        System.out.println("========================================");
        System.out.println("1. Registrar Nuevo Club");
        System.out.println("2. Inscribir Jugador en un Club");
        System.out.println("3. Auditar Mínimo de Jugadores (7)");
        System.out.println("4. Asignar Entrenador a un Club");
        System.out.println("5. Compra/Traspaso de Jugador");
        System.out.println("6. Borrar Jugador de un Equipo");
        System.out.println("7. Marcar/Desmarcar Jugador Lesionado");
        System.out.println("8. Mostrar Información de un Club");
        System.out.println("9. Buscar Jugador por ID");
        System.out.println("10. Insertar Estadísticas en un Jugador");
        System.out.println("0. Salir del Sistema");
        System.out.print("Seleccione operación (0-10): ");
    }

    private static void ejecutarOpcion(int opcion, Operaciones operaciones) {
        switch (opcion) {
            case 1:
                operaciones.registrarNuevoClub();
                break;

            case 2:
                operaciones.inscribirJugador();
                break;

            case 3:
                operaciones.auditarMinimoJugadores();
                break;

            case 4:
                operaciones.asignarEntrenador();
                break;

            case 5:
                operaciones.compraTraspaso();
                break;

            case 6:
                operaciones.borrarJugador();
                break;

            case 7:
                operaciones.marcarDesmarcarLesion();
                break;

            case 8:
                operaciones.mostrarClub();
                break;

            case 9:
                operaciones.buscarJugadorPorId();
                break;

            case 10:
                operaciones.insertarEstadisticas();
                break;

            default:
                System.out.println("⚠️ Opción no existente.");
                break;
        }
    }
}