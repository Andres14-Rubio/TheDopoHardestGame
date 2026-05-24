package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import dominio.*;

public class PanelJuego extends JPanel {

    private Juego juego;
    private int tamCelda = 35;

    public PanelJuego(Juego juego) {
        this.juego = juego;
        setPreferredSize(new Dimension(900, 600));
        setBackground(Color.WHITE);
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (juego == null || juego.getNivelActual() == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Cargando juego...", 350, 300);
            return;
        }
        Nivel nivel = juego.getNivelActual();
        Configuracion config = nivel.getConfiguracion();

        dibujarGrid(g, config);
        dibujarParedes(g, config);
        dibujarZonas(g, config);
        dibujarMonedas(g, nivel);          // ← usa getMonedas()
        dibujarFuentesVida(g, nivel);
        dibujarBombas(g, nivel);
        dibujarEnemigos(g, nivel);
        dibujarJugador1(g, nivel);
        if (juego.getModalidad().equals("PVP") || juego.getModalidad().equals("PVM")) {
            dibujarJugador2(g, nivel);
        }
    }

    private void dibujarGrid(Graphics g, Configuracion config) {
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= config.getAncho(); i++) {
            g.drawLine(i * tamCelda, 0, i * tamCelda, config.getAlto() * tamCelda);
        }
        for (int j = 0; j <= config.getAlto(); j++) {
            g.drawLine(0, j * tamCelda, config.getAncho() * tamCelda, j * tamCelda);
        }
    }

    private void dibujarParedes(Graphics g, Configuracion config) {
        for (int[] pared : config.getParedes()) {
            int px = pared[0] * tamCelda;
            int py = pared[1] * tamCelda;
            g.setColor(new Color(80, 80, 80));
            g.fillRect(px, py, tamCelda, tamCelda);
            g.setColor(new Color(60, 60, 60));
            g.drawRect(px, py, tamCelda, tamCelda);
        }
    }

    private void dibujarZonas(Graphics g, Configuracion config) {
        for (Zona zona : config.getZonas()) {
            if (zona.esZonaSegura()) {
                int zx = zona.getX() * tamCelda;
                int zy = zona.getY() * tamCelda;
                g.setColor(new Color(0, 200, 0, 100));
                g.fillRect(zx, zy, tamCelda, tamCelda);
                g.setColor(new Color(0, 150, 0));
                g.drawRect(zx, zy, tamCelda, tamCelda);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                if (zona.esZonaInicio()) {
                    g.drawString("S", zx + 12, zy + 23);
                } else if (zona.esZonaFinal()) {
                    g.drawString("F", zx + 12, zy + 23);
                } else {
                    g.drawString("I", zx + 12, zy + 23);
                }
            }
        }
    }

    // ========== MONEDAS COMPARTIDAS ==========
    private void dibujarMonedas(Graphics g, Nivel nivel) {
        List<Moneda> monedas = nivel.getMonedas();   // única lista
        for (Moneda m : monedas) {
            if (m.estaRecolectada()) continue;       // por si acaso
            int x = m.getX() * tamCelda;
            int y = m.getY() * tamCelda;

            if (m instanceof MonedaSkin) {
                MonedaSkin ms = (MonedaSkin) m;
                Color colorSkin, colorBrillo;
                switch (ms.getSkinOtorgado()) {
                    case "Rojo":
                        colorSkin  = new Color(220, 50, 50);
                        colorBrillo = new Color(255, 150, 150);
                        break;
                    case "Azul":
                        colorSkin  = new Color(50, 50, 220);
                        colorBrillo = new Color(150, 150, 255);
                        break;
                    case "Verde":
                        colorSkin  = new Color(50, 180, 50);
                        colorBrillo = new Color(150, 255, 150);
                        break;
                    default:
                        colorSkin  = Color.WHITE;
                        colorBrillo = Color.LIGHT_GRAY;
                }
                g.setColor(colorSkin);
                g.fillOval(x + 7, y + 7, tamCelda - 14, tamCelda - 14);
                g.setColor(colorBrillo);
                g.fillOval(x + 11, y + 11, tamCelda - 22, tamCelda - 22);
                g.setColor(Color.BLACK);
                g.drawOval(x + 7, y + 7, tamCelda - 14, tamCelda - 14);
                g.setFont(new Font("Arial", Font.BOLD, 11));
                g.setColor(Color.WHITE);
                g.drawString("S", x + tamCelda/2 - 4, y + tamCelda/2 + 4);
            } else {
                // Moneda normal
                g.setColor(new Color(255, 215, 0));
                g.fillOval(x + 10, y + 10, tamCelda - 20, tamCelda - 20);
                g.setColor(new Color(255, 255, 150));
                g.fillOval(x + 14, y + 14, tamCelda - 28, tamCelda - 28);
                g.setColor(Color.BLACK);
                g.drawOval(x + 10, y + 10, tamCelda - 20, tamCelda - 20);
            }
        }
    }

    private void dibujarFuentesVida(Graphics g, Nivel nivel) {
        for (FuenteVida fv : nivel.getFuentesVida()) {
            int x = fv.getX() * tamCelda;
            int y = fv.getY() * tamCelda;
            g.setColor(new Color(255, 100, 255));
            g.fillRect(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, tamCelda - 15));
            g.drawString("❤️", x + 8, y + tamCelda - 12);
        }
    }

    private void dibujarBombas(Graphics g, Nivel nivel) {
        for (Bomba bomba : nivel.getBombas()) {
            int x = bomba.getX() * tamCelda;
            int y = bomba.getY() * tamCelda;
            g.setColor(Color.BLACK);
            g.fillOval(x + 8, y + 8, tamCelda - 16, tamCelda - 16);
            g.setColor(Color.RED);
            g.fillOval(x + 11, y + 11, tamCelda - 22, tamCelda - 22);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, tamCelda - 20));
            g.drawString("💣", x + 9, y + tamCelda - 12);
        }
    }

    private void dibujarEnemigos(Graphics g, Nivel nivel) {
        for (Enemigo enemigo : nivel.getEnemigosActivos()) {
            int x = enemigo.getX() * tamCelda;
            int y = enemigo.getY() * tamCelda;
            if (enemigo instanceof EnemigoRapido) {
                g.setColor(new Color(200, 0, 0));
                g.fillOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
                g.setColor(Color.YELLOW);
                g.drawLine(x + 10, y + 15, x + 25, y + 15);
                g.drawLine(x + 17, y + 8,  x + 17, y + 22);
                g.setColor(Color.BLACK);
                g.drawOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
            } else if (enemigo instanceof EnemigoPatrullero) {
                g.setColor(new Color(128, 0, 128));
                g.fillOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
                g.setColor(Color.WHITE);
                g.drawArc(x + 8, y + 8, tamCelda - 16, tamCelda - 16, 0, 360);
                g.setColor(Color.BLACK);
                g.drawOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
            } else if (enemigo instanceof EnemigoPerseguidor) {
                g.setColor(new Color(255, 140, 0));
                g.fillOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
                g.setColor(Color.BLACK);
                g.fillOval(x + 12, y + 12, 5, 5);
                g.fillOval(x + 22, y + 12, 5, 5);
                g.drawOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
            } else {
                g.setColor(new Color(0, 0, 255));
                g.fillOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
                g.setColor(Color.BLACK);
                g.drawOval(x + 5, y + 5, tamCelda - 10, tamCelda - 10);
            }
        }
    }

    private void dibujarJugador1(Graphics g, Nivel nivel) {
        Jugador j1 = nivel.getJugador1();
        int totalMonedas = nivel.getTotalMonedas();
        int tamanio = (int)(tamCelda * j1.getTamanio());
        int offset  = (tamCelda - tamanio) / 2;
        int x = j1.getX() * tamCelda + offset;
        int y = j1.getY() * tamCelda + offset;

        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(x + 2, y + 2, tamanio, tamanio);
        dibujarCuerpoJugador(g, j1, x, y, tamanio);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(j1.getColorBorde());
        g2.drawRect(x, y, tamanio, tamanio);
        g2.setStroke(new BasicStroke(1));
        dibujarIndicadorEscudo(g, j1, x, y, tamanio);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("1", x + tamanio / 2 - 3, y + tamanio / 2 + 4);
        dibujarBarraMonedas(g, j1.getMonedasRecolectadas(), totalMonedas, x, y, tamanio);
    }

    private void dibujarJugador2(Graphics g, Nivel nivel) {
        Jugador j2 = nivel.getJugador2();
        if (j2 == null) return;
        int totalMonedas = nivel.getTotalMonedas();
        int tamanio = (int)(tamCelda * j2.getTamanio());
        int offset  = (tamCelda - tamanio) / 2;
        int x = j2.getX() * tamCelda + offset;
        int y = j2.getY() * tamCelda + offset;

        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(x + 2, y + 2, tamanio, tamanio);
        dibujarCuerpoJugador(g, j2, x, y, tamanio);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(j2.getColorBorde());
        g2.drawRect(x, y, tamanio, tamanio);
        g2.setStroke(new BasicStroke(1));
        dibujarIndicadorEscudo(g, j2, x, y, tamanio);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("2", x + tamanio / 2 - 3, y + tamanio / 2 + 4);
        dibujarBarraMonedas(g, j2.getMonedasRecolectadas(), totalMonedas, x, y, tamanio);
    }

    private void dibujarCuerpoJugador(Graphics g, Jugador jugador, int x, int y, int tamanio) {
        String skinActual = jugador.isEfectoActivo() && jugador.getSkinTemporal() != null
                ? jugador.getSkinTemporal()
                : jugador.getSkin();
        switch (skinActual) {
            case "Rojo":
                g.setColor(new Color(220, 0, 0));
                g.fillRect(x, y, tamanio, tamanio);
                g.setColor(new Color(255, 100, 100));
                g.fillRect(x + 2, y + 2, tamanio - 4, tamanio - 4);
                break;
            case "Azul":
                g.setColor(new Color(0, 0, 200));
                g.fillRect(x, y, tamanio, tamanio);
                g.setColor(new Color(100, 100, 255));
                g.fillRect(x + 2, y + 2, tamanio - 4, tamanio - 4);
                break;
            case "Verde":
                g.setColor(new Color(0, 180, 0));
                g.fillRect(x, y, tamanio, tamanio);
                g.setColor(new Color(100, 255, 100));
                g.fillRect(x + 2, y + 2, tamanio - 4, tamanio - 4);
                break;
            default:
                g.setColor(new Color(220, 0, 0));
                g.fillRect(x, y, tamanio, tamanio);
                g.setColor(new Color(255, 100, 100));
                g.fillRect(x + 2, y + 2, tamanio - 4, tamanio - 4);
        }
        if (jugador.isEfectoActivo()) {
            g.setColor(new Color(255, 255, 0, 200));
            g.fillOval(x + tamanio - 10, y + 2, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(x + tamanio - 10, y + 2, 8, 8);
        }
    }

    private void dibujarIndicadorEscudo(Graphics g, Jugador jugador, int x, int y, int tamanio) {
        if (!jugador.getSkin().equals("Verde")) return;
        if (jugador.isEscudoActivo()) {
            g.setColor(new Color(0, 255, 0, 180));
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawOval(x - 3, y - 3, tamanio + 6, tamanio + 6);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            g.setColor(new Color(0, 200, 0));
            g.drawString("🛡️", x - 8, y - 2);
        } else if (jugador.isEscudoUsado()) {
            g.setColor(new Color(255, 165, 0, 150));
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawOval(x - 3, y - 3, tamanio + 6, tamanio + 6);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            g.setColor(new Color(255, 140, 0));
            g.drawString("🐢", x - 8, y - 2);
        }
    }

    private void dibujarBarraMonedas(Graphics g, int recolectadas, int total, int x, int y, int tamanio) {
        int progreso = total > 0 ? (int)((double) recolectadas / total * tamanio) : 0;
        g.setColor(new Color(60, 60, 60));
        g.fillRect(x, y - 8, tamanio, 4);
        g.setColor(new Color(0, 200, 0));
        g.fillRect(x, y - 8, progreso, 4);
        g.setColor(Color.BLACK);
        g.drawRect(x, y - 8, tamanio, 4);
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
        repaint();
    }

    public void setTamCelda(int tamCelda) {
        this.tamCelda = tamCelda;
        if (juego != null && juego.getNivelActual() != null) {
            setPreferredSize(new Dimension(
                    juego.getNivelActual().getConfiguracion().getAncho() * tamCelda,
                    juego.getNivelActual().getConfiguracion().getAlto() * tamCelda));
        }
        repaint();
    }
}