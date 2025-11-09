package dao;

import model.Multa;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO {
    
    public int insertar(Multa m) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO multas (prestamo_id, usuario_id, monto, razon, fecha, pagada, fecha_pago) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, m.getPrestamoId());
            ps.setString(2, m.getUsuarioId());
            ps.setDouble(3, m.getMonto());
            ps.setString(4, m.getRazon());
            ps.setString(5, m.getFecha());
            ps.setInt(6, m.isPagada() ? 1 : 0);
            ps.setString(7, m.getFechaPago());
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
    
    public List<Multa> obtenerPorUsuario(String usuarioId) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Multa> lista = new ArrayList<Multa>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM multas WHERE usuario_id = ?");
            ps.setString(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Multa m = new Multa();
                m.setId(rs.getInt("id"));
                m.setPrestamoId(rs.getInt("prestamo_id"));
                m.setUsuarioId(rs.getString("usuario_id"));
                m.setMonto(rs.getDouble("monto"));
                m.setRazon(rs.getString("razon"));
                m.setFecha(rs.getString("fecha"));
                m.setPagada(rs.getInt("pagada") == 1);
                m.setFechaPago(rs.getString("fecha_pago"));
                lista.add(m);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Multa m) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE multas SET pagada=?, fecha_pago=? WHERE id=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setInt(1, m.isPagada() ? 1 : 0);
            ps.setString(2, m.getFechaPago());
            ps.setInt(3, m.getId());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
