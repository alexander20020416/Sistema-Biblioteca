package exception;

public class UsuarioException extends BibliotecaException {
    public UsuarioException(String mensaje) {
        super(mensaje);
    }
    
    public UsuarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
