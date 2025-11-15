package model;

/**
 * Clase Prestamo (refactorizada con comentarios)
 */
public class Prestamo {

    /** Identificador único del préstamo */
    private int id;

    /** ID del usuario que realizó el préstamo */
    private String usuarioId;

    /** ISBN del libro prestado */
    private String libroIsbn;

    /** Fecha en que se realizó el préstamo */
    private String fechaPrestamo;

    /** Fecha estimada para devolver el libro */
    private String fechaDevolucionEsperada;

    /** Fecha real en que el libro fue devuelto */
    private String fechaDevolucionReal;

    /** Estado del préstamo (activo, devuelto, vencido, etc.) */
    private String estado;

    /** ID del empleado que gestionó el préstamo */
    private String empleadoId;

    /** Constructor por defecto */
    public Prestamo() {}

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getLibroIsbn() { return libroIsbn; }
    public void setLibroIsbn(String libroIsbn) { this.libroIsbn = libroIsbn; }

    public String getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public String getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(String fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    public String getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(String fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(String empleadoId) { this.empleadoId = empleadoId; }
}
