package dominio;

/**
 * Clase JuegoException - Manejo de excepciones específicas del juego.
 * 
 *  */
public class JuegoException extends Exception {
    
    private int codigoError;
    private String timestamp;
    
    /**
     * Constructor con mensaje
     * @param mensaje Mensaje de error
     */
    public JuegoException(String mensaje) {
        super(mensaje);
        this.codigoError = 1000;
        this.timestamp = new java.util.Date().toString();
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Mensaje de error
     * @param causa Causa original
     */
    public JuegoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = 1001;
        this.timestamp = new java.util.Date().toString();
    }
    
    /**
     * Constructor completo
     * @param mensaje Mensaje de error
     * @param codigoError Código del error
     * @param causa Causa original
     */
    public JuegoException(String mensaje, int codigoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
        this.timestamp = new java.util.Date().toString();
    }
    
    public int getCodigoError() {
        return codigoError;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}