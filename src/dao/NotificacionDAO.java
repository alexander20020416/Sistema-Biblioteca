package dao;

import model.Notificacion;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {
    
    public int insertar(Notificacion n) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO notificaciones (usuario_id, tipo, mensaje, fecha, leida) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, n.getUsuarioId());
            ps.setString(2, n.getTipo());
            ps.setString(3, n.getMensaje());
            ps.setString(4, n.getFecha());
            ps.setInt(5, n.isLeida() ? 1 : 0);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            rs.close();
            ps.close();
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Notificacion> obtenerPorUsuario(String usuarioId) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Notificacion> lista = new ArrayList<Notificacion>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM notificaciones WHERE usuario_id = ? ORDER BY fecha DESC");
            ps.setString(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notificacion n = new Notificacion();
                n.setId(rs.getInt("id"));
                n.setUsuarioId(rs.getString("usuario_id"));
                n.setTipo(rs.getString("tipo"));
                n.setMensaje(rs.getString("mensaje"));
                n.setFecha(rs.getString("fecha"));
                n.setLeida(rs.getInt("leida") == 1);
                lista.add(n);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean marcarComoLeida(int id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE notificaciones SET leida=1 WHERE id=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setInt(1, id);
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
