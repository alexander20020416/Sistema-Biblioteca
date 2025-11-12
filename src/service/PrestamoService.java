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
            boolean usuarioExiste = (usuario != null);
            boolean usuarioEstaActivo = usuarioExiste && usuario.isActivo();
            
            if (!usuarioExiste) {
                throw new PrestamoException("Usuario no encontrado");
            }
            if (!usuarioEstaActivo) {
                throw new PrestamoException("Usuario inactivo");
            }
            
            // Validar libro
            Libro libro = libroDAO.buscar(libroIsbn);
            boolean libroExiste = (libro != null);
            boolean libroEstaDisponible = libroExiste && libro.isDisponible();
            
            if (!libroExiste) {
                throw new PrestamoException("Libro no encontrado");
            }
            if (!libroEstaDisponible) {
                throw new PrestamoException("Libro no disponible");
            }
            
            // Crear préstamo
            String fechaActual = CalculadoraFechas.obtenerFechaActual();
            String fechaDevolucionEsperada = CalculadoraFechas.agregarDias(fechaActual, diasPrestamo);
            
            Prestamo prestamo = new Prestamo();
            prestamo.setUsuarioId(usuarioId);
            prestamo.setLibroIsbn(libroIsbn);
            prestamo.setFechaPrestamo(fechaActual);
            prestamo.setFechaDevolucionEsperada(fechaDevolucionEsperada);
            prestamo.setEstado("ACTIVO");
            prestamo.setEmpleadoId(empleadoId);
            
            int prestamoId = prestamoDAO.insertar(prestamo);
            
            // Actualizar disponibilidad del libro
            int nuevoContadorPrestamos = libro.getVecesPrestado() + 1;
            libro.setDisponible(false);
            libro.setVecesPrestado(nuevoContadorPrestamos);
            libroDAO.actualizar(libro);
            
            // Actualizar contador de préstamos del usuario
            int nuevoTotalPrestamosUsuario = usuario.getTotalPrestamos() + 1;
            usuario.setTotalPrestamos(nuevoTotalPrestamosUsuario);
            usuarioDAO.actualizar(usuario);
            
            // Crear notificación
            String tituloLibro = libro.getTitulo();
            String mensajeNotificacion = "Préstamo realizado: " + tituloLibro;
            
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuarioId);
            notificacion.setTipo("PRESTAMO");
            notificacion.setMensaje(mensajeNotificacion);
            notificacion.setFecha(fechaActual);
            notificacion.setLeida(false);
            notificacionDAO.insertar(notificacion);
            
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
