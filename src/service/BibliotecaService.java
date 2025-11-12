package service;

import model.*;
import dao.*;
import exception.*;
import util.*;

public class BibliotecaService {
    
    private LibroDAO libroDAO;
    private AutorDAO autorDAO;
    private EditorialDAO editorialDAO;
    private CategoriaDAO categoriaDAO;
    private SedeDAO sedeDAO;
    
    public BibliotecaService() {
        this.libroDAO = new LibroDAO();
        this.autorDAO = new AutorDAO();
        this.editorialDAO = new EditorialDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.sedeDAO = new SedeDAO();
    }
    
    // Code smell: método muy largo con muchas responsabilidades
    public boolean registrarLibroCompleto(String isbn, String titulo, String nombreAutor, String nacionalidadAutor, String nombreEditorial, String paisEditorial, String nombreCategoria, int anio, int paginas, int sedeId) throws BibliotecaException {
        try {
            // Validar ISBN
            boolean isbnValido = ValidadorDatos.validarISBN(isbn);
            if (!isbnValido) {
                throw new BibliotecaException("ISBN inválido");
            }
            
            // Verificar si el libro ya existe
            Libro libroExistente = libroDAO.buscar(isbn);
            boolean libroYaExiste = (libroExistente != null);
            if (libroYaExiste) {
                throw new BibliotecaException("El libro ya existe");
            }
            
            // Obtener o crear entidades relacionadas
            int autorId = obtenerOCrearAutor(nombreAutor, nacionalidadAutor);
            int editorialId = obtenerOCrearEditorial(nombreEditorial, paisEditorial);
            int categoriaId = obtenerOCrearCategoria(nombreCategoria);
            
            // Crear el libro
            Libro libro = new Libro();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutorId(autorId);
            libro.setEditorialId(editorialId);
            libro.setCategoriaId(categoriaId);
            libro.setAnio(anio);
            libro.setPaginas(paginas);
            libro.setDisponible(true);
            libro.setVecesPrestado(0);
            libro.setSedeId(sedeId);
            
            return libroDAO.insertar(libro);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al registrar libro", e);
        }
    }
    
    private int obtenerOCrearAutor(String nombreAutor, String nacionalidadAutor) throws DatabaseException {
        for (Autor autor : autorDAO.obtenerTodos()) {
            boolean nombreCoincide = autor.getNombre().equalsIgnoreCase(nombreAutor);
            if (nombreCoincide) {
                return autor.getId();
            }
        }
        
        // Si no existe, crear nuevo autor
        Autor nuevoAutor = new Autor();
        nuevoAutor.setNombre(nombreAutor);
        nuevoAutor.setNacionalidad(nacionalidadAutor);
        return autorDAO.insertar(nuevoAutor);
    }
    
    private int obtenerOCrearEditorial(String nombreEditorial, String paisEditorial) throws DatabaseException {
        for (Editorial editorial : editorialDAO.obtenerTodas()) {
            boolean nombreCoincide = editorial.getNombre().equalsIgnoreCase(nombreEditorial);
            if (nombreCoincide) {
                return editorial.getId();
            }
        }
        
        // Si no existe, crear nueva editorial
        Editorial nuevaEditorial = new Editorial();
        nuevaEditorial.setNombre(nombreEditorial);
        nuevaEditorial.setPais(paisEditorial);
        return editorialDAO.insertar(nuevaEditorial);
    }
    
    private int obtenerOCrearCategoria(String nombreCategoria) throws DatabaseException {
        for (Categoria categoria : categoriaDAO.obtenerTodas()) {
            boolean nombreCoincide = categoria.getNombre().equalsIgnoreCase(nombreCategoria);
            if (nombreCoincide) {
                return categoria.getId();
            }
        }
        
        // Si no existe, crear nueva categoría
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(nombreCategoria);
        return categoriaDAO.insertar(nuevaCategoria);
    }
    
    public Libro buscarLibro(String isbn) throws BibliotecaException {
        try {
            return libroDAO.buscar(isbn);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error al buscar libro", e);
        }
    }
    
    public int contarLibrosDisponibles() throws BibliotecaException {
        try {
            int contador = 0;
            for (Libro l : libroDAO.obtenerTodos()) {
                if (l.isDisponible()) {
                    contador++;
                }
            }
            return contador;
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
