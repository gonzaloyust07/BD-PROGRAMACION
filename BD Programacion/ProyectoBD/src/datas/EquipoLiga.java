package datas;

public class EquipoLiga {
    private final int id;
    private final String nombre;
    private final String rama;
    private final String division;
    private final String localidad;
    private final String comunidad;
    private final Integer fundacion;

    public EquipoLiga(int id, String nombre, String rama, String division,
                      String localidad, String comunidad, Integer fundacion) {
        this.id = id;
        this.nombre = nombre;
        this.rama = rama;
        this.division = division;
        this.localidad = localidad;
        this.comunidad = comunidad;
        this.fundacion = fundacion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRama() { return rama; }
    public String getDivision() { return division; }
    public String getLocalidad() { return localidad; }
    public String getComunidad() { return comunidad; }
    public Integer getFundacion() { return fundacion; }

    public Object[] aFila() {
        return new Object[]{id, nombre, rama, division, localidad, comunidad,
                fundacion == null ? "" : fundacion};
    }

    @Override
    public String toString() {
        return nombre;
    }
}
