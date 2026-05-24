package dominio;

/**
 * Clase Moneda - Representa las monedas amarillas que el jugador debe recolectar.
 * 
 * Aplica: HERENCIA (hereda de Entidad)
 * 
 * 
 * 
 */
public class Moneda extends Entidad {
    
    private boolean recolectada;
    
    /**
     * Constructor de la moneda
     * @param x Posición X
     * @param y Posición Y
     */
    public Moneda(int x, int y) {
        super(x, y);
        this.recolectada = false;
    }
    
    /**
     * Marca la moneda como recolectada
     */
    public void recolectar() {
        this.recolectada = true;
    }
    
    /**
     * Verifica si la moneda ya fue recolectada
     * @return true si fue recolectada
     */
    public boolean estaRecolectada() {
        return recolectada;
    }
    
    @Override
    public String getIcono() {
        return "🟨"; // Moneda amarilla
    }
}