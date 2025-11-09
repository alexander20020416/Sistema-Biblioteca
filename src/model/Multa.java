package model;

public class Multa {
    private int id;
    private int prestamoId;
    private String usuarioId;
    private double monto;
    private String razon;
    private String fecha;
    private boolean pagada;
    private String fechaPago;
    
    public Multa() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPrestamoId() { return prestamoId; }
    public void setPrestamoId(int prestamoId) { this.prestamoId = prestamoId; }
    
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    
    public String getRazon() { return razon; }
    public void setRazon(String razon) { this.razon = razon; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }
    
    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
}
