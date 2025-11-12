package service;

import model.*;
import dao.*;
import exception.*;
import util.*;
import java.util.List;

public class ReservaService {
    
    private static final int DIAS_EXPIRACION_RESERVA = 3;
    
    private ReservaDAO reservaDAO;
    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;
    
    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.libroDAO = new LibroDAO();
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public int crearReserva(String usuarioId, String libroIsbn) throws BibliotecaException {
        try {
            Usuario usuario = usuarioDAO.buscar(usuarioId);
            if (usuario == null || !usuario.isActivo()) {
                throw new BibliotecaException("Usuario inválido");
            }
            
            Libro libro = libroDAO.buscar(libroIsbn);
            if (libro == null) {
                throw new BibliotecaException("Libro no encontrado");
            }
            
            String fechaActual = CalculadoraFechas.obtenerFechaActual();
            String fechaExpiracion = CalculadoraFechas.agregarDias(fechaActual, DIAS_EXPIRACION_RESERVA);
            
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setUsuarioId(usuarioId);
            nuevaReserva.setLibroIsbn(libroIsbn);
            nuevaReserva.setFechaReserva(fechaActual);
            nuevaReserva.setFechaExpiracion(fechaExpiracion);
            nuevaReserva.setEstado("ACTIVA");
            
            return reservaDAO.insertar(nuevaReserva);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al crear reserva", e);
        }
    }
    
    public boolean cancelarReserva(int reservaId) throws BibliotecaException {
        try {
            List<Reserva> reservasActivas = reservaDAO.obtenerActivas();
            for (Reserva reserva : reservasActivas) {
                if (reserva.getId() == reservaId) {
                    reserva.setEstado("CANCELADA");
                    return reservaDAO.actualizar(reserva);
                }
            }
            return false;
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al cancelar reserva", e);
        }
    }
    
    public void expirarReservas() throws BibliotecaException {
        try {
            String fechaActual = CalculadoraFechas.obtenerFechaActual();
            List<Reserva> reservasActivas = reservaDAO.obtenerActivas();
            
            for (Reserva reserva : reservasActivas) {
                if (CalculadoraFechas.esFechaPasada(reserva.getFechaExpiracion())) {
                    reserva.setEstado("EXPIRADA");
                    reservaDAO.actualizar(reserva);
                }
            }
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al expirar reservas", e);
        }
    }
}
