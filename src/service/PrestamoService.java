package service;

import model.*;
import dao.*;
import exception.*;
import util.*;

public class PrestamoService {
    
    private PrestamoDAO prestamoDAO;
    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;
    private NotificacionDAO notificacionDAO;
    
    public PrestamoService() {
        this.prestamoDAO = new PrestamoDAO();
        this.libroDAO = new LibroDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.notificacionDAO = new NotificacionDAO();
    }
    
    // Code smell: método largo con lógica compleja
    public int realizarPrestamo(String usuarioId, String libroIsbn, String empleadoId, int diasPrestamo) throws PrestamoException {
        try {
            // Validar usuario
            Usuario usuario = usuarioDAO.buscar(usuarioId);
            if (usuario == null) {
                throw new PrestamoException("Usuario no encontrado");
            }
            if (!usuario.isActivo()) {
                throw new PrestamoException("Usuario inactivo");
            }
            
            // Validar libro
            Libro libro = libroDAO.buscar(libroIsbn);
            if (libro == null) {
                throw new PrestamoException("Libro no encontrado");
            }
            if (!libro.isDisponible()) {
                throw new PrestamoException("Libro no disponible");
            }
            
            // Crear préstamo
            String fechaHoy = CalculadoraFechas.obtenerFechaActual();
            String fechaDevolucion = CalculadoraFechas.agregarDias(fechaHoy, diasPrestamo);
            
            Prestamo prestamo = new Prestamo();
            prestamo.setUsuarioId(usuarioId);
            prestamo.setLibroIsbn(libroIsbn);
            prestamo.setFechaPrestamo(fechaHoy);
            prestamo.setFechaDevolucionEsperada(fechaDevolucion);
            prestamo.setEstado("ACTIVO");
            prestamo.setEmpleadoId(empleadoId);
            
            int prestamoId = prestamoDAO.insertar(prestamo);
            
            // Actualizar disponibilidad del libro
            libro.setDisponible(false);
            libro.setVecesPrestado(libro.getVecesPrestado() + 1);
            libroDAO.actualizar(libro);
            
            // Actualizar contador de préstamos del usuario
            usuario.setTotalPrestamos(usuario.getTotalPrestamos() + 1);
            usuarioDAO.actualizar(usuario);
            
            // Crear notificación
            Notificacion notif = new Notificacion();
            notif.setUsuarioId(usuarioId);
            notif.setTipo("PRESTAMO");
            notif.setMensaje("Préstamo realizado: " + libro.getTitulo());
            notif.setFecha(fechaHoy);
            notif.setLeida(false);
            notificacionDAO.insertar(notif);
            
            return prestamoId;
        } catch (DatabaseException e) {
            throw new PrestamoException("Error al realizar préstamo", e);
        } catch (BibliotecaException e) {
            throw new PrestamoException("Error", e);
        }
    }
    
    public boolean devolverPrestamo(int prestamoId) throws PrestamoException {
        try {
            Prestamo prestamo = prestamoDAO.buscar(prestamoId);
            if (prestamo == null) {
                throw new PrestamoException("Préstamo no encontrado");
            }
            
            String fechaHoy = CalculadoraFechas.obtenerFechaActual();
            prestamo.setFechaDevolucionReal(fechaHoy);
            prestamo.setEstado("DEVUELTO");
            
            prestamoDAO.actualizar(prestamo);
            
            Libro libro = libroDAO.buscar(prestamo.getLibroIsbn());
            libro.setDisponible(true);
            libroDAO.actualizar(libro);
            
            return true;
        } catch (DatabaseException e) {
            throw new PrestamoException("Error", e);
        } catch (BibliotecaException e) {
            throw new PrestamoException("Error", e);
        }
    }
}
