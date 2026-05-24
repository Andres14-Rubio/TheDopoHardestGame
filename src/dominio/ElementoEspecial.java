package dominio;

import java.awt.*;

/**
 * Clase abstracta ElementoEspecial - Representa elementos especiales
 * como bombas, fuentes de vida, etc.
 */
public abstract class ElementoEspecial extends Entidad {

    protected int ancho;
    protected int alto;

    /**
     * Constructor de ElementoEspecial
     * @param x Posición X en el tablero
     * @param y Posición Y en el tablero
     * @param ancho Ancho del elemento (normalmente 1 celda)
     * @param alto Alto del elemento (normalmente 1 celda)
     */
    public ElementoEspecial(int x, int y, int ancho, int alto) {
        super(x, y);
        this.ancho = ancho;
        this.alto = alto;
    }

    /**
     * Obtiene el área de colisión del elemento
     * @return Rectangle con la posición y tamaño
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    /**
     * Dibuja el elemento especial en pantalla
     * @param g Objeto Graphics para dibujar
     */
    public abstract void dibujar(Graphics g);

    // Getters y Setters
    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    @Override
    public String getIcono() {
        return "⭐"; // Icono genérico para elementos especiales
    }
}