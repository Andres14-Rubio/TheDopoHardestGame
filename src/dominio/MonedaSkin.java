package dominio;

/**
 * Clase MonedaSkin - Moneda especial que otorga un skin temporal
 * 
 */
public class MonedaSkin extends Moneda {
    
    private String skinOtorgado;  // Rojo, Azul o Verde
    private int duracion;          // Duración en movimientos o segundos
    
    /**
     * Constructor de moneda skin
     * @param x Posición X
     * @param y Posición Y
     * @param skinOtorgado Tipo de skin que otorga
     * @param duracion Duración del efecto
     */
    public MonedaSkin(int x, int y, String skinOtorgado, int duracion) {
        super(x, y);
        this.skinOtorgado = skinOtorgado;
        this.duracion = duracion;
    }
    
    public String getSkinOtorgado() {
        return skinOtorgado;
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    @Override
    public String getIcono() {
        switch (skinOtorgado) {
            case Jugador.SKIN_ROJO: return "🔴";
            case Jugador.SKIN_AZUL: return "🔵";
            case Jugador.SKIN_VERDE: return "🟢";
            default: return "💎";
        }
    }
}