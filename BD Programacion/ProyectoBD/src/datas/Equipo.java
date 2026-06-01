package datas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Equipo implements Serializable {
    // 1. CORRECCIÓN OBLIGATORIA: Añadir el ID de versión serial
    private static final long serialVersionUID = 1L;

    // Nota: El 'id' estático no se guardará en el archivo.
    // Al reiniciar el programa volverá a valer 1.
    private static int id = 1;

    String nombre;
    double presupuesto;
    int idEquipo;

    Entrenador entrenador;

    private List<Jugador> plantilla = new ArrayList<>();
    private Set<Integer> inscritos = new HashSet<>();

    // Constructor
    public Equipo(String nombre, double presupuesto) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.idEquipo = id++;
    }

    // Getters y Setters
    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPresupuesto() { return this.presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public List<Jugador> getPlantilla() { return this.plantilla; }
    public Set<Integer> getInscritos() { return this.inscritos; }

    public void setInscritos(Set<Integer> inscritos) { this.inscritos = inscritos; }
    public int getIdEquipo() { return this.idEquipo; }

    public Entrenador getEntrenador() { return this.entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    public void agregarJugador(Jugador j) {
        this.plantilla.add(j);
        this.inscritos.add(j.getIdJugador());
    }

    public Jugador getJugadorPorId(int idJugador) {
        for (Jugador j : plantilla) {
            if (j.getIdJugador() == idJugador) return j;
        }
        return null;
    }

    public boolean eliminarJugadorPorId(int idJugador) {
        Jugador encontrado = getJugadorPorId(idJugador);
        if (encontrado != null) {
            plantilla.remove(encontrado);
            inscritos.remove(encontrado.getIdJugador());
            return true;
        }
        return false;
    }

    public boolean tieneMinimoJugadores() {
        return this.plantilla.size() >= 7;
    }
}
