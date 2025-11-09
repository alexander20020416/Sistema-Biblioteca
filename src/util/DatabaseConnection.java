package util;

import java.sql.*;
import exception.DatabaseException;

public class DatabaseConnection {
    private static Connection conexion = null;
    private static String url = "jdbc:sqlite:biblioteca.db";
    
    public static Connection obtenerConexion() throws DatabaseException {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url);
                inicializarBaseDatos();
            }
            return conexion;
        } catch (SQLException e) {
            throw new DatabaseException("Error al conectar con la base de datos", e);
        }
    }
    
    public static void inicializarBaseDatos() throws SQLException {
        if (conexion == null) {
            try {
                obtenerConexion();
            } catch (DatabaseException e) {
                throw new SQLException(e);
            }
        }
        Statement stmt = conexion.createStatement();
        
        // Tabla libros
        String sql1 = "CREATE TABLE IF NOT EXISTS libros (" +
            "isbn TEXT PRIMARY KEY, titulo TEXT NOT NULL, autor_id INTEGER, editorial_id INTEGER, " +
            "categoria_id INTEGER, anio INTEGER, paginas INTEGER, disponible INTEGER DEFAULT 1, " +
            "veces_prestado INTEGER DEFAULT 0, sede_id INTEGER)";
        stmt.execute(sql1);
        
        // Tabla usuarios
        String sql2 = "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id TEXT PRIMARY KEY, nombre TEXT NOT NULL, email TEXT, telefono TEXT, direccion TEXT, " +
            "tipo TEXT, fecha_registro TEXT, activo INTEGER DEFAULT 1, total_prestamos INTEGER DEFAULT 0)";
        stmt.execute(sql2);
        
        // Tabla prestamos
        String sql3 = "CREATE TABLE IF NOT EXISTS prestamos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, libro_isbn TEXT, " +
            "fecha_prestamo TEXT, fecha_devolucion_esperada TEXT, fecha_devolucion_real TEXT, " +
            "estado TEXT, empleado_id TEXT)";
        stmt.execute(sql3);
        
        // Tabla autores
        String sql4 = "CREATE TABLE IF NOT EXISTS autores (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, nacionalidad TEXT, " +
            "biografia TEXT, anio_nacimiento INTEGER)";
        stmt.execute(sql4);
        
        // Tabla editoriales
        String sql5 = "CREATE TABLE IF NOT EXISTS editoriales (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, pais TEXT, ciudad TEXT, " +
            "telefono TEXT, email TEXT)";
        stmt.execute(sql5);
        
        // Tabla categorias
        String sql6 = "CREATE TABLE IF NOT EXISTS categorias (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, descripcion TEXT)";
        stmt.execute(sql6);
        
        // Tabla multas
        String sql7 = "CREATE TABLE IF NOT EXISTS multas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, prestamo_id INTEGER, usuario_id TEXT, " +
            "monto REAL, razon TEXT, fecha TEXT, pagada INTEGER DEFAULT 0, fecha_pago TEXT)";
        stmt.execute(sql7);
        
        // Tabla reservas
        String sql8 = "CREATE TABLE IF NOT EXISTS reservas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, libro_isbn TEXT, " +
            "fecha_reserva TEXT, fecha_expiracion TEXT, estado TEXT)";
        stmt.execute(sql8);
        
        // Tabla empleados
        String sql9 = "CREATE TABLE IF NOT EXISTS empleados (" +
            "id TEXT PRIMARY KEY, nombre TEXT NOT NULL, cargo TEXT, email TEXT, telefono TEXT, " +
            "fecha_contratacion TEXT, salario REAL, sede_id INTEGER)";
        stmt.execute(sql9);
        
        // Tabla sedes
        String sql10 = "CREATE TABLE IF NOT EXISTS sedes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, direccion TEXT, " +
            "telefono TEXT, horario TEXT, capacidad INTEGER)";
        stmt.execute(sql10);
        
        // Tabla notificaciones
        String sql11 = "CREATE TABLE IF NOT EXISTS notificaciones (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, tipo TEXT, mensaje TEXT, " +
            "fecha TEXT, leida INTEGER DEFAULT 0)";
        stmt.execute(sql11);
        
        // Tabla configuracion
        String sql12 = "CREATE TABLE IF NOT EXISTS configuracion (" +
            "clave TEXT PRIMARY KEY, valor TEXT, descripcion TEXT)";
        stmt.execute(sql12);
        
        stmt.close();
    }
    
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
