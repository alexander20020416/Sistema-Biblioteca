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
            if (!ValidadorDatos.validarISBN(isbn)) {
                throw new BibliotecaException("ISBN inválido");
            }
            
            // Verificar si el libro ya existe
            Libro libroExistente = libroDAO.buscar(isbn);
            if (libroExistente != null) {
                throw new BibliotecaException("El libro ya existe");
            }
            
            // Buscar o crear autor
            int autorId = -1;
            for (Autor a : autorDAO.obtenerTodos()) {
                if (a.getNombre().equalsIgnoreCase(nombreAutor)) {
                    autorId = a.getId();
                    break;
                }
            }
            if (autorId == -1) {
                Autor nuevoAutor = new Autor();
                nuevoAutor.setNombre(nombreAutor);
                nuevoAutor.setNacionalidad(nacionalidadAutor);
                autorId = autorDAO.insertar(nuevoAutor);
            }
            
            // Buscar o crear editorial
            int editorialId = -1;
            for (Editorial e : editorialDAO.obtenerTodas()) {
                if (e.getNombre().equalsIgnoreCase(nombreEditorial)) {
                    editorialId = e.getId();
                    break;
                }
            }
            if (editorialId == -1) {
                Editorial nuevaEditorial = new Editorial();
                nuevaEditorial.setNombre(nombreEditorial);
                nuevaEditorial.setPais(paisEditorial);
                editorialId = editorialDAO.insertar(nuevaEditorial);
            }
            
            // Buscar o crear categoría
            int categoriaId = -1;
            for (Categoria c : categoriaDAO.obtenerTodas()) {
                if (c.getNombre().equalsIgnoreCase(nombreCategoria)) {
                    categoriaId = c.getId();
                    break;
                }
            }
            if (categoriaId == -1) {
                Categoria nuevaCategoria = new Categoria();
                nuevaCategoria.setNombre(nombreCategoria);
                categoriaId = categoriaDAO.insertar(nuevaCategoria);
            }
            
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
