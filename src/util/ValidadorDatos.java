package util;

public class ValidadorDatos {
    
    public static boolean validarISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) return false;
        isbn = isbn.replace("-", "").replace(" ", "");
        if (isbn.length() == 10) {
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                char c = isbn.charAt(i);
                if (c < '0' || c > '9') return false;
                suma += (c - '0') * (10 - i);
            }
            char ultimo = isbn.charAt(9);
            if (ultimo == 'X') suma += 10;
            else if (ultimo >= '0' && ultimo <= '9') suma += (ultimo - '0');
            else return false;
            return suma % 11 == 0;
        } else if (isbn.length() == 13) {
            int suma = 0;
            for (int i = 0; i < 13; i++) {
                char c = isbn.charAt(i);
                if (c < '0' || c > '9') return false;
                suma += (i % 2 == 0) ? (c - '0') : (c - '0') * 3;
            }
            return suma % 10 == 0;
        }
        return false;
    }
    
    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        int arroba = email.indexOf('@');
        if (arroba <= 0 || arroba == email.length() - 1) return false;
        int punto = email.lastIndexOf('.');
        return punto > arroba && punto < email.length() - 1;
    }
    
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) return false;
        telefono = telefono.replaceAll("[\\s\\-()]", "");
        if (telefono.length() < 7 || telefono.length() > 15) return false;
        for (int i = 0; i < telefono.length(); i++) {
            char c = telefono.charAt(i);
            if (c < '0' || c > '9') {
                if (i == 0 && c == '+') continue;
                return false;
            }
        }
        return true;
    }
    
    public static boolean validarNombre(String nombre) {
        return nombre != null && !nombre.isEmpty() && nombre.length() >= 2 && nombre.length() <= 100;
    }
    
    public static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
