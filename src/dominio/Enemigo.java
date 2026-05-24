package dominio;

/**
 * Clase abstracta Enemigo - Representa a los enemigos del juego.
 * 
 */
public abstract class Enemigo extends Entidad implements Movible {
    
    protected int velocidad;
    
    public Enemigo(int x, int y, int velocidad) {
        super(x, y);
        this.velocidad = velocidad;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
}