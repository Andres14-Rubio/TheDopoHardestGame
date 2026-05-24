package dominio;

import java.awt.*;

/**
 * Clase Bomba - Elemento especial que destruye jugadores y enemigos.
 *
 */
public class Bomba extends ElementoEspecial {

    private boolean explotada;

    /**
     * Constructor de la bomba
     * @param x Posición X en el tablero
     * @param y Posición Y en el tablero
     * @param ancho Ancho de la bomba (normalmente 1 celda)
     * @param alto Alto de la bomba (normalmente 1 celda)
     */
    public Bomba(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.explotada = false;
    }

    /**
     * Marca la bomba como explotada (desaparece después de explotar)
     */
    public void explotar() {
        this.explotada = true;
        ErrorLogger.getInstance().logError("💣 Bomba explotó en (" + x + "," + y + ")");
    }

    /**
     * Verifica si la bomba ya explotó
     * @return true si ya explotó
     */
    public boolean isExplotada() {
        return explotada;
    }

    @Override
    public void dibujar(Graphics g) {
        if (!explotada) {
            // Círculo exterior negro
            g.setColor(Color.BLACK);
            g.fillOval(x + 8, y + 8, ancho - 16, alto - 16);

            // Círculo interior rojo (mecha)
            g.setColor(Color.RED);
            g.fillOval(x + 11, y + 11, ancho - 22, alto - 22);

            // Detalles de la mecha
            g.setColor(Color.ORANGE);
            g.drawLine(x + ancho - 10, y + 5, x + ancho - 5, y + 2);
            g.drawLine(x + ancho - 8, y + 3, x + ancho - 4, y + 1);

            // Chispa
            g.setColor(Color.YELLOW);
            g.fillOval(x + ancho - 6, y - 1, 5, 5);

            // Icono de bomba (opcional)
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, ancho - 20));
            g.drawString("💣", x + 7, y + alto - 8);
        }
    }
}