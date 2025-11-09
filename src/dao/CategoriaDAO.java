package dao;

import model.Categoria;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    
    public int insertar(Categoria cat) throws DatabaseException {
        Connection conn = DatabaseConnection.obtenerConexion();
        String query = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, cat.getNombre());
            pstmt.setString(2, cat.getDescripcion());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            rs.close();
            pstmt.close();
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public Categoria buscar(int id) throws DatabaseException {
        Connection conn = DatabaseConnection.obtenerConexion();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM categorias WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Categoria cat = null;
            if (rs.next()) {
                cat = new Categoria();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
            }
            rs.close();
            pstmt.close();
            return cat;
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
    }
    
    public List<Categoria> obtenerTodas() throws DatabaseException {
        Connection conn = DatabaseConnection.obtenerConexion();
        List<Categoria> lista = new ArrayList<Categoria>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM categorias");
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                lista.add(cat);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error", e);
        }
        return lista;
    }
}
