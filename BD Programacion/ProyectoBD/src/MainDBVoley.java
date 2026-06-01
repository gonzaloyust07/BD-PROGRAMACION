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
        Connection testConexion = ConectionDB.conectar();

        if (testConexion != null) {
            System.out.println("✅ ¡Conexión JDBC establecida con éxito! Base de datos en línea.\n");
        } else {
            System.out.println("⚠️ ADVERTENCIA: No hay conexión con MySQL (El programa continuará en modo Local).");
        }

        // 1. Cargar clubes desde el archivo binario
        List<Equipo> misClubes = gestor.cargarClubesBinario(ARCHIVO_DATOS);

        // 2. Comprobación simple de carga
        if (misClubes != null && !misClubes.isEmpty()) {
            System.out.println("📦 Datos recuperados. Equipos cargados: " + misClubes.size());
        } else {
            System.out.println("ℹ️ No se detectaron datos previos o el archivo estaba vacío.");
        }

        // Crear instancia de Operaciones
        Operaciones operaciones = new Operaciones(sc, misClubes);

        int opcion;

        do {
            mostrarMenu();
            // Control de errores básico por si el usuario introduce letras en vez de números
            while (!sc.hasNextInt()) {
                System.out.print("⚠️ Por favor, introduce un número válido: ");
                sc.next();
            }
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar el búfer

            if (opcion == 0) break;

            ejecutarOpcion(opcion, operaciones);

        } while (opcion != 0);

        // Guardar datos al salir
        System.out.println("\n🔄 Guardando datos...");
        gestor.guardarTodosClubesBinario(misClubes, ARCHIVO_DATOS);
        System.out.println("✅ ¡Datos guardados. Programa cerrado con éxito!");

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("         MENÚ DE GESTIÓN LIGA           ");
        System.out.println("========================================");
        System.out.println("1. Registrar Nuevo Club");
        System.out.println("2. Inscribir Jugador en un Club");
        System.out.println("3. Revisar Parámetros de Inscripción");
        System.out.println("4. Asignar Entrenador a un Club");
        System.out.println("5. Compra/Traspaso de Jugador");
        System.out.println("6. Borrar Jugador de un Equipo");
        System.out.println("7. Marcar/Desmarcar Jugador Lesionado");
        System.out.println("8. Mostrar Información de un Club");
        System.out.println("9. Buscar Jugador por ID");
        System.out.println("0. Guardar y salir del Sistema");
        System.out.print("Seleccione operación (0-9): ");
    }

    private static void ejecutarOpcion(int opcion, Operaciones operaciones) {
        switch (opcion) {
            case 1: operaciones.registrarNuevoClub();
            break;

            case 2: operaciones.inscribirJugador();
            break;

            case 3: operaciones.revisarMinimoJugadores();
            break;

            case 4: operaciones.asignarEntrenador();
            break;

            case 5: operaciones.compraTraspaso();
            break;

            case 6: operaciones.borrarJugador();
            break;

            case 7: operaciones.marcarDesmarcarLesion();
            break;

            case 8: operaciones.mostrarClub();
            break;

            case 9: operaciones.buscarJugadorPorId();
            break;

            default: System.out.println("⚠️ Opción no existente.");
            break;
        }
    }
}
