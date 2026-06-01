package conection;

import datas.Entrenador;
import datas.Equipo;
import datas.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigaDAO {

    //  EQUIPOS
    public static int insertarEquipo(Equipo e) {
        String sql = "INSERT INTO equipo (nombre, presupuesto) VALUES (?, ?)";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getNombre());
            ps.setDouble(2, e.getPresupuesto());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar equipo: " + ex.getMessage());
        }
        return -1;
    }

    // Devuelve todos los equipos de la BD
    public static List<Equipo> obtenerTodosEquipos() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipo";
        try (Connection c = ConectionDB.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Equipo eq = new Equipo(rs.getString("nombre"), rs.getDouble("presupuesto"));
                lista.add(eq);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener equipos: " + ex.getMessage());
        }
        return lista;
    }

    //  JUGADORES
    public static int insertarJugador(Jugador j, int idEquipo) {
        String sqlJ = "INSERT INTO jugador (nombreCompleto, nombreCamiseta, numero, edad, nacionalidad, posicion, precio) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sqlJ, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, j.getNombreCompleto());
            ps.setString(2, j.getNombreCamiseta());
            ps.setInt   (3, j.getNumero());
            ps.setInt   (4, j.getEdad());
            ps.setString(5, j.getNacionalidad());
            ps.setString(6, j.getPosicion());
            ps.setDouble(7, j.getPrecio());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                // Insertar en la tabla N:M
                vincularJugadorEquipo(idEquipo, idGenerado, c);
                return idGenerado;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar jugador: " + ex.getMessage());
        }
        return -1;
    }

    /** Vincula jugador-equipo en la tabla equipo_jugador */
    private static void vincularJugadorEquipo(int idEquipo, int idJugador, Connection c) throws SQLException {
        String sql = "INSERT IGNORE INTO equipo_jugador (idEquipo, idJugador) VALUES (?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idJugador);
            ps.executeUpdate();
        }
    }

    /** Elimina un jugador por su ID (también borra la relación N:M por CASCADE) */
    public static boolean borrarJugador(int idJugador) {
        String sql = "DELETE FROM jugador WHERE idJugador = ?";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("❌ Error al borrar jugador: " + ex.getMessage());
        }
        return false;
    }

    /** Actualiza el estado de lesión de un jugador */
    public static boolean actualizarLesion(int idJugador, boolean lesionado) {
        String sql = "UPDATE jugador SET lesionado = ? WHERE idJugador = ?";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, lesionado ? 1 : 0);
            ps.setInt(2, idJugador);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar lesión: " + ex.getMessage());
        }
        return false;
    }

    /** Traspaso: cambia la asociación equipo_jugador de origen a destino */
    public static boolean traspasar(int idJugador, int idEquipoOrigen, int idEquipoDestino) {
        String sqlDel = "DELETE FROM equipo_jugador WHERE idEquipo = ? AND idJugador = ?";
        String sqlIns = "INSERT IGNORE INTO equipo_jugador (idEquipo, idJugador) VALUES (?, ?)";
        try (Connection c = ConectionDB.conectar()) {
            c.setAutoCommit(false);  // transacción
            try (PreparedStatement psDel = c.prepareStatement(sqlDel);
                 PreparedStatement psIns = c.prepareStatement(sqlIns)) {

                psDel.setInt(1, idEquipoOrigen);
                psDel.setInt(2, idJugador);
                psDel.executeUpdate();

                psIns.setInt(1, idEquipoDestino);
                psIns.setInt(2, idJugador);
                psIns.executeUpdate();

                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                System.err.println("❌ Error en traspaso (rollback): " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error de conexión en traspaso: " + ex.getMessage());
        }
        return false;
    }

    /** Obtiene los jugadores de un equipo concreto (JOIN N:M) */
    public static List<Jugador> obtenerJugadoresDeEquipo(int idEquipo) {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT j.* FROM jugador j " +
                     "JOIN equipo_jugador ej ON j.idJugador = ej.idJugador " +
                     "WHERE ej.idEquipo = ?";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador j = new Jugador(
                    rs.getString("nombreCompleto"),
                    rs.getString("nombreCamiseta"),
                    rs.getInt("numero"),
                    rs.getInt("edad"),
                    rs.getString("nacionalidad"),
                    rs.getString("posicion"),
                    rs.getDouble("precio")
                );
                j.setLesionado(rs.getInt("lesionado") == 1);
                lista.add(j);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener jugadores: " + ex.getMessage());
        }
        return lista;
    }

    // ================================================================
    //  ENTRENADORES
    // ================================================================

    /** Inserta un entrenador y lo asigna a un equipo */
    public static int insertarEntrenador(Entrenador en, int idEquipo) {
        String sqlE = "INSERT INTO entrenador (nombreCompleto, edad, nacionalidad, precio) VALUES (?, ?, ?, ?)";
        try (Connection c = ConectionDB.conectar();
             PreparedStatement ps = c.prepareStatement(sqlE, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, en.getNombreCompleto());
            ps.setInt   (2, en.getEdad());
            ps.setString(3, en.getNacionalidad());
            ps.setDouble(4, en.getPrecio());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                // Actualizar equipo con su entrenador
                actualizarEntrenadorEquipo(idEquipo, idGenerado, c);
                return idGenerado;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar entrenador: " + ex.getMessage());
        }
        return -1;
    }

    private static void actualizarEntrenadorEquipo(int idEquipo, int idEntrenador, Connection c) throws SQLException {
        String sql = "UPDATE equipo SET idEntrenador = ? WHERE idEquipo = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            ps.setInt(2, idEquipo);
            ps.executeUpdate();
        }
    }

    // ================================================================
    //  CONSULTAS EXTRA (para el documento de la profesora)
    // ================================================================

    /** Muestra todos los equipos con el nombre de su entrenador (JOIN 2 tablas) */
    public static void mostrarEquiposConEntrenador() {
        String sql = "SELECT e.nombre AS equipo, e.presupuesto, " +
                     "COALESCE(en.nombreCompleto, 'Sin asignar') AS entrenador " +
                     "FROM equipo e " +
                     "LEFT JOIN entrenador en ON e.idEntrenador = en.idEntrenador";
        try (Connection c = ConectionDB.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Equipos con entrenador ---");
            while (rs.next()) {
                System.out.printf("  %-25s | Presupuesto: %10.0f € | Entrenador: %s%n",
                    rs.getString("equipo"),
                    rs.getDouble("presupuesto"),
                    rs.getString("entrenador"));
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error en consulta equipos: " + ex.getMessage());
        }
    }

    /** Cuenta jugadores por equipo */
    public static void mostrarConteoPorEquipo() {
        String sql = "SELECT e.nombre, COUNT(ej.idJugador) AS total " +
                     "FROM equipo e " +
                     "LEFT JOIN equipo_jugador ej ON e.idEquipo = ej.idEquipo " +
                     "GROUP BY e.idEquipo";
        try (Connection c = ConectionDB.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Jugadores por equipo ---");
            while (rs.next()) {
                System.out.printf("  %-25s → %d jugadores%n",
                    rs.getString("nombre"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error en conteo: " + ex.getMessage());
        }
    }
}
