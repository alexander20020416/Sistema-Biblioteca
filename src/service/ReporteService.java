package service;

import model.*;
import dao.*;
import exception.*;
import util.*;
import java.util.List;

public class ReporteService {
    
    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;
    private PrestamoDAO prestamoDAO;
    private MultaDAO multaDAO;
    
    public ReporteService() {
        this.libroDAO = new LibroDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.multaDAO = new MultaDAO();
    }
    
    // Code smell: método muy largo con múltiples responsabilidades
    public String generarReporteGeneral() throws BibliotecaException {
        try {
            StringBuilder reporte = new StringBuilder();
            reporte.append("=== REPORTE GENERAL DE BIBLIOTECA ===\n");
            reporte.append("Fecha: " + CalculadoraFechas.obtenerFechaActual() + "\n\n");
            
            // Contar libros
            List<Libro> libros = libroDAO.obtenerTodos();
            int totalLibros = libros.size();
            int librosDisponibles = 0;
            int librosPrestados = 0;
            
            for (Libro l : libros) {
                if (l.isDisponible()) {
                    librosDisponibles++;
                } else {
                    librosPrestados++;
                }
            }
            
            reporte.append("Total de libros: " + totalLibros + "\n");
            reporte.append("Libros disponibles: " + librosDisponibles + "\n");
            reporte.append("Libros prestados: " + librosPrestados + "\n\n");
            
            // Contar usuarios
            List<Usuario> usuarios = usuarioDAO.obtenerTodos();
            int totalUsuarios = usuarios.size();
            int usuariosActivos = 0;
            
            for (Usuario u : usuarios) {
                if (u.isActivo()) {
                    usuariosActivos++;
                }
            }
            
            reporte.append("Total de usuarios: " + totalUsuarios + "\n");
            reporte.append("Usuarios activos: " + usuariosActivos + "\n\n");
            
            // Préstamos activos
            List<Prestamo> prestamos = prestamoDAO.obtenerActivos();
            reporte.append("Préstamos activos: " + prestamos.size() + "\n\n");
            
            return reporte.toString();
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    public boolean exportarReporte(String contenido, String nombreArchivo) throws BibliotecaException {
        return ExportadorReportes.exportarTexto(contenido, nombreArchivo);
    }
    
    public String generarReporteLibrosMasPrestados() throws BibliotecaException {
        try {
            List<Libro> libros = libroDAO.obtenerTodos();
            StringBuilder reporte = new StringBuilder();
            reporte.append("=== LIBROS MÁS PRESTADOS ===\n\n");
            
            // Code smell: algoritmo ineficiente (bubble sort casero)
            for (int i = 0; i < libros.size(); i++) {
                for (int j = 0; j < libros.size() - 1; j++) {
                    if (libros.get(j).getVecesPrestado() < libros.get(j + 1).getVecesPrestado()) {
                        Libro temp = libros.get(j);
                        libros.set(j, libros.get(j + 1));
                        libros.set(j + 1, temp);
                    }
                }
            }
            
            int contador = 0;
            for (Libro l : libros) {
                if (contador < 10) {
                    reporte.append((contador + 1) + ". " + l.getTitulo() + " - Veces prestado: " + l.getVecesPrestado() + "\n");
                    contador++;
                }
            }
            
            return reporte.toString();
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
