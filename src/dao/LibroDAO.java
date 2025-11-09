package dao;

import model.Libro;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    
    // Code smell: método muy largo
    public boolean insertar(Libro l) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "INSERT INTO libros (isbn, titulo, autor_id, editorial_id, categoria_id, anio, paginas, disponible, veces_prestado, sede_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setInt(3, l.getAutorId());
            ps.setInt(4, l.getEditorialId());
            ps.setInt(5, l.getCategoriaId());
            ps.setInt(6, l.getAnio());
            ps.setInt(7, l.getPaginas());
            ps.setInt(8, l.isDisponible() ? 1 : 0);
            ps.setInt(9, l.getVecesPrestado());
            ps.setInt(10, l.getSedeId());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public Libro buscar(String isbn) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM libros WHERE isbn = ?");
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            Libro l = null;
            if (rs.next()) {
                l = new Libro();
                l.setIsbn(rs.getString("isbn"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutorId(rs.getInt("autor_id"));
                l.setEditorialId(rs.getInt("editorial_id"));
                l.setCategoriaId(rs.getInt("categoria_id"));
                l.setAnio(rs.getInt("anio"));
                l.setPaginas(rs.getInt("paginas"));
                l.setDisponible(rs.getInt("disponible") == 1);
                l.setVecesPrestado(rs.getInt("veces_prestado"));
                l.setSedeId(rs.getInt("sede_id"));
            }
            rs.close();
            ps.close();
            return l;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    // Code smell: código duplicado
    public List<Libro> obtenerTodos() throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        List<Libro> lista = new ArrayList<Libro>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM libros");
            while (rs.next()) {
                Libro l = new Libro();
                l.setIsbn(rs.getString("isbn"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutorId(rs.getInt("autor_id"));
                l.setEditorialId(rs.getInt("editorial_id"));
                l.setCategoriaId(rs.getInt("categoria_id"));
                l.setAnio(rs.getInt("anio"));
                l.setPaginas(rs.getInt("paginas"));
                l.setDisponible(rs.getInt("disponible") == 1);
                l.setVecesPrestado(rs.getInt("veces_prestado"));
                l.setSedeId(rs.getInt("sede_id"));
                lista.add(l);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
    
    public boolean actualizar(Libro l) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        String s = "UPDATE libros SET titulo=?, autor_id=?, editorial_id=?, categoria_id=?, anio=?, paginas=?, disponible=?, veces_prestado=?, sede_id=? WHERE isbn=?";
        try {
            PreparedStatement ps = c.prepareStatement(s);
            ps.setString(1, l.getTitulo());
            ps.setInt(2, l.getAutorId());
            ps.setInt(3, l.getEditorialId());
            ps.setInt(4, l.getCategoriaId());
            ps.setInt(5, l.getAnio());
            ps.setInt(6, l.getPaginas());
            ps.setInt(7, l.isDisponible() ? 1 : 0);
            ps.setInt(8, l.getVecesPrestado());
            ps.setInt(9, l.getSedeId());
            ps.setString(10, l.getIsbn());
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public boolean eliminar(String isbn) throws DatabaseException {
        Connection c = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM libros WHERE isbn = ?");
            ps.setString(1, isbn);
            int r = ps.executeUpdate();
            ps.close();
            return r > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
}
