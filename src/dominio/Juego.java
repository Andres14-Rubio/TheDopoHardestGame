package dominio;

public class Juego {

    private Nivel nivelActual;
    private String modalidad;
    private boolean corriendo;
    private boolean pausado;
    private String configArchivo;
    private PerfilMaquina perfilMaquina;

    public Juego(String modalidad) {
        this.modalidad = modalidad;
        this.corriendo = false;
        this.pausado = false;
    }

    public Juego(String modalidad, PerfilMaquina perfilMaquina) {
        this(modalidad);
        this.perfilMaquina = perfilMaquina;
    }

    public void iniciarJuego(Configuracion config, String skinJ1, String skinJ2) {
        this.configArchivo = config.getNombreArchivo();
        Zona zonaInicio = config.getZonaInicio();
        Jugador jugador1 = new Jugador(zonaInicio.getX(), zonaInicio.getY(), skinJ1);
        Jugador jugador2 = null;
        boolean necesitaJ2 = modalidad.equals("PVP") || modalidad.equals("PVM");
        if (necesitaJ2) {
            Zona zonaInicioJ2 = null;
            for (Zona z : config.getZonas()) {
                if (z.getTipo().equals(Zona.TIPO_INICIO) && z != zonaInicio) {
                    zonaInicioJ2 = z;
                    break;
                }
            }
            if (zonaInicioJ2 == null && zonaInicio != null) {
                int nuevaX = Math.min(zonaInicio.getX() + 8, config.getAncho() - 2);
                zonaInicioJ2 = new Zona(nuevaX, zonaInicio.getY(), Zona.TIPO_INICIO);
            }
            String skinMaquina = (skinJ2 != null) ? skinJ2 : "Rojo";
            if (zonaInicioJ2 != null) {
                jugador2 = new Jugador(zonaInicioJ2.getX(), zonaInicioJ2.getY(), skinMaquina);
            } else {
                jugador2 = new Jugador(1, 1, skinMaquina);
            }
        }
        if (necesitaJ2) {
            this.nivelActual = new Nivel(config, jugador1, jugador2, modalidad);
        } else {
            this.nivelActual = new Nivel(config, jugador1, modalidad);
        }
        this.nivelActual.iniciar();
        this.corriendo = true;
        this.pausado = false;
        ErrorLogger.getInstance().logError("Juego iniciado - Modo: " + modalidad +
                " | J1: " + skinJ1 +
                (jugador2 != null ? " | J2: " + jugador2.getSkin() : "") +
                (perfilMaquina != null ? " | IA: " + perfilMaquina.getNombre() : ""));
    }

    public void moverJugador1(int dx, int dy) {
        if (!corriendo || pausado) return;
        if (nivelActual == null) return;
        if (!nivelActual.getEstado().equals(Nivel.ESTADO_EN_CURSO)) return;
        Jugador jugador = nivelActual.getJugador1();
        Configuracion config = nivelActual.getConfiguracion();
        int nuevaX = jugador.getX() + dx;
        int nuevaY = jugador.getY() + dy;
        if (nuevaX < 0 || nuevaX >= config.getAncho() || nuevaY < 0 || nuevaY >= config.getAlto()) return;
        if (esPared(nuevaX, nuevaY, config)) return;
        if (dx != 0 && dy != 0) {
            if (esPared(jugador.getX() + dx, jugador.getY(), config)) return;
            if (esPared(jugador.getX(), jugador.getY() + dy, config)) return;
        }
        jugador.mover(dx, dy);
        nivelActual.verificarColisiones();
    }

    public void moverJugador2(int dx, int dy) {
        if (!modalidad.equals("PVP")) return;
        if (!corriendo || pausado) return;
        if (nivelActual == null) return;
        if (!nivelActual.getEstado().equals(Nivel.ESTADO_EN_CURSO)) return;
        Jugador jugador = nivelActual.getJugador2();
        if (jugador == null) return;
        Configuracion config = nivelActual.getConfiguracion();
        int nuevaX = jugador.getX() + dx;
        int nuevaY = jugador.getY() + dy;
        if (nuevaX < 0 || nuevaX >= config.getAncho() || nuevaY < 0 || nuevaY >= config.getAlto()) return;
        if (esPared(nuevaX, nuevaY, config)) return;
        if (dx != 0 && dy != 0) {
            if (esPared(jugador.getX() + dx, jugador.getY(), config)) return;
            if (esPared(jugador.getX(), jugador.getY() + dy, config)) return;
        }
        jugador.mover(dx, dy);
        nivelActual.verificarColisiones();
    }

    private boolean esPared(int x, int y, Configuracion config) {
        for (int[] pared : config.getParedes()) {
            if (pared[0] == x && pared[1] == y) return true;
        }
        return false;
    }

    public void moverMaquina() {
        if (!modalidad.equals("PVM")) return;
        if (!corriendo || pausado) return;
        if (nivelActual == null || perfilMaquina == null) return;
        if (!nivelActual.getEstado().equals(Nivel.ESTADO_EN_CURSO)) return;
        Jugador maquina = nivelActual.getJugador2();
        if (maquina == null) return;
        Configuracion config = nivelActual.getConfiguracion();
        // Lista de monedas aún no recolectadas por NADIE (compartidas)
        int[] movimiento = perfilMaquina.decidirMovimiento(maquina, config, nivelActual.getMonedas());
        int dx = movimiento[0];
        int dy = movimiento[1];
        int nuevaX = maquina.getX() + dx;
        int nuevaY = maquina.getY() + dy;
        if (nuevaX < 0 || nuevaX >= config.getAncho() || nuevaY < 0 || nuevaY >= config.getAlto()) return;
        for (int[] pared : config.getParedes()) {
            if (pared[0] == nuevaX && pared[1] == nuevaY) return;
        }
        maquina.mover(dx, dy);
        nivelActual.verificarColisiones();
    }

    public void actualizarTiempo() {
        if (corriendo && !pausado && nivelActual != null) {
            if (nivelActual.getEstado().equals(Nivel.ESTADO_EN_CURSO)) {
                nivelActual.setTiempoRestante(nivelActual.getTiempoRestante() - 1);
                nivelActual.verificarTiempoAgotado();
            }
        }
    }

    public void actualizarEnemigos() {
        if (nivelActual != null && corriendo && !pausado) {
            nivelActual.actualizarEnemigos();
            nivelActual.verificarColisiones();
        }
    }

    public void pausarJuego() {
        if (corriendo && !pausado) {
            this.pausado = true;
            if (nivelActual != null) nivelActual.pausar();
        }
    }

    public void reanudarJuego() {
        if (corriendo && pausado) {
            this.pausado = false;
            if (nivelActual != null) nivelActual.reanudar();
        }
    }

    public void terminarJuego() {
        this.corriendo = false;
        this.pausado = false;
    }

    public void guardarPartida(String rutaArchivo) throws JuegoException {
        if (nivelActual == null) throw new JuegoException("No hay partida en curso para guardar", null);
        EstadoPartida estado = EstadoPartida.capturar(this, configArchivo);
        estado.guardar(rutaArchivo);
    }

    // Getters
    public Nivel getNivelActual() { return nivelActual; }
    public String getModalidad() { return modalidad; }
    public boolean isCorriendo() { return corriendo; }
    public boolean isPausado() { return pausado; }
    public PerfilMaquina getPerfilMaquina() { return perfilMaquina; }
    public String getConfigArchivo() { return configArchivo; }
    public int getTiempoRestante() { return nivelActual != null ? nivelActual.getTiempoRestante() : 0; }
    public int getMuertes() { return nivelActual != null ? nivelActual.getJugador1().getMuertes() : 0; }
    public int getMuertesJugador2() { return (nivelActual != null && nivelActual.getJugador2() != null) ? nivelActual.getJugador2().getMuertes() : 0; }
    public int getMonedasRecolectadas() { return nivelActual != null ? nivelActual.getJugador1().getMonedasRecolectadas() : 0; }
    public int getMonedasRecolectadasJugador2() { return (nivelActual != null && nivelActual.getJugador2() != null) ? nivelActual.getJugador2().getMonedasRecolectadas() : 0; }
    public int getTotalMonedas() { return nivelActual != null ? nivelActual.getTotalMonedas() : 0; }
    public String getEstadoNivel() { return nivelActual != null ? nivelActual.getEstado() : null; }
    public String getGanador() { return nivelActual != null ? nivelActual.getGanador() : null; }
}