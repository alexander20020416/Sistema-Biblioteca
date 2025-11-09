package service;

import model.*;
import dao.*;
import exception.*;
import util.*;
import java.util.List;

public class MultaService {
    
    private MultaDAO multaDAO;
    private PrestamoDAO prestamoDAO;
    private ConfiguracionDAO configDAO;
    
    public MultaService() {
        this.multaDAO = new MultaDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.configDAO = new ConfiguracionDAO();
    }
    
    // Code smell: magic numbers
    public void generarMultasPorRetraso() throws BibliotecaException {
        try {
            String fechaHoy = CalculadoraFechas.obtenerFechaActual();
            List<Prestamo> prestamosActivos = prestamoDAO.obtenerActivos();
            
            for (Prestamo p : prestamosActivos) {
                if (CalculadoraFechas.esFechaPasada(p.getFechaDevolucionEsperada())) {
                    int diasRetraso = CalculadoraFechas.calcularDiferenciaDias(p.getFechaDevolucionEsperada(), fechaHoy);
                    
                    // Magic number: 2.5 dólares por día
                    double monto = diasRetraso * 2.5;
                    
                    Multa multa = new Multa();
                    multa.setPrestamoId(p.getId());
                    multa.setUsuarioId(p.getUsuarioId());
                    multa.setMonto(monto);
                    multa.setRazon("Retraso de " + diasRetraso + " días");
                    multa.setFecha(fechaHoy);
                    multa.setPagada(false);
                    
                    multaDAO.insertar(multa);
                }
            }
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    public boolean pagarMulta(int multaId) throws BibliotecaException {
        try {
            List<Multa> multas = multaDAO.obtenerPorUsuario("dummy");
            Multa multa = null;
            for (Multa m : multas) {
                if (m.getId() == multaId) {
                    multa = m;
                    break;
                }
            }
            
            if (multa == null) {
                throw new BibliotecaException("Multa no encontrada");
            }
            
            multa.setPagada(true);
            multa.setFechaPago(CalculadoraFechas.obtenerFechaActual());
            
            return multaDAO.actualizar(multa);
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
    
    public double calcularTotalMultas(String usuarioId) throws BibliotecaException {
        try {
            List<Multa> multas = multaDAO.obtenerPorUsuario(usuarioId);
            double total = 0;
            for (Multa m : multas) {
                if (!m.isPagada()) {
                    total = total + m.getMonto();
                }
            }
            return total;
        } catch (DatabaseException e) {
            throw new BibliotecaException("Error", e);
        }
    }
}
