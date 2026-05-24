package dominio;

import java.util.*;

/**
 * MaquinaExperta - Perfil de IA que usa BFS (Búsqueda en Anchura) para encontrar
 * el camino más corto hacia las monedas pendientes y, al terminarlas, hacia la zona final.
 *
 * Estrategia:
 *   1. Si quedan monedas: BFS hacia la moneda más cercana.
 *   2. Si ya recolectó todas: BFS hacia la zona segura final.
 *   3. Si BFS no encuentra camino (bloqueado por enemigos, etc.): retrocede aleatoriamente.
 */
public class MaquinaExperta implements PerfilMaquina {

    private static final int[][] DIRECCIONES = {
            {0, -1}, // Norte
            {0,  1}, // Sur
            {1,  0}, // Este
            {-1, 0}  // Oeste
    };

    private final Random random = new Random();

    @Override
    public int[] decidirMovimiento(Jugador jugador, Configuracion config, List<Moneda> monedasPendientes) {
        int ox = jugador.getX();
        int oy = jugador.getY();

        // Elegir objetivo: moneda más cercana o zona final
        int[] objetivo = encontrarObjetivo(ox, oy, config, monedasPendientes);
        if (objetivo == null) return new int[]{0, 0};

        // BFS desde posición actual hacia el objetivo
        int[] primerPaso = bfs(ox, oy, objetivo[0], objetivo[1], config);
        if (primerPaso != null) return primerPaso;

        // Si BFS falla, moverse aleatoriamente
        return moverAleatorio(ox, oy, config);
    }

    // --------------------------------------------------
    // Encontrar objetivo
    // --------------------------------------------------

    private int[] encontrarObjetivo(int x, int y, Configuracion config, List<Moneda> monedasPendientes) {
        if (!monedasPendientes.isEmpty()) {
            // Moneda más cercana (distancia Manhattan)
            Moneda cercana = null;
            int menorDist = Integer.MAX_VALUE;
            for (Moneda m : monedasPendientes) {
                int dist = Math.abs(m.getX() - x) + Math.abs(m.getY() - y);
                if (dist < menorDist) {
                    menorDist = dist;
                    cercana = m;
                }
            }
            if (cercana != null) return new int[]{cercana.getX(), cercana.getY()};
        }

        // Zona final
        Zona zonaFinal = config.getZonaFinal();
        if (zonaFinal != null) return new int[]{zonaFinal.getX(), zonaFinal.getY()};

        return null;
    }

    // --------------------------------------------------
    // BFS
    // --------------------------------------------------

    private int[] bfs(int sx, int sy, int tx, int ty, Configuracion config) {
        int ancho = config.getAncho();
        int alto  = config.getAlto();

        if (sx == tx && sy == ty) return new int[]{0, 0};

        boolean[][] visitado = new boolean[ancho][alto];
        // padre[x][y] = {px, py, dx, dy} — celda padre + dirección tomada
        int[][][][] padre = new int[ancho][alto][2][2]; // [x][y] → {parentX, parentY} y {dx, dy}
        // Simplificamos: guardamos la dirección desde el origen para cada celda
        int[][][] dirDesdeOrigen = new int[ancho][alto][];

        Queue<int[]> cola = new LinkedList<>();
        cola.add(new int[]{sx, sy});
        visitado[sx][sy] = true;

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int ax = actual[0], ay = actual[1];

            for (int[] dir : DIRECCIONES) {
                int nx = ax + dir[0];
                int ny = ay + dir[1];

                if (!esPosicionValida(nx, ny, config)) continue;
                if (visitado[nx][ny]) continue;

                visitado[nx][ny] = true;

                // Primer paso desde el origen
                if (ax == sx && ay == sy) {
                    dirDesdeOrigen[nx][ny] = dir;
                } else {
                    dirDesdeOrigen[nx][ny] = dirDesdeOrigen[ax][ay];
                }

                if (nx == tx && ny == ty) {
                    return dirDesdeOrigen[nx][ny];
                }

                cola.add(new int[]{nx, ny});
            }
        }

        return null; // No encontró camino
    }

    // --------------------------------------------------
    // Fallback aleatorio
    // --------------------------------------------------

    private int[] moverAleatorio(int x, int y, Configuracion config) {
        List<int[]> validas = new ArrayList<>();
        for (int[] dir : DIRECCIONES) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (esPosicionValida(nx, ny, config)) validas.add(dir);
        }
        if (validas.isEmpty()) return new int[]{0, 0};
        return validas.get(random.nextInt(validas.size()));
    }

    // --------------------------------------------------
    // Validación de posición
    // --------------------------------------------------

    private boolean esPosicionValida(int x, int y, Configuracion config) {
        if (x < 0 || x >= config.getAncho() || y < 0 || y >= config.getAlto()) return false;
        for (int[] pared : config.getParedes()) {
            if (pared[0] == x && pared[1] == y) return false;
        }
        return true;
    }

    @Override
    public String getNombre() {
        return "Experta";
    }
}
