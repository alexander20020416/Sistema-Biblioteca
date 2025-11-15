package service;

import model.Usuario;
import model.Notificacion;
import dao.UsuarioDAO;
import dao.NotificacionDAO;
import exception.UsuarioException;
import exception.DatabaseException;
import exception.BibliotecaException;
import util.ValidadorDatos;
import util.GeneradorCodigos;
import util.CalculadoraFechas;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private NotificacionDAO notificacionDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.notificacionDAO = new NotificacionDAO();
    }

    public boolean registrarUsuario(String nombre, String email, String telefono,
                                    String direccion, String tipo) throws UsuarioException {

        try {
            validarDatos(nombre, email, telefono);

            Usuario usuario = construirUsuario(nombre, email, telefono, direccion, tipo);

            boolean resultado = usuarioDAO.insertar(usuario);

            if (resultado) {
                registrarNotificacionBienvenida(usuario);
            }

            return resultado;

        } catch (BibliotecaException e) {
            throw new UsuarioException("Error", e);
        }
    }

    // ============================================================
    // MÉTODOS EXTRAÍDOS (EXTRACT METHOD)
    // ============================================================

    private void validarDatos(String nombre, String email, String telefono) throws UsuarioException {

        if (!ValidadorDatos.validarNombre(nombre)) {
            throw new UsuarioException("Nombre inválido");
        }
        if (!ValidadorDatos.validarEmail(email)) {
            throw new UsuarioException("Email inválido");
        }
        if (!ValidadorDatos.validarTelefono(telefono)) {
            throw new UsuarioException("Teléfono inválido");
        }
    }

    private Usuario construirUsuario(String nombre, String email, String telefono,
                                     String direccion, String tipo) {

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

        return usuario;
    }

    private void registrarNotificacionBienvenida(Usuario usuario) throws DatabaseException {
        Notificacion notif = new Notificacion();
        notif.setUsuarioId(usuario.getId());
        notif.setTipo("BIENVENIDA");
        notif.setMensaje("Bienvenido a la biblioteca");
        notif.setFecha(CalculadoraFechas.obtenerFechaActual());
        notif.setLeida(false);

        notificacionDAO.insertar(notif);
    }

    // ============================================================
    // RESTO DE MÉTODOS ORIGINALES
    // ============================================================

    public Usuario buscarUsuario(String id) throws UsuarioException {
        try {
            return usuarioDAO.buscar(id);
        } catch (DatabaseException e) {
            throw new UsuarioException("Error", e);
        }
    }

    public boolean desactivarUsuario(String id) throws UsuarioException {
        try {
            Usuario usuario = usuarioDAO.buscar(id);

            if (usuario == null) {
                throw new UsuarioException("Usuario no encontrado");
            }

            usuario.setActivo(false);
            return usuarioDAO.actualizar(usuario);

        } catch (DatabaseException e) {
            throw new UsuarioException("Error", e);
        }
    }
}
