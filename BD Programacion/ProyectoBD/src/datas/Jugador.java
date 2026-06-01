package datas;

import java.io.Serializable;

public class Jugador implements Serializable {
    private static int id = 1;

    int idJugador;           // id interno del sistema
    String nombreCompleto;   // nombre completo
    String nombreCamiseta;   // nombre que aparece en la camiseta (alias)
    int numero;              // dorsal
    int edad;
    String nacionalidad;
    String posicion;
    boolean lesionado = false;
    double precio = 0.0;     // precio de mercado o traspaso

    public Jugador(String nombreCompleto, String nombreCamiseta, int numero, int edad, String nacionalidad, String posicion, double precio) {
        this.idJugador = id++;
        this.nombreCompleto = nombreCompleto;
        this.nombreCamiseta = nombreCamiseta;
        this.numero = numero;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.posicion = posicion;
        this.precio = precio;
        this.lesionado = false;
    }

    // Getters y setters relevantes
    public int getIdJugador() {
        return idJugador;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCamiseta() {
        return nombreCamiseta;
    }

    public void setNombreCamiseta(String nombreCamiseta) {
        this.nombreCamiseta = nombreCamiseta;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isLesionado() {
        return lesionado;
    }

    public void setLesionado(boolean lesionado) {
        this.lesionado = lesionado;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getPosicion() {
        return posicion;
    }
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    // Información corta (para listados compactos)
    public String infoCorta() {
        return ("Numero: " + this.numero + "| Nombre: " + this.nombreCamiseta + " | Precio: " + this.precio + "€");
    }

    // Información larga (ficha completa)
    @Override
    public String toString() {
        return "\n=======================================" +
                "\n             FICHA TÉCNICA             " +
                "\n=======================================" +
                "\n  • ID Sistema      :\t" + this.idJugador +
                "\n  • Nombre completo :\t" + this.nombreCompleto +
                "\n  • Nombre camiseta :\t" + this.nombreCamiseta +
                "\n  • Edad            :\t" + this.edad + " años" +
                "\n  • Dorsal (#)      :\t" + this.numero +
                "\n  • Lesionado       :\t" + (this.lesionado ? "Sí" : "No") +
                "\n  • Nacionalidad    :\t" + this.nacionalidad +
                "\n  • Posición        :\t" + this.posicion +
                "\n  • Precio          :\t" + this.precio + " €" +
                "\n=======================================";
    }
}
