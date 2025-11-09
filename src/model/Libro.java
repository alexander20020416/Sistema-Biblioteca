package model;

public class Libro {
    private String isbn;
    private String titulo;
    private int autorId;
    private int editorialId;
    private int categoriaId;
    private int anio;
    private int paginas;
    private boolean disponible;
    private int vecesPrestado;
    private int sedeId;
    
    public Libro() {
        this.disponible = true;
    }
    
    public Libro(String isbn, String titulo) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.disponible = true;
    }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }
    
    public int getEditorialId() { return editorialId; }
    public void setEditorialId(int editorialId) { this.editorialId = editorialId; }
    
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    
    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    public int getVecesPrestado() { return vecesPrestado; }
    public void setVecesPrestado(int vecesPrestado) { this.vecesPrestado = vecesPrestado; }
    
    public int getSedeId() { return sedeId; }
    public void setSedeId(int sedeId) { this.sedeId = sedeId; }
}
