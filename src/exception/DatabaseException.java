package exception;

public class DatabaseException extends BibliotecaException {
    public DatabaseException(String mensaje) {
        super(mensaje);
    }
    
    public DatabaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
