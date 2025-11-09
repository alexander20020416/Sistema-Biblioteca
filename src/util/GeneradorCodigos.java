package util;

import java.util.Random;

public class GeneradorCodigos {
    private static Random rand = new Random();
    
    public static String generarCodigoUsuario() {
        return "USR" + (10000 + rand.nextInt(90000));
    }
    
    public static String generarCodigoEmpleado() {
        return "EMP" + (1000 + rand.nextInt(9000));
    }
    
    public static String generarCodigoPrestamo() {
        return "PRE" + (100000 + rand.nextInt(900000));
    }
}
