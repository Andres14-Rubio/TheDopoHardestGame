package presentacion;

import java.awt.*;
import dominio.*;

public class Renderizador {
    
    private static final int TAMANO_CELDA = 40;
    
    public static void dibujarPared(Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, TAMANO_CELDA, TAMANO_CELDA);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TAMANO_CELDA, TAMANO_CELDA);
    }
    
    public static void dibujarJugador(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x + 5, y + 5, TAMANO_CELDA - 10, TAMANO_CELDA - 10);
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y + 5, TAMANO_CELDA - 10, TAMANO_CELDA - 10);
    }
    
    public static void dibujarEnemigoBasico(Graphics g, int x, int y) {
        g.setColor(new Color(0, 0, 255));
        g.fillOval(x + 5, y + 5, TAMANO_CELDA - 10, TAMANO_CELDA - 10);
        g.setColor(Color.BLACK);
        g.drawOval(x + 5, y + 5, TAMANO_CELDA - 10, TAMANO_CELDA - 10);
    }
    
    public static void dibujarMoneda(Graphics g, int x, int y) {
        g.setColor(new Color(255, 215, 0));
        g.fillOval(x + 8, y + 8, TAMANO_CELDA - 16, TAMANO_CELDA - 16);
        g.setColor(Color.BLACK);
        g.drawOval(x + 8, y + 8, TAMANO_CELDA - 16, TAMANO_CELDA - 16);
    }
    
    public static void dibujarZonaSegura(Graphics g, int x, int y, String tipo) {
    g.setColor(new Color(0, 255, 0, 100));
    g.fillRect(x, y, TAMANO_CELDA, TAMANO_CELDA);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, TAMANO_CELDA, TAMANO_CELDA);
    
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 16));
    if (tipo.equals(Zona.TIPO_INICIO)) {
        g.drawString("S", x + 15, y + 28);
    } else if (tipo.equals(Zona.TIPO_FINAL)) {
        g.drawString("F", x + 15, y + 28);  // ← Verifica que sea "F"
    } else {
        g.drawString("I", x + 15, y + 28);
    }
}
    
    public static int getTamanoCelda() {
        return TAMANO_CELDA;
    }
}