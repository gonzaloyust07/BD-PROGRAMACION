package datas;

import java.io.Serializable;

public class Entrenador implements Serializable {
    private static int id = 1;

    String nombreCompleto;
    int edad;
    String nacionalidad;
    int idEntrenador;
    double precio = 0.0;

    public Entrenador(String nombreCompleto, int edad, String nacionalidad, double precio) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.idEntrenador = id++;
        this.precio = precio;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getIdEntrenador() {
        return idEntrenador;
    }
    public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Información corta
    public String infoCorta() {
        return "ID: " + this.idEntrenador + " | Nombre: " + this.nombreCompleto + " | Precio: " + this.precio + " €";
    }


    @Override
    public String toString() {
       return  "\n=======================================" +
               "\n             FICHA TÉCNICA             " +
               "\n=======================================" +
               "\n  • ID          :\t" + this.idEntrenador +
               "\n  • Nombre      :\t" + this.nombreCompleto +
               "\n  • Edad        :\t" + this.edad + " años" +
               "\n  • Nacionalidad:\t" + this.nacionalidad +
               "\n  • Precio      :\t" + this.precio +
               "\n=======================================\n";
    }

}


