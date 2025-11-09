package dao;

import model.Usuario;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public boolean insertar(Usuario u) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO usuarios (id, nombre, email, telefono, direccion, tipo, fecha_registro, activo, total_prestamos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, u.getId());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getDireccion());
            ps.setString(6, u.getTipo());
            ps.setString(7, u.getFechaRegistro());
            ps.setInt(8, u.isActivo() ? 1 : 0);
            ps.setInt(9, u.getTotalPrestamos());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public Usuario buscar(String id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            Usuario u = null;
            if (rs.next()) {
                u = new Usuario();
                u.setId(rs.getString("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setTelefono(rs.getString("telefono"));
                u.setDireccion(rs.getString("direccion"));
                u.setTipo(rs.getString("tipo"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setActivo(rs.getInt("activo") == 1);
                u.setTotalPrestamos(rs.getInt("total_prestamos"));
            }
            rs.close();
            ps.close();
            return u;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Usuario> obtenerTodos() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Usuario> lista = new ArrayList<Usuario>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM usuarios");
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getString("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setTelefono(rs.getString("telefono"));
                u.setDireccion(rs.getString("direccion"));
                u.setTipo(rs.getString("tipo"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setActivo(rs.getInt("activo") == 1);
                u.setTotalPrestamos(rs.getInt("total_prestamos"));
                lista.add(u);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Usuario u) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE usuarios SET nombre=?, email=?, telefono=?, direccion=?, tipo=?, activo=?, total_prestamos=? WHERE id=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getDireccion());
            ps.setString(5, u.getTipo());
            ps.setInt(6, u.isActivo() ? 1 : 0);
            ps.setInt(7, u.getTotalPrestamos());
            ps.setString(8, u.getId());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
