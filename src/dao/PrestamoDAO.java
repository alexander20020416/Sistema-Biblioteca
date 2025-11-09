package dao;

import model.Prestamo;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    
    public int insertar(Prestamo p) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO prestamos (usuario_id, libro_isbn, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado, empleado_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getUsuarioId());
            ps.setString(2, p.getLibroIsbn());
            ps.setString(3, p.getFechaPrestamo());
            ps.setString(4, p.getFechaDevolucionEsperada());
            ps.setString(5, p.getFechaDevolucionReal());
            ps.setString(6, p.getEstado());
            ps.setString(7, p.getEmpleadoId());
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
    
    public Prestamo buscar(int id) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM prestamos WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Prestamo p = null;
            if (rs.next()) {
                p = new Prestamo();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getString("usuario_id"));
                p.setLibroIsbn(rs.getString("libro_isbn"));
                p.setFechaPrestamo(rs.getString("fecha_prestamo"));
                p.setFechaDevolucionEsperada(rs.getString("fecha_devolucion_esperada"));
                p.setFechaDevolucionReal(rs.getString("fecha_devolucion_real"));
                p.setEstado(rs.getString("estado"));
                p.setEmpleadoId(rs.getString("empleado_id"));
            }
            rs.close();
            ps.close();
            return p;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Prestamo> obtenerActivos() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM prestamos WHERE estado = 'ACTIVO'");
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getString("usuario_id"));
                p.setLibroIsbn(rs.getString("libro_isbn"));
                p.setFechaPrestamo(rs.getString("fecha_prestamo"));
                p.setFechaDevolucionEsperada(rs.getString("fecha_devolucion_esperada"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Prestamo p) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE prestamos SET fecha_devolucion_real=?, estado=? WHERE id=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, p.getFechaDevolucionReal());
            ps.setString(2, p.getEstado());
            ps.setInt(3, p.getId());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
