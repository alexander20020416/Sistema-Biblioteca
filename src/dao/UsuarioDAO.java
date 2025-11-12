package dao;

import model.Usuario;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    private static final int VALOR_VERDADERO = 1;
    private static final int VALOR_FALSO = 0;
    
    public boolean insertar(Usuario usuario) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        String sqlInsert = "INSERT INTO usuarios (id, nombre, email, telefono, direccion, tipo, fecha_registro, activo, total_prestamos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, usuario.getId());
            preparedStatement.setString(2, usuario.getNombre());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getTelefono());
            preparedStatement.setString(5, usuario.getDireccion());
            preparedStatement.setString(6, usuario.getTipo());
            preparedStatement.setString(7, usuario.getFechaRegistro());
            preparedStatement.setInt(8, usuario.isActivo() ? VALOR_VERDADERO : VALOR_FALSO);
            preparedStatement.setInt(9, usuario.getTotalPrestamos());
            int filasAfectadas = preparedStatement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al insertar usuario", e);
        }
    }
    
    public Usuario buscar(String id) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        String sqlSelect = "SELECT * FROM usuarios WHERE id = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(resultSet.getString("id"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setEmail(resultSet.getString("email"));
                    usuario.setTelefono(resultSet.getString("telefono"));
                    usuario.setDireccion(resultSet.getString("direccion"));
                    usuario.setTipo(resultSet.getString("tipo"));
                    usuario.setFechaRegistro(resultSet.getString("fecha_registro"));
                    usuario.setActivo(resultSet.getInt("activo") == VALOR_VERDADERO);
                    usuario.setTotalPrestamos(resultSet.getInt("total_prestamos"));
                    return usuario;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar usuario", e);
        }
    }
    
    public List<Usuario> obtenerTodos() throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        List<Usuario> listaUsuarios = new ArrayList<Usuario>();
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM usuarios")) {
            
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getString("id"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setDireccion(resultSet.getString("direccion"));
                usuario.setTipo(resultSet.getString("tipo"));
                usuario.setFechaRegistro(resultSet.getString("fecha_registro"));
                usuario.setActivo(resultSet.getInt("activo") == VALOR_VERDADERO);
                usuario.setTotalPrestamos(resultSet.getInt("total_prestamos"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al obtener usuarios", e);
        }
        return listaUsuarios;
    }
    
    public boolean actualizar(Usuario usuario) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        String sqlUpdate = "UPDATE usuarios SET nombre=?, email=?, telefono=?, direccion=?, tipo=?, activo=?, total_prestamos=? WHERE id=?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getDireccion());
            preparedStatement.setString(5, usuario.getTipo());
            preparedStatement.setInt(6, usuario.isActivo() ? VALOR_VERDADERO : VALOR_FALSO);
            preparedStatement.setInt(7, usuario.getTotalPrestamos());
            preparedStatement.setString(8, usuario.getId());
            int filasActualizadas = preparedStatement.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar usuario", e);
        }
    }
}
