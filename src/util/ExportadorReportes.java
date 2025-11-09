package util;

import java.io.FileWriter;
import java.io.IOException;

public class ExportadorReportes {
    
    public static boolean exportarTexto(String contenido, String archivo) {
        try {
            FileWriter writer = new FileWriter(archivo);
            writer.write(contenido);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String generarNombreArchivo(String tipo) {
        return tipo + "_" + CalculadoraFechas.obtenerFechaActual() + ".txt";
    }
}
