package dominio;

/**
 * Clase abstracta Entidad - Representa cualquier elemento que ocupa
 * una posición en el tablero del juego.
 * 
 * 
 * 
 * 
 * 
 */
public abstract class Entidad {
    
    // Atributos encapsulados (ENCAPSULAMIENTO) 
    protected int x;
    protected int y;
    
    /**
     * Constructor de la entidad
     * @param x Posición horizontal
     * @param y Posición vertical
     */
    public Entidad(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Métodos GETTERS Y SETTERS (ENCAPSULAMIENTO)
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Verifica si esta entidad colisiona con otra
     * @param otra Entidad con la que verificar colisión
     * @return true si colisionan (misma posición)
     */
    public boolean colisionaCon(Entidad otra) {
        return this.x == otra.getX() && this.y == otra.getY();
    }
    
    /**
     * Método abstracto para obtener la representación visual
     * @return String con el símbolo o identificador
     */
    public abstract String getIcono();
}