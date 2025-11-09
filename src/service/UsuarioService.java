package service;

import model.*;
import dao.*;
import exception.*;
import util.*;

public class UsuarioService {
    
    private UsuarioDAO usuarioDAO;
    private NotificacionDAO notificacionDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.notificacionDAO = new NotificacionDAO();
    }
    
    public boolean registrarUsuario(String nombre, String email, String telefono, String direccion, String tipo) throws UsuarioException {
        try {
            // Validaciones
            if (!ValidadorDatos.validarNombre(nombre)) {
                throw new UsuarioException("Nombre inválido");
            }
            if (!ValidadorDatos.validarEmail(email)) {
                throw new UsuarioException("Email inválido");
            }
            if (!ValidadorDatos.validarTelefono(telefono)) {
                throw new UsuarioException("Teléfono inválido");
            }
            
            Usuario usuario = new Usuario();
            usuario.setId(GeneradorCodigos.generarCodigoUsuario());
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setTipo(tipo);
            usuario.setFechaRegistro(CalculadoraFechas.obtenerFechaActual());
            usuario.setActivo(true);
            usuario.setTotalPrestamos(0);
            
            boolean resultado = usuarioDAO.insertar(usuario);
            
            if (resultado) {
                Notificacion notif = new Notificacion();
                notif.setUsuarioId(usuario.getId());
                notif.setTipo("BIENVENIDA");
                notif.setMensaje("Bienvenido a la biblioteca");
                notif.setFecha(CalculadoraFechas.obtenerFechaActual());
                notif.setLeida(false);
                notificacionDAO.insertar(notif);
            }
            
            return resultado;
        } catch (DatabaseException e) {
            throw new UsuarioException("Error", e);
        } catch (BibliotecaException e) {
            throw new UsuarioException("Error", e);
        }
    }
    
    public Usuario buscarUsuario(String id) throws UsuarioException {
        try {
            return usuarioDAO.buscar(id);
        } catch (DatabaseException e) {
            throw new UsuarioException("Error", e);
        }
    }
    
    public boolean desactivarUsuario(String id) throws UsuarioException {
        try {
            Usuario u = usuarioDAO.buscar(id);
            if (u == null) {
                throw new UsuarioException("Usuario no encontrado");
            }
            u.setActivo(false);
            return usuarioDAO.actualizar(u);
        } catch (DatabaseException e) {
            throw new UsuarioException("Error", e);
        }
    }
}
