package util;

import java.sql.*;
import java.io.*;
import java.util.Properties;
import exception.DatabaseException;

public class DatabaseConnection {
    private static Connection conexion = null;
    private static String url;
    
    // Bloque estático para cargar configuración desde archivo
    static {
        try {
            // Cargar el driver de SQLite
            Class.forName("org.sqlite.JDBC");
            
            Properties properties = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config.properties");
            
            if (input == null) {
                // Valor por defecto si no se encuentra el archivo
                url = "jdbc:sqlite:biblioteca.db";
            } else {
                properties.load(input);
                url = properties.getProperty("db.url");
                input.close();
            }
        } catch (IOException e) {
            // Valor por defecto en caso de error
            url = "jdbc:sqlite:biblioteca.db";
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de SQLite");
            url = "jdbc:sqlite:biblioteca.db";
        }
    }
    
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
        
        crearTablasCatalogo(stmt);
        crearTablasUsuariosEmpleados(stmt);
        crearTablasPrestamosReservas(stmt);
        crearTablasAuxiliares(stmt);
        
        stmt.close();
    }
    
    private static void crearTablasCatalogo(Statement stmt) throws SQLException {
        // Tabla libros
        String sqlLibros = "CREATE TABLE IF NOT EXISTS libros (" +
            "isbn TEXT PRIMARY KEY, titulo TEXT NOT NULL, autor_id INTEGER, editorial_id INTEGER, " +
            "categoria_id INTEGER, anio INTEGER, paginas INTEGER, disponible INTEGER DEFAULT 1, " +
            "veces_prestado INTEGER DEFAULT 0, sede_id INTEGER)";
        stmt.execute(sqlLibros);
        
        // Tabla autores
        String sqlAutores = "CREATE TABLE IF NOT EXISTS autores (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, nacionalidad TEXT, " +
            "biografia TEXT, anio_nacimiento INTEGER)";
        stmt.execute(sqlAutores);
        
        // Tabla editoriales
        String sqlEditoriales = "CREATE TABLE IF NOT EXISTS editoriales (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, pais TEXT, ciudad TEXT, " +
            "telefono TEXT, email TEXT)";
        stmt.execute(sqlEditoriales);
        
        // Tabla categorias
        String sqlCategorias = "CREATE TABLE IF NOT EXISTS categorias (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, descripcion TEXT)";
        stmt.execute(sqlCategorias);
    }
    
    private static void crearTablasUsuariosEmpleados(Statement stmt) throws SQLException {
        // Tabla usuarios
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id TEXT PRIMARY KEY, nombre TEXT NOT NULL, email TEXT, telefono TEXT, direccion TEXT, " +
            "tipo TEXT, fecha_registro TEXT, activo INTEGER DEFAULT 1, total_prestamos INTEGER DEFAULT 0)";
        stmt.execute(sqlUsuarios);
        
        // Tabla empleados
        String sqlEmpleados = "CREATE TABLE IF NOT EXISTS empleados (" +
            "id TEXT PRIMARY KEY, nombre TEXT NOT NULL, cargo TEXT, email TEXT, telefono TEXT, " +
            "fecha_contratacion TEXT, salario REAL, sede_id INTEGER)";
        stmt.execute(sqlEmpleados);
        
        // Tabla sedes
        String sqlSedes = "CREATE TABLE IF NOT EXISTS sedes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, direccion TEXT, " +
            "telefono TEXT, horario TEXT, capacidad INTEGER)";
        stmt.execute(sqlSedes);
    }
    
    private static void crearTablasPrestamosReservas(Statement stmt) throws SQLException {
        // Tabla prestamos
        String sqlPrestamos = "CREATE TABLE IF NOT EXISTS prestamos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, libro_isbn TEXT, " +
            "fecha_prestamo TEXT, fecha_devolucion_esperada TEXT, fecha_devolucion_real TEXT, " +
            "estado TEXT, empleado_id TEXT)";
        stmt.execute(sqlPrestamos);
        
        // Tabla reservas
        String sqlReservas = "CREATE TABLE IF NOT EXISTS reservas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, libro_isbn TEXT, " +
            "fecha_reserva TEXT, fecha_expiracion TEXT, estado TEXT)";
        stmt.execute(sqlReservas);
        
        // Tabla multas
        String sqlMultas = "CREATE TABLE IF NOT EXISTS multas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, prestamo_id INTEGER, usuario_id TEXT, " +
            "monto REAL, razon TEXT, fecha TEXT, pagada INTEGER DEFAULT 0, fecha_pago TEXT)";
        stmt.execute(sqlMultas);
    }
    
    private static void crearTablasAuxiliares(Statement stmt) throws SQLException {
        // Tabla notificaciones
        String sqlNotificaciones = "CREATE TABLE IF NOT EXISTS notificaciones (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id TEXT, tipo TEXT, mensaje TEXT, " +
            "fecha TEXT, leida INTEGER DEFAULT 0)";
        stmt.execute(sqlNotificaciones);
        
        // Tabla configuracion
        String sqlConfiguracion = "CREATE TABLE IF NOT EXISTS configuracion (" +
            "clave TEXT PRIMARY KEY, valor TEXT, descripcion TEXT)";
        stmt.execute(sqlConfiguracion);
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
