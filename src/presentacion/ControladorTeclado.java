package presentacion;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import dominio.*;

/**
 * ControladorTeclado - Maneja la entrada del teclado para J1 y J2.
 *
 * Movimiento J1 (WASD):
 *   W/S/A/D solos     → cardinal (norte/sur/oeste/este)
 *   W+D / W+A         → diagonal norte-este / norte-oeste
 *   S+D / S+A         → diagonal sur-este  / sur-oeste
 *
 * Movimiento J2 (flechas, solo modo PVP):
 *   ↑ ↓ ← → solos     → cardinal
 *   ↑+→ / ↑+←         → diagonal norte-este / norte-oeste
 *   ↓+→ / ↓+←         → diagonal sur-este  / sur-oeste
 */
public class ControladorTeclado implements KeyListener {

    private Juego juego;

    // Teclas actualmente presionadas
    private final Set<Integer> teclasPresionadas = new HashSet<>();

    public ControladorTeclado(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        teclasPresionadas.add(key);

        if (juego == null || !juego.isCorriendo() || juego.isPausado()) return;

        // ESC para pausar
        if (key == KeyEvent.VK_ESCAPE) {
            juego.pausarJuego();
            return;
        }

        // ==========================================
        // JUGADOR 1 — WASD (cardinal + diagonal)
        // ==========================================
        boolean w = teclasPresionadas.contains(KeyEvent.VK_W);
        boolean s = teclasPresionadas.contains(KeyEvent.VK_S);
        boolean a = teclasPresionadas.contains(KeyEvent.VK_A);
        boolean d = teclasPresionadas.contains(KeyEvent.VK_D);

        boolean teclaJ1 = w || s || a || d;

        if (teclaJ1) {
            int dx1 = 0, dy1 = 0;
            if (w) dy1 = -1;
            if (s) dy1 =  1;
            if (a) dx1 = -1;
            if (d) dx1 =  1;
            juego.moverJugador1(dx1, dy1);
            return;
        }

        // ==========================================
        // JUGADOR 2 — Flechas (cardinal + diagonal), solo PVP
        // ==========================================
        if (juego.getModalidad().equals("PVP")) {
            boolean up    = teclasPresionadas.contains(KeyEvent.VK_UP);
            boolean down  = teclasPresionadas.contains(KeyEvent.VK_DOWN);
            boolean left  = teclasPresionadas.contains(KeyEvent.VK_LEFT);
            boolean right = teclasPresionadas.contains(KeyEvent.VK_RIGHT);

            boolean teclaJ2 = up || down || left || right;

            if (teclaJ2) {
                int dx2 = 0, dy2 = 0;
                if (up)    dy2 = -1;
                if (down)  dy2 =  1;
                if (left)  dx2 = -1;
                if (right) dx2 =  1;
                juego.moverJugador2(dx2, dy2);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPresionadas.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita implementación
    }
}
