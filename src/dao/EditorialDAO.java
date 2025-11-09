package dao;

import model.Editorial;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {
    
    public int insertar(Editorial e) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String sql = "INSERT INTO editoriales (nombre, pais, ciudad, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getPais());
            ps.setString(3, e.getCiudad());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getEmail());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            rs.close();
            ps.close();
            return id;
        } catch (SQLException ex) {
            throw new DatabaseException("Error", ex);
        }
    }
    
    public Editorial buscar(int id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM editoriales WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Editorial e = null;
            if (rs.next()) {
                e = new Editorial();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setPais(rs.getString("pais"));
                e.setCiudad(rs.getString("ciudad"));
                e.setTelefono(rs.getString("telefono"));
                e.setEmail(rs.getString("email"));
            }
            rs.close();
            ps.close();
            return e;
        } catch (SQLException ex) {
            throw new DatabaseException("Error", ex);
        }
    }
    
    public List<Editorial> obtenerTodas() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Editorial> lista = new ArrayList<Editorial>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM editoriales");
            while (rs.next()) {
                Editorial e = new Editorial();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setPais(rs.getString("pais"));
                e.setCiudad(rs.getString("ciudad"));
                e.setTelefono(rs.getString("telefono"));
                e.setEmail(rs.getString("email"));
                lista.add(e);
            }
            rs.close();
            s.close();
        } catch (SQLException ex) {
            throw new DatabaseException("Error", ex);
        }
        return lista;
    }
}
