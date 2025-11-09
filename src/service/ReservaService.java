package service;

import model.*;
import dao.*;
import exception.*;
import util.*;
import java.util.List;

public class ReservaService {
    
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
            Usuario u = usuarioDAO.buscar(usuarioId);
            if (u == null || !u.isActivo()) {
                throw new BibliotecaException("Usuario inválido");
            }
            
            Libro l = libroDAO.buscar(libroIsbn);
            if (l == null) {
                throw new BibliotecaException("Libro no encontrado");
            }
            
            String fechaHoy = CalculadoraFechas.obtenerFechaActual();
            String fechaExpiracion = CalculadoraFechas.agregarDias(fechaHoy, 3);
            
            Reserva r = new Reserva();
            r.setUsuarioId(usuarioId);
            r.setLibroIsbn(libroIsbn);
            r.setFechaReserva(fechaHoy);
            r.setFechaExpiracion(fechaExpiracion);
            r.setEstado("ACTIVA");
            
            return reservaDAO.insertar(r);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    public boolean cancelarReserva(int reservaId) throws BibliotecaException {
        try {
            List<Reserva> reservas = reservaDAO.obtenerActivas();
            for (Reserva r : reservas) {
                if (r.getId() == reservaId) {
                    r.setEstado("CANCELADA");
                    return reservaDAO.actualizar(r);
                }
            }
            return false;
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    public void expirarReservas() throws BibliotecaException {
        try {
            String fechaHoy = CalculadoraFechas.obtenerFechaActual();
            List<Reserva> activas = reservaDAO.obtenerActivas();
            
            for (Reserva r : activas) {
                if (CalculadoraFechas.esFechaPasada(r.getFechaExpiracion())) {
                    r.setEstado("EXPIRADA");
                    reservaDAO.actualizar(r);
                }
            }
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
