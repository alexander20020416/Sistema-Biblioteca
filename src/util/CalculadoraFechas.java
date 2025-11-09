package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class CalculadoraFechas {
    private static SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String obtenerFechaActual() {
        return formato.format(new Date());
    }
    
    public static String agregarDias(String fecha, int dias) {
        try {
            Date d = formato.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_MONTH, dias);
            return formato.format(cal.getTime());
        } catch (Exception e) {
            return fecha;
        }
    }
    
    public static int calcularDiferenciaDias(String fecha1, String fecha2) {
        try {
            Date d1 = formato.parse(fecha1);
            Date d2 = formato.parse(fecha2);
            long diferencia = d2.getTime() - d1.getTime();
            return (int) (diferencia / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static boolean esFechaPasada(String fecha) {
        try {
            Date d = formato.parse(fecha);
            return d.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
