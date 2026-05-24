package dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MaquinaAleatoria - Perfil de máquina que mueve al jugador de forma aleatoria.
 * En cada turno elige una dirección válida (que no sea pared ni borde) al azar.
 * Si no hay movimiento válido, se queda quieta.
 */
public class MaquinaAleatoria implements PerfilMaquina {

    private static final int[][] DIRECCIONES = {
            {0, -1}, // Norte
            {0,  1}, // Sur
            {1,  0}, // Este
            {-1, 0}  // Oeste
    };

    private final Random random = new Random();

    @Override
    public int[] decidirMovimiento(Jugador jugador, Configuracion config, List<Moneda> monedasPendientes) {
        List<int[]> validas = new ArrayList<>();

        for (int[] dir : DIRECCIONES) {
            int nx = jugador.getX() + dir[0];
            int ny = jugador.getY() + dir[1];
            if (esPosicionValida(nx, ny, config)) {
                validas.add(dir);
            }
        }

        if (validas.isEmpty()) return new int[]{0, 0};
        return validas.get(random.nextInt(validas.size()));
    }

    private boolean esPosicionValida(int x, int y, Configuracion config) {
        if (x < 0 || x >= config.getAncho() || y < 0 || y >= config.getAlto()) return false;
        for (int[] pared : config.getParedes()) {
            if (pared[0] == x && pared[1] == y) return false;
        }
        return true;
    }

    @Override
    public String getNombre() {
        return "Aleatoria";
    }
}
