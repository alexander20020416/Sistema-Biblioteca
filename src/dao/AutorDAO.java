package dao;

import model.Autor;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    
    public int insertar(Autor a) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String sql = "INSERT INTO autores (nombre, nacionalidad, biografia, anio_nacimiento) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getNacionalidad());
            ps.setString(3, a.getBiografia());
            ps.setInt(4, a.getAnioNacimiento());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            rs.close();
            ps.close();
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Error al insertar autor", e);
        }
    }
    
    public Autor buscar(int id) throws DatabaseException {
        Connection con = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM autores WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Autor a = null;
            if (rs.next()) {
                a = new Autor();
                a.setId(rs.getInt("id"));
                a.setNombre(rs.getString("nombre"));
                a.setNacionalidad(rs.getString("nacionalidad"));
                a.setBiografia(rs.getString("biografia"));
                a.setAnioNacimiento(rs.getInt("anio_nacimiento"));
            }
            rs.close();
            ps.close();
            return a;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Autor> obtenerTodos() throws DatabaseException {
        Connection con = DatabaseConnection.obtenerConexion();
        List<Autor> lista = new ArrayList<Autor>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM autores");
            while (rs.next()) {
                Autor a = new Autor();
                a.setId(rs.getInt("id"));
                a.setNombre(rs.getString("nombre"));
                a.setNacionalidad(rs.getString("nacionalidad"));
                a.setBiografia(rs.getString("biografia"));
                a.setAnioNacimiento(rs.getInt("anio_nacimiento"));
                lista.add(a);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
}
