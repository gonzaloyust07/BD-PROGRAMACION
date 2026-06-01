package brain;

import datas.Equipo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    @SuppressWarnings("unchecked")
    public List<Equipo> cargarClubesBinario(String nombreArchivo) {
        File fichero = new File(nombreArchivo);

        if (!fichero.exists()) {
            return new ArrayList<>(); // Devuelve lista vacía si el archivo no existe aún
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
            return (List<Equipo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error al restaurar datos desde '" + nombreArchivo + "':");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void guardarTodosClubesBinario(List<Equipo> listaClubes, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(listaClubes);
        } catch (IOException e) {
            System.out.println("⚠️ Fallo en el volcado binario automático en " + nombreArchivo + ":");
            e.printStackTrace();
        }
    }
}
