package conection;

import datas.Comunidad;
import datas.EquipoLiga;
import datas.JugadorLiga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LigaDAO {

    public static boolean autenticar(String usuario, String password) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE login = ? AND password = ?";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario);
            sentencia.setString(2, password);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }

    public static void comprobarEsquema() throws SQLException {
        String sql = "SELECT 1 FROM usuarios, equipos, jugadores, equipo_jugador, ccaa LIMIT 0";
        try (Connection conexion = ConectionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            resultado.next();
        }
    }

    public static List<EquipoLiga> obtenerEquipos() throws SQLException {
        List<EquipoLiga> equipos = new ArrayList<>();
        String sql = "SELECT e.id_equipo, e.nombre, e.rama, e.division, e.localidad, " +
                "c.nombre AS comunidad, e.fundacion " +
                "FROM equipos e JOIN ccaa c ON e.id_ccaa = c.id_ccaa ORDER BY e.nombre";

        try (Connection conexion = ConectionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                Object anio = resultado.getObject("fundacion");
                equipos.add(new EquipoLiga(
                        resultado.getInt("id_equipo"),
                        resultado.getString("nombre"),
                        resultado.getString("rama"),
                        resultado.getString("division"),
                        resultado.getString("localidad"),
                        resultado.getString("comunidad"),
                        anio == null ? null : ((Number) anio).intValue()));
            }
        }
        return equipos;
    }

    public static List<Comunidad> obtenerComunidades() throws SQLException {
        List<Comunidad> comunidades = new ArrayList<>();
        String sql = "SELECT id_ccaa, nombre FROM ccaa ORDER BY nombre";
        try (Connection conexion = ConectionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                comunidades.add(new Comunidad(
                        resultado.getInt("id_ccaa"),
                        resultado.getString("nombre")));
            }
        }
        return comunidades;
    }

    public static List<JugadorLiga> obtenerJugadores(int idEquipo) throws SQLException {
        List<JugadorLiga> jugadores = new ArrayList<>();
        String sql = "SELECT j.id_jugador, j.nombre, j.posicion, j.numero, j.edad, " +
                "j.nacionalidad, j.capitan, e.id_equipo, COALESCE(e.nombre, 'Sin equipo') AS equipo " +
                "FROM jugadores j LEFT JOIN equipo_jugador ej ON j.id_jugador = ej.id_jugador " +
                "LEFT JOIN equipos e ON ej.id_equipo = e.id_equipo " +
                "WHERE ? = 0 OR ej.id_equipo = ? ORDER BY j.nombre";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEquipo);
            sentencia.setInt(2, idEquipo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    jugadores.add(new JugadorLiga(
                            resultado.getInt("id_jugador"),
                            resultado.getString("nombre"),
                            resultado.getString("posicion"),
                            resultado.getInt("numero"),
                            resultado.getInt("edad"),
                            resultado.getString("nacionalidad"),
                            resultado.getBoolean("capitan"),
                            resultado.getInt("id_equipo"),
                            resultado.getString("equipo")));
                }
            }
        }
        return jugadores;
    }

    public static int insertarEquipo(String nombre, String rama, String division,
                                     String localidad, Integer fundacion, int idComunidad)
            throws SQLException {
        String sql = "INSERT INTO equipos (nombre, rama, division, localidad, fundacion, id_ccaa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, nombre);
            sentencia.setString(2, rama);
            sentencia.setString(3, division);
            sentencia.setString(4, localidad);
            if (fundacion == null) {
                sentencia.setNull(5, java.sql.Types.INTEGER);
            } else {
                sentencia.setInt(5, fundacion);
            }
            sentencia.setInt(6, idComunidad);
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }
        throw new SQLException("MySQL no devolvió el ID del equipo insertado.");
    }

    public static void actualizarEquipo(int idEquipo, String nombre, String rama, String division,
                                        String localidad, Integer fundacion, int idComunidad)
            throws SQLException {
        String sql = "UPDATE equipos SET nombre = ?, rama = ?, division = ?, localidad = ?, " +
                "fundacion = ?, id_ccaa = ? WHERE id_equipo = ?";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nombre);
            sentencia.setString(2, rama);
            sentencia.setString(3, division);
            sentencia.setString(4, localidad);
            if (fundacion == null) {
                sentencia.setNull(5, java.sql.Types.INTEGER);
            } else {
                sentencia.setInt(5, fundacion);
            }
            sentencia.setInt(6, idComunidad);
            sentencia.setInt(7, idEquipo);
            if (sentencia.executeUpdate() == 0 && !existeRegistro(
                    conexion, "equipos", "id_equipo", idEquipo)) {
                throw new SQLException("El equipo ya no existe en la base de datos.");
            }
        }
    }

    public static void eliminarEquipo(int idEquipo) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEquipo);
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("El equipo ya no existe en la base de datos.");
            }
        }
    }

    public static void insertarJugador(String nombre, String posicion, int numero, int edad,
                                       String nacionalidad, boolean capitan, int idEquipo)
            throws SQLException {
        String insertarJugador = "INSERT INTO jugadores " +
                "(nombre, posicion, numero, edad, nacionalidad, capitan) VALUES (?, ?, ?, ?, ?, ?)";
        String asignarEquipo = "INSERT INTO equipo_jugador (id_equipo, id_jugador) VALUES (?, ?)";
        try (Connection conexion = ConectionDB.conectar()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement sentenciaJugador = conexion.prepareStatement(
                    insertarJugador, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement sentenciaEquipo = conexion.prepareStatement(asignarEquipo)) {
                sentenciaJugador.setString(1, nombre);
                sentenciaJugador.setString(2, posicion);
                sentenciaJugador.setInt(3, numero);
                sentenciaJugador.setInt(4, edad);
                sentenciaJugador.setString(5, nacionalidad);
                sentenciaJugador.setBoolean(6, capitan);
                sentenciaJugador.executeUpdate();
                try (ResultSet claves = sentenciaJugador.getGeneratedKeys()) {
                    if (!claves.next()) {
                        throw new SQLException("MySQL no devolvió el ID del jugador insertado.");
                    }
                    sentenciaEquipo.setInt(1, idEquipo);
                    sentenciaEquipo.setInt(2, claves.getInt(1));
                    sentenciaEquipo.executeUpdate();
                }
                conexion.commit();
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            }
        }
    }

    public static void actualizarJugador(int idJugador, String nombre, String posicion, int numero,
                                         int edad, String nacionalidad, boolean capitan, int idEquipo)
            throws SQLException {
        String actualizar = "UPDATE jugadores SET nombre = ?, posicion = ?, numero = ?, edad = ?, " +
                "nacionalidad = ?, capitan = ? WHERE id_jugador = ?";
        String quitarEquipo = "DELETE FROM equipo_jugador WHERE id_jugador = ?";
        String asignarEquipo = "INSERT INTO equipo_jugador (id_equipo, id_jugador) VALUES (?, ?)";
        try (Connection conexion = ConectionDB.conectar()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement sentencia = conexion.prepareStatement(actualizar);
                 PreparedStatement quitar = conexion.prepareStatement(quitarEquipo);
                 PreparedStatement asignar = conexion.prepareStatement(asignarEquipo)) {
                sentencia.setString(1, nombre);
                sentencia.setString(2, posicion);
                sentencia.setInt(3, numero);
                sentencia.setInt(4, edad);
                sentencia.setString(5, nacionalidad);
                sentencia.setBoolean(6, capitan);
                sentencia.setInt(7, idJugador);
                if (sentencia.executeUpdate() == 0 && !existeRegistro(
                        conexion, "jugadores", "id_jugador", idJugador)) {
                    throw new SQLException("El jugador ya no existe en la base de datos.");
                }
                quitar.setInt(1, idJugador);
                quitar.executeUpdate();
                asignar.setInt(1, idEquipo);
                asignar.setInt(2, idJugador);
                asignar.executeUpdate();
                conexion.commit();
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            }
        }
    }

    public static void eliminarJugador(int idJugador) throws SQLException {
        String sql = "DELETE FROM jugadores WHERE id_jugador = ?";
        try (Connection conexion = ConectionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idJugador);
            if (sentencia.executeUpdate() == 0) {
                throw new SQLException("El jugador ya no existe en la base de datos.");
            }
        }
    }

    private static boolean existeRegistro(Connection conexion, String tabla, String columna, int id)
            throws SQLException {
        String sql = "SELECT 1 FROM " + tabla + " WHERE " + columna + " = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }
}
