package dominio;

/**
 * Clase EnemigoRapido - Enemigo con velocidad 2x en línea recta
 */
public class EnemigoRapido extends EnemigoBasico {
    
    private static final int VELOCIDAD_RAPIDA = 2;
    
    public EnemigoRapido(int x, int y, boolean direccionHorizontal, 
                          int anchoTablero, int altoTablero) {
        super(x, y, direccionHorizontal, anchoTablero, altoTablero);
        this.velocidadBase = VELOCIDAD_RAPIDA;
    }
    
    @Override
    public String getIcono() {
        return "💨";
    }
}