package datas;

public class JugadorLiga {
    private final int id;
    private final String nombre;
    private final String posicion;
    private final int numero;
    private final int edad;
    private final String nacionalidad;
    private final boolean capitan;
    private final int idEquipo;
    private final String equipo;

    public JugadorLiga(int id, String nombre, String posicion, int numero, int edad,
                       String nacionalidad, boolean capitan, int idEquipo, String equipo) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.numero = numero;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.capitan = capitan;
        this.idEquipo = idEquipo;
        this.equipo = equipo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPosicion() { return posicion; }
    public int getNumero() { return numero; }
    public int getEdad() { return edad; }
    public String getNacionalidad() { return nacionalidad; }
    public boolean isCapitan() { return capitan; }
    public int getIdEquipo() { return idEquipo; }
    public String getEquipo() { return equipo; }

    public Object[] aFila() {
        return new Object[]{id, nombre, posicion, numero, edad, nacionalidad, capitan, equipo, idEquipo};
    }
}
