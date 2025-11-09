package service;

import model.*;
import dao.*;
import exception.*;
import util.*;

public class EmpleadoService {
    
    private EmpleadoDAO empleadoDAO;
    private SedeDAO sedeDAO;
    
    public EmpleadoService() {
        this.empleadoDAO = new EmpleadoDAO();
        this.sedeDAO = new SedeDAO();
    }
    
    public boolean registrarEmpleado(String nombre, String cargo, String email, String telefono, double salario, int sedeId) throws BibliotecaException {
        try {
            if (!ValidadorDatos.validarEmail(email)) {
                throw new BibliotecaException("Email inválido");
            }
            if (!ValidadorDatos.validarTelefono(telefono)) {
                throw new BibliotecaException("Teléfono inválido");
            }
            
            Empleado emp = new Empleado();
            emp.setId(GeneradorCodigos.generarCodigoEmpleado());
            emp.setNombre(nombre);
            emp.setCargo(cargo);
            emp.setEmail(email);
            emp.setTelefono(telefono);
            emp.setFechaContratacion(CalculadoraFechas.obtenerFechaActual());
            emp.setSalario(salario);
            emp.setSedeId(sedeId);
            
            return empleadoDAO.insertar(emp);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        } catch (BibliotecaException e) {
            throw e;
        }
    }
    
    public Empleado buscarEmpleado(String id) throws BibliotecaException {
        try {
            return empleadoDAO.buscar(id);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
