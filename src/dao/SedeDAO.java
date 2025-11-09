package dao;

import model.Sede;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO {
    
    public int insertar(Sede s) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String sql = "INSERT INTO sedes (nombre, direccion, telefono, horario, capacidad) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            ps.setString(3, s.getTelefono());
            ps.setString(4, s.getHorario());
            ps.setInt(5, s.getCapacidad());
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
    
    public Sede buscar(int id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM sedes WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Sede s = null;
            if (rs.next()) {
                s = new Sede();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setDireccion(rs.getString("direccion"));
                s.setTelefono(rs.getString("telefono"));
                s.setHorario(rs.getString("horario"));
                s.setCapacidad(rs.getInt("capacidad"));
            }
            rs.close();
            ps.close();
            return s;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Sede> obtenerTodas() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Sede> lista = new ArrayList<Sede>();
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM sedes");
            while (rs.next()) {
                Sede s = new Sede();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setDireccion(rs.getString("direccion"));
                s.setTelefono(rs.getString("telefono"));
                s.setHorario(rs.getString("horario"));
                s.setCapacidad(rs.getInt("capacidad"));
                lista.add(s);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
}
