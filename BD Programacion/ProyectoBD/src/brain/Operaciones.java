package brain;

import datas.Equipo;
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
        if (e.getPlantilla().size() < 7 || e.getEntrenador() == null) {
            System.out.println("❌ Error: '" + e.getNombre() + "' no posee entrenador o el numero de fichas activas es insuficiente \n(posee " + e.getPlantilla().size() + " fichas activas (Requerido: 7)).\n");
            return false;
        }
        System.out.println("✅ OK: '" + e.getNombre() + "' cumple con el mínimo obligatorio.\n");
        return true;
    }

    // CASE 1: Registrar Nuevo Club
    public void registrarNuevoClub() {
        System.out.print("\nNombre del nuevo club: ");
        String nombreClub = sc.nextLine();
        System.out.print("Presupuesto del club (€): ");
        double presupuesto = sc.nextDouble();

        misClubes.add(new Equipo(nombreClub, presupuesto));
        System.out.println("\n✅ Club '" + nombreClub + "' añadido correctamente en memoria.\n");
    }

    // CASE 2: Inscribir Jugador en un Club
    public void inscribirJugador() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No existen clubes registrados.\n");
            return;
        }

        System.out.println("\nSeleccione el club de destino:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }
        System.out.print("ID de club: ");
        int indice = sc.nextInt() - 1;

        if (indice < 0 || indice >= misClubes.size()) {
            System.out.println("⚠️ ID de club incorrecto.\n");
            return;
        }

        System.out.print("\nDorsal (número): ");
        int numero = sc.nextInt();
        sc.nextLine();
        System.out.print("Nombre completo: ");
        String nombreCompleto = sc.nextLine();

        System.out.print("Nombre en la camiseta: ");
        String nombreCam = sc.nextLine();

        System.out.print("Edad del jugador: ");
        int edad = sc.nextInt();
        sc.nextLine();

        System.out.print("Precio en el mercado (€): ");
        double precio = sc.nextDouble();
        sc.nextLine();

        System.out.print("Nacionalidad: ");
        String nacionalidad = sc.nextLine();

        System.out.print("Posición: ");
        String posicion = sc.nextLine();

        Jugador nuevo = new Jugador(nombreCompleto, nombreCam, numero, edad, nacionalidad, posicion, precio);
        nuevo.setPrecio(precio);

        misClubes.get(indice).agregarJugador(nuevo);
        System.out.println("\n✅ Ficha tramitada con éxito. ID asignado: " + nuevo.getIdJugador() + "\n");
    }

    // CASE 3: Revisar parametros de inscripcion
    public void revisarMinimoJugadores() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes disponibles.\n");
            return;
        }
        System.out.println("\nSeleccione el club a revisar:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;

        if (idClub >= 0 && idClub < misClubes.size()) {
            validarEquipo(misClubes.get(idClub));
        } else {
            System.out.println("\n⚠️ Selección incorrecta.\n");
        }
    }

    // CASE 4: Asignar Entrenador de un Club
    public void asignarEntrenador() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes disponibles.\n");
            return;
        }

        System.out.println("\nSeleccione el club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;
        sc.nextLine();

        if (idClub < 0 || idClub >= misClubes.size()) {
            System.out.println("\n⚠️ Índice incorrecto.\n");
            return;
        }

        Equipo equipoSeleccionado = misClubes.get(idClub);

        if (equipoSeleccionado.getEntrenador() != null) {
            System.out.println("\n⚠️ Este club ya tiene un entrenador: " + equipoSeleccionado.getEntrenador().infoCorta());
            System.out.println("❌ No se puede asignar otro entrenador.\n");
            return;
        }

        System.out.print("\nNombre Completo: ");
        String nombreCompleto = sc.nextLine();

        System.out.print("Edad: ");
        int edad = sc.nextInt();
        sc.nextLine();

        System.out.print("Nacionalidad: ");
        String nacionalidad = sc.nextLine();

        System.out.print("Precio (€): ");
        double precio = sc.nextDouble();
        sc.nextLine();

        Entrenador nuevoEntrenador = new Entrenador(nombreCompleto, edad, nacionalidad, precio);
        equipoSeleccionado.setEntrenador(nuevoEntrenador);
        System.out.println("\n✅ Entrenador asignado correctamente al club '" + equipoSeleccionado.getNombre() + "'\n");
    }

    // CASE 5: Compra/Traspaso de Jugador
    public void compraTraspaso() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes.\n");
            return;
        }

        //seleccion del club vendedor
        System.out.println("\nSeleccione el club de ORIGEN (Vendedor):");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club origen: ");
        int idOrigen = sc.nextInt() - 1;
        sc.nextLine();

        if (idOrigen < 0 || idOrigen >= misClubes.size()) {
            System.out.println("\n⚠️ ID de origen incorrecto.\n");
            return;
        }

        Equipo clubOrigen = misClubes.get(idOrigen);

        // validacion del club vendedor
        if (clubOrigen.getPlantilla().isEmpty()) {
            System.out.println("\n❌ No hay jugadores registrados aún en este club.\n");
            return;
        } else {
            // Mostrar plantilla para elegir jugador
            System.out.println("\nJugadores disponibles en " + clubOrigen.getNombre() + ":");
            for (Jugador j : clubOrigen.getPlantilla()) {
                System.out.println(j.getIdJugador() + " - " + j.infoCorta());
            }

            System.out.print("ID del jugador a traspasar: ");
            int idJugador = sc.nextInt();
            sc.nextLine();

            Jugador jugadorATraspasar = clubOrigen.getJugadorPorId(idJugador);
            if (jugadorATraspasar == null) {
                System.out.println("\n❌ El jugador con ID " + idJugador + " no pertenece a este club.\n");
                return;
            }

            // seleccion del club de destino
            System.out.println("\nSeleccione el club de DESTINO (Comprador):");
            for (int i = 0; i < misClubes.size(); i++) {
                if (i != idOrigen) { // Evita mostrar el mismo equipo de origen
                    System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
                }
            }

            System.out.println("ID del club destino: ");
            int idDestino = sc.nextInt() - 1;
            sc.nextLine();

            if (idDestino < 0 || idDestino >= misClubes.size() || idDestino == idOrigen) {
                System.out.println("\n⚠️ ID de destino incorrecto o inválido.\n");
                return;
            }

            Equipo clubDestino = misClubes.get(idDestino);

            // validacion club de destino
            if (clubDestino.getPlantilla().isEmpty()) {
                System.out.println("\n❌ No hay jugadores registrados aún en este club.\n");
                return;
            } else {
                clubOrigen.eliminarJugadorPorId(idJugador);
                clubDestino.agregarJugador(jugadorATraspasar);

                System.out.println("\n✅ ¡Traspaso completado con éxito!");
                System.out.println("👉 " + jugadorATraspasar.getNombreCamiseta() + " ahora pertenece a " + clubDestino.getNombre() + ".\n");
            }
        }
    }

    // CASE 6: Borrar Jugador de un Equipo
    public void borrarJugador() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes.\n");
            return;
        }

        System.out.println("\nSeleccione el ID del club del jugador a borrar:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;

        Equipo club = misClubes.get(idClub);

        if (idClub < 0 || idClub >= misClubes.size()) {
            System.out.println("\n⚠️ ID incorrecto.\n");
            return;
        } else if (club.getPlantilla().isEmpty()) {
            System.out.println("\n❌ Este equipo aún no tiene ningún jugador registrado.\n");
            return;
        } else {

            System.out.println("\nJugadores del club:");
            for (Jugador j : club.getPlantilla()) {
                System.out.println(j.getIdJugador() + " - " + j.infoCorta());
            }

            System.out.print("ID del jugador a borrar: ");
            int idBorrar = sc.nextInt();

            if (club.eliminarJugadorPorId(idBorrar)) {
                System.out.println("✅ Jugador eliminado.\n");
            } else {
                System.out.println("❌ No se encontró el jugador.\n");
            }
        }
    }

    // CASE 7: Marcar/Desmarcar Jugador Lesionado
    public void marcarDesmarcarLesion() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes.\n");
            return;
        }

        System.out.println("\nClubes:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;
        sc.nextLine();

        if (idClub < 0 || idClub >= misClubes.size()) {
            System.out.println("⚠️ ID incorrecto.\n");
            return;
        }

        Equipo lesionesEquipo = misClubes.get(idClub);

        if (lesionesEquipo.getPlantilla().isEmpty()) {
            System.out.println("\n❌ No hay jugadores registrados aún en este club.\n");
        } else {
            System.out.println("Jugadores:");
            for (Jugador lesionado : lesionesEquipo.getPlantilla()) {
                System.out.println(lesionado.getIdJugador() + " - " + lesionado.infoCorta() + " - Lesionado: " + (lesionado.isLesionado() ? "Sí" : "No"));
            }

            System.out.print("ID del jugador a cambiar estado: ");
            int idLesionado = sc.nextInt();
            sc.nextLine();

            Jugador jugador = lesionesEquipo.getJugadorPorId(idLesionado);
            if (jugador == null) {
                System.out.println("\n❌ Jugador no encontrado.");
                return;
            }

            jugador.setLesionado(!jugador.isLesionado());
            System.out.println("\n✅ Estado actualizado: Lesionado = " + (jugador.isLesionado() ? "Sí" : "No\n"));
        }
    }

    // CASE 8: Mostrar Club (Información completa)
    public void mostrarClub() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes.\n");
            return;
        }

        System.out.println("\nSeleccione club:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;

        if (idClub < 0 || idClub >= misClubes.size()) {
            System.out.println("⚠️ ID incorrecto.\n");
            return;
        }

        Equipo club = misClubes.get(idClub);

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
            System.out.println("⚠️ Entrenador sin asignar");
        }

        // Plantilla de jugadores
        if (club.getPlantilla().isEmpty()) {
            System.out.println("⚠️ Este club no tiene jugadores registrados.\n");
        } else {
            System.out.println("========================================");
            System.out.println("               PLANTILLA                ");
            System.out.println("========================================");
            for (Jugador plantilla : club.getPlantilla()) {
                System.out.println("  " + plantilla.infoCorta() + "\n");
            }
        }
    }

    // CASE 9: Buscar Jugador por ID
    public void buscarJugadorPorId() {
        if (misClubes.isEmpty()) {
            System.out.println("\n❌ No hay clubes registrados.\n");
            return;
        }

        System.out.println("\nClubes:");
        for (int i = 0; i < misClubes.size(); i++) {
            System.out.println((i + 1) + ". " + misClubes.get(i).getNombre());
        }

        System.out.print("ID del club: ");
        int idClub = sc.nextInt() - 1;
        sc.nextLine();

        if (idClub < 0 || idClub >= misClubes.size()) {
            System.out.println("\n⚠️ ID de club incorrecto.\n");
            return;
        }

        Equipo clubSeleccionado = misClubes.get(idClub);

        // Comprobar si el equipo seleccionado no tiene jugadores
        if (clubSeleccionado.getPlantilla().isEmpty()) {
            System.out.println("\n❌ No hay jugadores registrados aún en este club.\n");
        } else {
            // Mostrar la lista de jugadores de ese club
            System.out.println("\nJugadores:");
            for (Jugador j : clubSeleccionado.getPlantilla()) {
                System.out.println(j.getIdJugador() + " - " + j.infoCorta());
            }

            System.out.print("ID del jugador a buscar: ");
            int idJugador = sc.nextInt();
            sc.nextLine();

            boolean encontrado = false;
            for (Equipo club : misClubes) {
                Jugador found = club.getJugadorPorId(idJugador);
                if (found != null) {
                    System.out.println(found.toString());
                    System.out.println("Club: " + club.getNombre() + "\n");
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("⚠️ No se encontró jugador con ese ID.\n");
            }
        }
    }
}
