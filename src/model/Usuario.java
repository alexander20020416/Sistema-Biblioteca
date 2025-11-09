package model;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String tipo;
    private String fechaRegistro;
    private boolean activo;
    private int totalPrestamos;
    
    public Usuario() {
        this.activo = true;
    }
    
    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.activo = true;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public int getTotalPrestamos() { return totalPrestamos; }
    public void setTotalPrestamos(int totalPrestamos) { this.totalPrestamos = totalPrestamos; }
}
