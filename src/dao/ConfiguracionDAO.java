package dao;

import model.Configuracion;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionDAO {
    
    public boolean insertar(Configuracion cfg) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO configuracion (clave, valor, descripcion) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, cfg.getClave());
            ps.setString(2, cfg.getValor());
            ps.setString(3, cfg.getDescripcion());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public Configuracion buscar(String clave) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM configuracion WHERE clave = ?");
            ps.setString(1, clave);
            ResultSet rs = ps.executeQuery();
            Configuracion cfg = null;
            if (rs.next()) {
                cfg = new Configuracion();
                cfg.setClave(rs.getString("clave"));
                cfg.setValor(rs.getString("valor"));
                cfg.setDescripcion(rs.getString("descripcion"));
            }
            rs.close();
            ps.close();
            return cfg;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Configuracion> obtenerTodas() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Configuracion> lista = new ArrayList<Configuracion>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM configuracion");
            while (rs.next()) {
                Configuracion cfg = new Configuracion();
                cfg.setClave(rs.getString("clave"));
                cfg.setValor(rs.getString("valor"));
                cfg.setDescripcion(rs.getString("descripcion"));
                lista.add(cfg);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Configuracion cfg) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE configuracion SET valor=?, descripcion=? WHERE clave=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, cfg.getValor());
            ps.setString(2, cfg.getDescripcion());
            ps.setString(3, cfg.getClave());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
