package dominio;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase ErrorLogger - Patrón Singleton para registro de errores del juego.
 * 
 * Aplica: Patrón SINGLETON (creación de una sola instancia)
 * 
 *
 */
public class ErrorLogger {
    
    private static ErrorLogger instancia;
    private String archivoLog;
    private PrintWriter writer;
    
    /**
     * Constructor privado (Singleton)
     */
    private ErrorLogger() {
        this.archivoLog = "error.log";
        inicializarArchivo();
    }
    
    /**
     * Obtiene la única instancia del ErrorLogger
     * @return Instancia única
     */
    public static ErrorLogger getInstance() {
        if (instancia == null) {
            instancia = new ErrorLogger();
        }
        return instancia;
    }
    
    /**
     * Inicializa el archivo de log
     */
    private void inicializarArchivo() {
        try {
            writer = new PrintWriter(new FileWriter(archivoLog, true));
            writer.println("========================================");
            writer.println("LOG DE ERRORES - THE DOPO HARDEST GAME");
            writer.println("Iniciado: " + new Date());
            writer.println("========================================");
            writer.flush();
        } catch (IOException e) {
            System.err.println("No se pudo inicializar el archivo de log: " + e.getMessage());
        }
    }
    
    /**
     * Registra un error en el log
     * @param mensaje Mensaje de error
     */
    public void logError(String mensaje) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logLinea = "[" + timestamp + "] INFO: " + mensaje;
        
        System.out.println(logLinea);
        
        if (writer != null) {
            writer.println(logLinea);
            writer.flush();
        }
    }
    
    /**
     * Registra una excepción en el log
     * @param mensaje Mensaje de error
     * @param e Excepción ocurrida
     */
    public void logError(String mensaje, Throwable e) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logLinea = "[" + timestamp + "] ERROR: " + mensaje;
        String logDetalle = "[" + timestamp + "] DETALLE: " + e.toString();
        
        System.err.println(logLinea);
        System.err.println(logDetalle);
        
        if (writer != null) {
            writer.println(logLinea);
            writer.println(logDetalle);
            writer.flush();
        }
    }
    
    /**
     * Guarda el log actual en disco
     */
    public void guardarLog() {
        if (writer != null) {
            writer.flush();
        }
    }
    
    /**
     * Limpia el archivo de log
     */
    public void limpiarLog() {
        try {
            writer.close();
            writer = new PrintWriter(new FileWriter(archivoLog));
            writer.println("========================================");
            writer.println("LOG LIMPIADO - " + new Date());
            writer.println("========================================");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al limpiar log: " + e.getMessage());
        }
    }
}