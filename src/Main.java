import service.*;
import exception.*;
import util.DatabaseConnection;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioService usuarioService = new UsuarioService();
    private static BibliotecaService bibliotecaService = new BibliotecaService();
    private static EmpleadoService empleadoService = new EmpleadoService();
    private static PrestamoService prestamoService = new PrestamoService();
    private static ReporteService reporteService = new ReporteService();
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE BIBLIOTECA ===\n");
        
        try {
            DatabaseConnection.inicializarBaseDatos();
            System.out.println("Base de datos inicializada correctamente\n");
            
            mostrarMenu();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static void mostrarMenu() {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Registrar Empleado");
            System.out.println("3. Registrar Libro");
            System.out.println("4. Realizar Prestamo");
            System.out.println("5. Devolver Libro");
            System.out.println("6. Ver Libros Disponibles");
            System.out.println("7. Buscar Libro");
            System.out.println("8. Buscar Usuario");
            System.out.println("9. Generar Reporte General");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione opcion: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                System.out.println();
                
                switch (opcion) {
                    case 1: registrarUsuario(); break;
                    case 2: registrarEmpleado(); break;
                    case 3: registrarLibro(); break;
                    case 4: realizarPrestamo(); break;
                    case 5: devolverLibro(); break;
                    case 6: verLibrosDisponibles(); break;
                    case 7: buscarLibro(); break;
                    case 8: buscarUsuario(); break;
                    case 9: generarReporte(); break;
                    case 0: 
                        salir = true;
                        System.out.println("Gracias por usar el sistema. Adios!");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero valido");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void registrarUsuario() {
        try {
            System.out.println("--- REGISTRAR USUARIO ---");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefono: ");
            String telefono = scanner.nextLine();
            System.out.print("Direccion: ");
            String direccion = scanner.nextLine();
            System.out.print("Tipo (ESTUDIANTE/DOCENTE/PUBLICO): ");
            String tipo = scanner.nextLine();
            
            usuarioService.registrarUsuario(nombre, email, telefono, direccion, tipo);
            System.out.println("Usuario registrado exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void registrarEmpleado() {
        try {
            System.out.println("--- REGISTRAR EMPLEADO ---");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Cargo: ");
            String cargo = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefono: ");
            String telefono = scanner.nextLine();
            System.out.print("Salario: ");
            double salario = Double.parseDouble(scanner.nextLine());
            
            empleadoService.registrarEmpleado(nombre, cargo, email, telefono, salario, 1);
            System.out.println("Empleado registrado exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void registrarLibro() {
        try {
            System.out.println("--- REGISTRAR LIBRO ---");
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Nacionalidad Autor: ");
            String nacionalidad = scanner.nextLine();
            System.out.print("Editorial: ");
            String editorial = scanner.nextLine();
            System.out.print("Pais Editorial: ");
            String pais = scanner.nextLine();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Anio: ");
            int anio = Integer.parseInt(scanner.nextLine());
            System.out.print("Paginas: ");
            int paginas = Integer.parseInt(scanner.nextLine());
            
            bibliotecaService.registrarLibroCompleto(isbn, titulo, autor, nacionalidad, editorial, pais, categoria, anio, paginas, 1);
            System.out.println("Libro registrado exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void realizarPrestamo() {
        try {
            System.out.println("--- REALIZAR PRESTAMO ---");
            System.out.print("ID Usuario: ");
            String usuarioId = scanner.nextLine();
            System.out.print("ISBN Libro: ");
            String libroIsbn = scanner.nextLine();
            System.out.print("ID Empleado: ");
            String empleadoId = scanner.nextLine();
            System.out.print("Dias de prestamo: ");
            int dias = Integer.parseInt(scanner.nextLine());
            
            int prestamoId = prestamoService.realizarPrestamo(usuarioId, libroIsbn, empleadoId, dias);
            System.out.println("Prestamo #" + prestamoId + " realizado exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void devolverLibro() {
        try {
            System.out.println("--- DEVOLVER LIBRO ---");
            System.out.print("ID Prestamo: ");
            int prestamoId = Integer.parseInt(scanner.nextLine());
            
            prestamoService.devolverPrestamo(prestamoId);
            System.out.println("Libro devuelto exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void verLibrosDisponibles() {
        try {
            int disponibles = bibliotecaService.contarLibrosDisponibles();
            System.out.println("Libros disponibles: " + disponibles);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void buscarLibro() {
        try {
            System.out.print("ISBN del libro: ");
            String isbn = scanner.nextLine();
            
            model.Libro libro = bibliotecaService.buscarLibro(isbn);
            if (libro != null) {
                System.out.println("\n--- LIBRO ENCONTRADO ---");
                System.out.println("Titulo: " + libro.getTitulo());
                System.out.println("ISBN: " + libro.getIsbn());
                System.out.println("Anio: " + libro.getAnio());
                System.out.println("Paginas: " + libro.getPaginas());
                System.out.println("Disponible: " + (libro.isDisponible() ? "Si" : "No"));
                System.out.println("Veces prestado: " + libro.getVecesPrestado());
            } else {
                System.out.println("Libro no encontrado");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void buscarUsuario() {
        try {
            System.out.print("ID del usuario: ");
            String id = scanner.nextLine();
            
            model.Usuario usuario = usuarioService.buscarUsuario(id);
            if (usuario != null) {
                System.out.println("\n--- USUARIO ENCONTRADO ---");
                System.out.println("ID: " + usuario.getId());
                System.out.println("Nombre: " + usuario.getNombre());
                System.out.println("Email: " + usuario.getEmail());
                System.out.println("Telefono: " + usuario.getTelefono());
                System.out.println("Tipo: " + usuario.getTipo());
                System.out.println("Activo: " + (usuario.isActivo() ? "Si" : "No"));
                System.out.println("Total prestamos: " + usuario.getTotalPrestamos());
            } else {
                System.out.println("Usuario no encontrado");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void generarReporte() {
        try {
            String reporte = reporteService.generarReporteGeneral();
            System.out.println(reporte);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
