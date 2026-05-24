package dominio;

import java.awt.*;

public class FuenteVida extends ElementoEspecial {

    private boolean usada;

    public FuenteVida(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.usada = false;
    }

    public boolean isUsada() {
        return usada;
    }

    public void usar() {
        this.usada = true;
    }

    @Override
    public void dibujar(Graphics g) {
        if (!usada) {
            g.setColor(new Color(255, 100, 255));
            g.fillRect(x, y, ancho, ancho);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, ancho - 10));
            g.drawString("❤️", x + 3, y + ancho - 5);
        }
    }
}