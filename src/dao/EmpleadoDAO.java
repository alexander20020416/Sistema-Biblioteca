package dao;

import model.Empleado;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    
    public boolean insertar(Empleado e) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO empleados (id, nombre, cargo, email, telefono, fecha_contratacion, salario, sede_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, e.getId());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getCargo());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getTelefono());
            ps.setString(6, e.getFechaContratacion());
            ps.setDouble(7, e.getSalario());
            ps.setInt(8, e.getSedeId());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException ex) {
            throw new DatabaseException("Error", ex);
        }
    }
    
    public Empleado buscar(String id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM empleados WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            Empleado e = null;
            if (rs.next()) {
                e = new Empleado();
                e.setId(rs.getString("id"));
                e.setNombre(rs.getString("nombre"));
                e.setCargo(rs.getString("cargo"));
                e.setEmail(rs.getString("email"));
                e.setTelefono(rs.getString("telefono"));
                e.setFechaContratacion(rs.getString("fecha_contratacion"));
                e.setSalario(rs.getDouble("salario"));
                e.setSedeId(rs.getInt("sede_id"));
            }
            rs.close();
            ps.close();
            return e;
        } catch (SQLException ex) {
            throw new DatabaseException("Error", ex);
        }
    }
    
    public List<Empleado> obtenerTodos() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Empleado> lista = new ArrayList<Empleado>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM empleados");
            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId(rs.getString("id"));
                e.setNombre(rs.getString("nombre"));
                e.setCargo(rs.getString("cargo"));
                e.setEmail(rs.getString("email"));
                e.setTelefono(rs.getString("telefono"));
                e.setFechaContratacion(rs.getString("fecha_contratacion"));
                e.setSalario(rs.getDouble("salario"));
                e.setSedeId(rs.getInt("sede_id"));
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
