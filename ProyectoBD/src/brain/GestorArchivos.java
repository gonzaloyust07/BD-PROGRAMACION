package brain;

import datas.Equipo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    public List<Equipo> cargarClubesBinario(String nombreArchivo) {
        File fichero = new File(nombreArchivo);

        if (!fichero.exists()) {
            return new ArrayList<>(); // Devuelve lista vacía si el archivo no existe aún
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
            return (List<Equipo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Error al restaurar datos desde " + nombreArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 2. Método de Guardado Adaptado (Recibe la lista y la constante del archivo)
    public void guardarTodosClubesBinario(List<Equipo> listaClubes, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(listaClubes);
            // El mensaje de éxito ya lo imprime tu Main según tu captura, así que aquí solo procesamos el volcado masivo
        } catch (IOException e) {
            System.out.println("⚠️ Fallo en el volcado binario automático en " + nombreArchivo + ": " + e.getMessage());
        }
    }
}
