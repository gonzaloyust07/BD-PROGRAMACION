package datas;

public class Estadistica {
    int puntos;
    int bloqueos;
    int faltas;
    int partidosJugados;

    // Constructor
    public Estadistica(int puntos, int bloqueos, int faltas, int partidosJugados) {
        this.puntos = puntos;
        this.bloqueos = bloqueos;
        this.faltas = faltas;
        this.partidosJugados = partidosJugados;
    }

    // Getters y Setters
    public int getPuntos() {
        return this.puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getBloqueos() {
        return this.bloqueos;
    }
    public void setBloqueos(int bloqueos) {
        this.bloqueos = bloqueos;
    }

    public int getFaltas() {
        return this.faltas;
    }
    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public int getPartidosJugados() {
        return this.partidosJugados;
    }
    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    @Override
    public String toString() {
        return "📊 Estadísticas -> Partidos: " + this.partidosJugados + "[Puntos: " + this.puntos + " | Bloqueos: " + this.bloqueos + " | Faltas: " + this.faltas + "]";
    }
}
