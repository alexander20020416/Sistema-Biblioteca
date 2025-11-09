package service;

import model.*;
import dao.*;
import exception.*;

public class NotificacionService {
    
    private NotificacionDAO notificacionDAO;
    
    public NotificacionService() {
        this.notificacionDAO = new NotificacionDAO();
    }
    
    public int enviarNotificacion(String usuarioId, String tipo, String mensaje) throws BibliotecaException {
        try {
            Notificacion n = new Notificacion();
            n.setUsuarioId(usuarioId);
            n.setTipo(tipo);
            n.setMensaje(mensaje);
            n.setFecha(util.CalculadoraFechas.obtenerFechaActual());
            n.setLeida(false);
            
            return notificacionDAO.insertar(n);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al enviar notificación", e);
        } catch (BibliotecaException e) {
            throw e;
        }
    }
    
    public boolean marcarComoLeida(int notificacionId) throws BibliotecaException {
        try {
            return notificacionDAO.marcarComoLeida(notificacionId);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    // Code smell: código duplicado con lógica similar a otros servicios
    public int contarNoLeidas(String usuarioId) throws BibliotecaException {
        try {
            int contador = 0;
            for (Notificacion n : notificacionDAO.obtenerPorUsuario(usuarioId)) {
                if (!n.isLeida()) {
                    contador++;
                }
            }
            return contador;
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
