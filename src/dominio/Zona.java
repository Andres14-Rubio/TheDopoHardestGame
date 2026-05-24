package dominio;

/**
 * Clase Zona - Representa las zonas seguras (inicio, intermedia, final)
 * 
 * Aplica: HERENCIA (hereda de Entidad)
 * 
 * 
 * 
 */
public class Zona extends Entidad {
    
    // Constantes para los tipos de zona
    public static final String TIPO_INICIO = "INICIO";
    public static final String TIPO_INTERMEDIA = "INTERMEDIA";
    public static final String TIPO_FINAL = "FINAL";
    
    private String tipo;
    private boolean activa;
    
    /**
     * Constructor de la zona
     * @param x Posición X
     * @param y Posición Y
     * @param tipo Tipo de zona (INICIO, INTERMEDIA, FINAL)
     */
    public Zona(int x, int y, String tipo) {
        super(x, y);
        this.tipo = tipo;
        this.activa = false;
    }
    
    /**
     * Activa la zona como punto de reaparición (para zonas intermedias)
     */
    public void activar() {
        this.activa = true;
    }
    
    /**
     * Verifica si es una zona segura
     * @return true si es INICIO, INTERMEDIA o FINAL
     */
    public boolean esZonaSegura() {
        return tipo.equals(TIPO_INICIO) || 
               tipo.equals(TIPO_INTERMEDIA) || 
               tipo.equals(TIPO_FINAL);
    }
    
    /**
     * Verifica si es la zona final
     * @return true si es FINAL
     */
    public boolean esZonaFinal() {
        return tipo.equals(TIPO_FINAL);
    }
    
    /**
     * Verifica si es zona de inicio
     * @return true si es INICIO
     */
    public boolean esZonaInicio() {
        return tipo.equals(TIPO_INICIO);
    }
    
    // Getters y Setters
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public boolean isActiva() {
        return activa;
    }
    
    @Override
    public String getIcono() {
        return "🟩"; // Zona verde
    }
}