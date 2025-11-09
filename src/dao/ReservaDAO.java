package dao;

import model.Reserva;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    
    public int insertar(Reserva r) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO reservas (usuario_id, libro_isbn, fecha_reserva, fecha_expiracion, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, r.getUsuarioId());
            ps.setString(2, r.getLibroIsbn());
            ps.setString(3, r.getFechaReserva());
            ps.setString(4, r.getFechaExpiracion());
            ps.setString(5, r.getEstado());
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
    
    public List<Reserva> obtenerActivas() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Reserva> lista = new ArrayList<Reserva>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM reservas WHERE estado = 'ACTIVA'");
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setUsuarioId(rs.getString("usuario_id"));
                r.setLibroIsbn(rs.getString("libro_isbn"));
                r.setFechaReserva(rs.getString("fecha_reserva"));
                r.setFechaExpiracion(rs.getString("fecha_expiracion"));
                r.setEstado(rs.getString("estado"));
                lista.add(r);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Reserva r) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE reservas SET estado=? WHERE id=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, r.getEstado());
            ps.setInt(2, r.getId());
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
