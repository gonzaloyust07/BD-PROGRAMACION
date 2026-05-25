package brain;

import datas.Equipo;
import datas.Estadistica;
import datas.Jugador;
import datas.Entrenador;

import java.util.List;
import java.util.Scanner;

public class Operaciones {

    Scanner sc;
    List<Equipo> misClubes;

    public Operaciones(Scanner sc, List<Equipo> misClubes) {
        this.sc = sc;
        this.misClubes = misClubes;
    }

    // Validación del mínimo de 7 jugadores
    public boolean validarEquipo(Equipo e) {
        if (e.getPlantilla().size() < 7) {
            System.out.println("❌ Alerta: '" + e.getNombre() + "' solo posee " + e.getPlantilla().size() + " fichas activas. Requerido: 7.");
            return false;
        }
        System.out.println("✅ OK: '" + e.getNombre() + "' dispone del mínimo reglamentario.");
        return true;
    }

    // CASE 1: Registrar Nuevo Club
    public void registrarNuevoClub() {
        System.out.print("Nombre del nuevo club: ");
        String nombreClub = sc.nextLine();
        System.out.print("Presupuesto de la entidad: ");
        double pres = sc.nextDouble();
        sc.nextLine();

        misClubes.add(new Equipo(nombreClub, pres));
        System.out.println("Club '" + nombreClub + "' añadido correctamente en memoria.");
    }

    // CASE 2: Inscribir Jugador en un Club
    public void inscribirJugador() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No existen clubes registrados.");
            return;
        }

        System.out.println("Seleccione el club de destino:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        System.out.print("Número de club: ");
        int indice = sc.nextInt() - 1;
        sc.nextLine();

        if (indice < 0 || indice >= misClubes.size()) {
            System.out.println("⚠️ Índice incorrecto.");
            return;
        }

        System.out.print("Dorsal (número): ");
        int numero = sc.nextInt();
        sc.nextLine();
        System.out.print("Nombre completo: ");
        String nombreJug = sc.nextLine();
        System.out.print("Nombre en la camiseta: ");
        String nombreCam = sc.nextLine();
        while (nombreCam == null || nombreCam.trim().isEmpty()) {
            System.out.print("Debe introducir un nombre en la camiseta: ");
            nombreCam = sc.nextLine();
        }
        System.out.print("Edad del jugador: ");
        int edad = sc.nextInt();
        sc.nextLine();
        System.out.print("Precio de mercado (€): ");
        double precio = sc.nextDouble();
        sc.nextLine();

        Jugador nuevo = new Jugador(numero, nombreJug, nombreCam, edad);
        nuevo.setPrecio(precio);

        misClubes.get(indice).agregarJugador(nuevo);
        System.out.println("🏃‍♂️ Ficha tramitada con éxito. ID asignado: " + nuevo.getIdJugador());
    }

    // CASE 3: Auditar Mínimo de Jugadores (7)
    public void auditarMinimoJugadores() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes disponibles.");
            return;
        }
        System.out.println("Seleccione el club a auditar:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        int indAuditar = sc.nextInt() - 1;
        sc.nextLine();

        if (indAuditar >= 0 && indAuditar < misClubes.size()) {
            validarEquipo(misClubes.get(indAuditar));
        } else {
            System.out.println("⚠️ Selección incorrecta.");
        }
    }

    // CASE 4: Asignar Entrenador de un Club
    public void asignarEntrenador() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes disponibles.");
            return;
        }

        System.out.println("Seleccione el club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        System.out.print("Número de club: ");
        int indice = sc.nextInt() - 1;
        sc.nextLine();

        if (indice < 0 || indice >= misClubes.size()) {
            System.out.println("⚠️ Índice incorrecto.");
            return;
        }

        Equipo equipoSeleccionado = misClubes.get(indice);

        if (equipoSeleccionado.getEntrenador() != null) {
            System.out.println("⚠️ Este club ya tiene un entrenador: " + equipoSeleccionado.getEntrenador().infoCorta());
            System.out.println("No se puede asignar otro entrenador.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Edad: ");
        int edad = sc.nextInt();
        sc.nextLine();
        System.out.print("Nacionalidad: ");
        String nacionalidad = sc.nextLine();
        sc.nextLine();
        System.out.print("Precio (€): ");
        double precio = sc.nextDouble();
        sc.nextLine();

        Entrenador nuevoEntrenador = new Entrenador(nombre, edad, nacionalidad, precio);
        equipoSeleccionado.setEntrenador(nuevoEntrenador);
        System.out.println("✅ Entrenador asignado correctamente al club '" + equipoSeleccionado.getNombre() + "'");
    }

    // CASE 5: Compra/Traspaso de Jugador
    public void compraTraspaso() {
        if (misClubes.size() < 2) {
            System.out.println("❌ Se requieren al menos dos clubes para un traspaso.");
            return;
        }

        System.out.println("Seleccione el club vendedor:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        int vendedorIdx = sc.nextInt() - 1;
        sc.nextLine();

        if (vendedorIdx < 0 || vendedorIdx >= misClubes.size()) {
            System.out.println("Índice incorrecto.");
            return;
        }

        Equipo vendedor = misClubes.get(vendedorIdx);

        System.out.println("Jugadores del club vendedor (ID - Nombre camiseta - Dorsal):");
        for (Jugador j : vendedor.getPlantilla()) {
            System.out.println(j.getIdJugador() + " - " + j.infoCorta());
        }

        System.out.print("ID del jugador a traspasar: ");
        int idTraspaso = sc.nextInt();
        sc.nextLine();

        Jugador jug = vendedor.getJugadorPorId(idTraspaso);
        if (jug == null) {
            System.out.println("Jugador no encontrado en el club vendedor.");
            return;
        }

        System.out.println("Seleccione el club comprador:");
        for (int i = 0; i < misClubes.size(); i++) {
            if (i == vendedorIdx) continue;
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre() + " (Presupuesto: " + misClubes.get(i).getPresupuesto() + " €)");
        }

        int compradorIdx = sc.nextInt() - 1;
        sc.nextLine();

        if (compradorIdx < 0 || compradorIdx >= misClubes.size() || compradorIdx == vendedorIdx) {
            System.out.println("Índice incorrecto.");
            return;
        }

        Equipo comprador = misClubes.get(compradorIdx);

        System.out.print("Precio de traspaso acordado (€): ");
        double precioTraspaso = sc.nextDouble();
        sc.nextLine();

        if (comprador.getPresupuesto() < precioTraspaso) {
            System.out.println("❌ El comprador no dispone de presupuesto suficiente.");
            return;
        }

        // Realizar traspaso
        boolean eliminado = vendedor.eliminarJugadorPorId(jug.getIdJugador());
        if (!eliminado) {
            System.out.println("Error eliminando jugador del vendedor.");
            return;
        }

        comprador.agregarJugador(jug);
        comprador.setPresupuesto(comprador.getPresupuesto() - precioTraspaso);
        vendedor.setPresupuesto(vendedor.getPresupuesto() + precioTraspaso);
        System.out.println("✅ Traspaso completado. " + jug.getNombreCompleto() + " ahora en " + comprador.getNombre());
    }

    // CASE 6: Borrar Jugador de un Equipo
    public void borrarJugador() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes.");
            return;
        }

        System.out.println("Seleccione club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        int idxClub = sc.nextInt() - 1;
        sc.nextLine();

        if (idxClub < 0 || idxClub >= misClubes.size()) {
            System.out.println("Índice incorrecto.");
            return;
        }

        Equipo club = misClubes.get(idxClub);
        System.out.println("Jugadores del club:");
        for (Jugador j : club.getPlantilla()) {
            System.out.println(j.getIdJugador() + " - " + j.infoCorta());
        }

        System.out.print("ID del jugador a borrar: ");
        int idBorrar = sc.nextInt();
        sc.nextLine();

        if (club.eliminarJugadorPorId(idBorrar)) {
            System.out.println("✅ Jugador eliminado.");
        } else {
            System.out.println("❌ No se encontró el jugador.");
        }
    }

    // CASE 7: Marcar/Desmarcar Jugador Lesionado
    public void marcarDesmarcarLesion() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes.");
            return;
        }

        System.out.println("Seleccione club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        int idxC = sc.nextInt() - 1;
        sc.nextLine();

        if (idxC < 0 || idxC >= misClubes.size()) {
            System.out.println("Índice incorrecto.");
            return;
        }

        Equipo equipoLes = misClubes.get(idxC);
        System.out.println("Jugadores:");
        for (Jugador j : equipoLes.getPlantilla()) {
            System.out.println(j.getIdJugador() + " - " + j.infoCorta() + " - Lesionado: " + (j.isLesionado() ? "Sí" : "No"));
        }

        System.out.print("ID del jugador a cambiar estado: ");
        int idLes = sc.nextInt();
        sc.nextLine();

        Jugador jj = equipoLes.getJugadorPorId(idLes);
        if (jj == null) {
            System.out.println("Jugador no encontrado.");
            return;
        }

        jj.setLesionado(!jj.isLesionado());
        System.out.println("Estado actualizado: Lesionado = " + (jj.isLesionado() ? "Sí" : "No"));
    }

    // CASE 8: Mostrar Club (Información completa)
    public void mostrarClub() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes.");
            return;
        }

        System.out.println("Seleccione club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        int idxP = sc.nextInt() - 1;
        sc.nextLine();

        if (idxP < 0 || idxP >= misClubes.size()) {
            System.out.println("Índice incorrecto.");
            return;
        }

        Equipo club = misClubes.get(idxP);

        System.out.println("\n========================================");
        System.out.println("          INFORMACIÓN DEL CLUB          ");
        System.out.println("========================================");
        System.out.println("🏆 Club: " + club.getNombre());
        System.out.println("💰 Presupuesto: " + club.getPresupuesto() + " €");
        System.out.println("👥 Número de jugadores: " + club.getPlantilla().size());
        System.out.println();

        // Información del entrenador
        if (club.getEntrenador() != null) {
            System.out.println("========================================");
            System.out.println("               ENTRENADOR               ");
            System.out.println("========================================");
            System.out.println(club.getEntrenador().infoCorta());
            System.out.println();
        } else {
            System.out.println("⚠️ Entrenador: Sin asignar\n");
        }

        // Plantilla de jugadores
        if (club.getPlantilla().isEmpty()) {
            System.out.println("❌ Este club no tiene jugadores registrados.\n");
        } else {
            System.out.println("========================================");
            System.out.println("               PLANTILLA                ");
            System.out.println("========================================");
            for (Jugador j : club.getPlantilla()) {
                System.out.println("  " + j.infoCorta());
            }
            System.out.println();
        }

        System.out.println("========================================\n");
    }

    // CASE 9: Buscar Jugador por ID
    public void buscarJugadorPorId() {
        System.out.print("ID jugador: ");
        int idBuscar = sc.nextInt();
        sc.nextLine();

        boolean encontrado = false;
        for (Equipo e : misClubes) {
            Jugador found = e.getJugadorPorId(idBuscar);
            if (found != null) {
                System.out.println(found.toString());
                System.out.println("Club: " + e.getNombre());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró jugador con ese ID.");
        }
    }

    // CASE 10: Insertar estadísticas en un jugador
    public void insertarEstadisticas() {
        if (misClubes.isEmpty()) {
            System.out.println("❌ No hay clubes disponibles.");
            return;
        }

        System.out.println("Seleccione el club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        System.out.print("Número de club: ");
        int indice = sc.nextInt() - 1;
        sc.nextLine();

        if (indice < 0 || indice >= misClubes.size()) {
            System.out.println("⚠️ Índice incorrecto.");
            return;
        }

        Equipo equipo = misClubes.get(indice);
        System.out.println("Jugadores del club:");
        for (Jugador j : equipo.getPlantilla()) {
            System.out.println(j.getIdJugador() + " - " + j.infoCorta());
        }

        System.out.print("ID del jugador: ");
        int idJugador = sc.nextInt();
        sc.nextLine();

        Jugador jugador = equipo.getJugadorPorId(idJugador);
        if (jugador == null) {
            System.out.println("❌ Jugador no encontrado.");
            return;
        }

        System.out.println("\n=== Insertar Estadísticas para: " + jugador.getNombreCompleto() + " ===");
        System.out.print("Partidos jugados: ");
        int partidos = sc.nextInt();
        System.out.print("Puntos anotados: ");
        int puntos = sc.nextInt();
        System.out.print("Faltas: ");
        int faltas = sc.nextInt();
        System.out.print("Bloqueos: ");
        int bloqueos = sc.nextInt();

        Estadistica nuevo = new Estadistica(partidos, puntos, faltas, bloqueos);

        System.out.println("✅ Estadísticas guardadas correctamente para " + jugador.getNombreCompleto());
        System.out.println(nuevo.toString());
    }

}