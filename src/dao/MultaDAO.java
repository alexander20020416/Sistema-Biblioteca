package dao;

import model.Multa;
import util.DatabaseConnection;
import exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO {
    
    private static final int VALOR_VERDADERO = 1;
    private static final int VALOR_FALSO = 0;
    
    public int insertar(Multa multa) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        String sqlInsert = "INSERT INTO multas (prestamo_id, usuario_id, monto, razon, fecha, pagada, fecha_pago) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, multa.getPrestamoId());
            preparedStatement.setString(2, multa.getUsuarioId());
            preparedStatement.setDouble(3, multa.getMonto());
            preparedStatement.setString(4, multa.getRazon());
            preparedStatement.setString(5, multa.getFecha());
            preparedStatement.setInt(6, multa.isPagada() ? VALOR_VERDADERO : VALOR_FALSO);
            preparedStatement.setString(7, multa.getFechaPago());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int multaId = resultSet.next() ? resultSet.getInt(1) : -1;
            resultSet.close();
            preparedStatement.close();
            return multaId;
        } catch (SQLException e) {
            throw new DatabaseException("Error al insertar multa en la base de datos", e);
        }
    }
    
    public List<Multa> obtenerPorUsuario(String usuarioId) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        List<Multa> listaMultas = new ArrayList<Multa>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM multas WHERE usuario_id = ?");
            preparedStatement.setString(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Multa multa = new Multa();
                multa.setId(resultSet.getInt("id"));
                multa.setPrestamoId(resultSet.getInt("prestamo_id"));
                multa.setUsuarioId(resultSet.getString("usuario_id"));
                multa.setMonto(resultSet.getDouble("monto"));
                multa.setRazon(resultSet.getString("razon"));
                multa.setFecha(resultSet.getString("fecha"));
                multa.setPagada(resultSet.getInt("pagada") == VALOR_VERDADERO);
                multa.setFechaPago(resultSet.getString("fecha_pago"));
                listaMultas.add(multa);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error al obtener multas del usuario", e);
        }
        return listaMultas;
    }
    
    public boolean actualizar(Multa multa) throws DatabaseException {
        Connection connection = DatabaseConnection.obtenerConexion();
        String sqlUpdate = "UPDATE multas SET pagada=?, fecha_pago=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setInt(1, multa.isPagada() ? VALOR_VERDADERO : VALOR_FALSO);
            preparedStatement.setString(2, multa.getFechaPago());
            preparedStatement.setInt(3, multa.getId());
            int filasActualizadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar multa", e);
        }
    }
}
