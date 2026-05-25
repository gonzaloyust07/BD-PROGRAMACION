package datas;

import java.io.Serializable;

public class Jugador implements Serializable {
    private static int id = 1;

    private int idJugador;           // id interno del sistema
    private String nombreCompleto;   // nombre completo
    private String nombreCamiseta;   // nombre que aparece en la camiseta (alias)
    private int numero;              // dorsal
    private int edad;
    private boolean lesionado = false;
    private double precio = 0.0;     // precio de mercado o traspaso

    public Jugador(int numero, String nombreCompleto, String nombreCamiseta, int edad) {
        this.idJugador = id++;
        this.nombreCompleto = nombreCompleto;
        this.nombreCamiseta = nombreCamiseta;
        this.numero = numero;
        this.edad = edad;
        this.precio = precio;
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
                "\n  • Precio          :\t" + this.precio + " €" +
                "\n=======================================\n";
    }
}
