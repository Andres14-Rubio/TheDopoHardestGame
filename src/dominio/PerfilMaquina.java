package dominio;

import java.util.List;

/**
 * Interfaz PerfilMaquina - Define el comportamiento de una IA que controla al jugador máquina.
 * Implementaciones: MaquinaAleatoria, MaquinaExperta.
 */
public interface PerfilMaquina {

    /**
     * Decide el movimiento (dx, dy) de la máquina en este turno.
     *
     * @param jugador          el jugador que controla la máquina
     * @param config           la configuración del nivel (mapa, paredes, zonas)
     * @param monedasPendientes monedas que aún no ha recolectado la máquina
     * @return int[]{dx, dy}
     */
    int[] decidirMovimiento(Jugador jugador, Configuracion config, List<Moneda> monedasPendientes);

    /**
     * @return nombre del perfil para mostrar en la UI
     */
    String getNombre();
}
