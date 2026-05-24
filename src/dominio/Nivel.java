package dominio;

import java.util.*;

public class Nivel {

    public static final String ESTADO_EN_CURSO = "EN_CURSO";
    public static final String ESTADO_GANADO = "GANADO";
    public static final String ESTADO_PERDIDO = "PERDIDO";
    public static final String ESTADO_PAUSADO = "PAUSADO";

    private Configuracion configuracion;
    private Jugador jugador1;
    private Jugador jugador2;
    private String modalidad;
    private List<Enemigo> enemigosActivos;
    private List<Moneda> monedasNivel;
    private List<FuenteVida> fuentesVida;
    private List<Bomba> bombas;
    private int tiempoRestante;
    private String estado;
    private String ganador;

    private Zona zonaReaparicionActualJ1;
    private Zona zonaReaparicionActualJ2;
    private int monedasGuardadasJ1;
    private int monedasGuardadasJ2;
    private boolean zonaIntermediaActivadaJ1;
    private boolean zonaIntermediaActivadaJ2;
    private boolean regenerandoJ1;
    private boolean regenerandoJ2;

    public Nivel(Configuracion configuracion, Jugador jugador1, String modalidad) {
        this(configuracion, jugador1, null, modalidad);
    }

    public Nivel(Configuracion configuracion, Jugador jugador1, Jugador jugador2, String modalidad) {
        this.configuracion = configuracion;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.modalidad = modalidad;
        this.enemigosActivos = new ArrayList<>();
        this.monedasNivel = new ArrayList<>();
        this.fuentesVida = new ArrayList<>();
        this.bombas = new ArrayList<>();
        this.tiempoRestante = configuracion.getTiempoLimite();
        this.estado = ESTADO_EN_CURSO;
        this.ganador = null;

        this.monedasGuardadasJ1 = 0;
        this.monedasGuardadasJ2 = 0;
        this.zonaIntermediaActivadaJ1 = false;
        this.zonaIntermediaActivadaJ2 = false;
        this.regenerandoJ1 = false;
        this.regenerandoJ2 = false;

        // Copiar enemigos
        for (Enemigo e : configuracion.getEnemigos()) {
            this.enemigosActivos.add(e);
        }

        // Copiar monedas (compartidas)
        for (Moneda m : configuracion.getMonedas()) {
            if (m instanceof MonedaSkin) {
                MonedaSkin ms = (MonedaSkin) m;
                this.monedasNivel.add(new MonedaSkin(ms.getX(), ms.getY(), ms.getSkinOtorgado(), ms.getDuracion()));
            } else {
                this.monedasNivel.add(new Moneda(m.getX(), m.getY()));
            }
        }

        // Copiar fuentes de vida
        for (FuenteVida fv : configuracion.getFuentesVida()) {
            this.fuentesVida.add(new FuenteVida(fv.getX(), fv.getY(), 1, 1));
        }

        // Copiar bombas
        for (Bomba b : configuracion.getBombas()) {
            this.bombas.add(new Bomba(b.getX(), b.getY(), 1, 1));
        }

        // Establecer zonas de reaparición
        Zona zonaInicio = configuracion.getZonaInicio();
        if (zonaInicio != null) {
            this.zonaReaparicionActualJ1 = zonaInicio;
            this.jugador1.setX(zonaInicio.getX());
            this.jugador1.setY(zonaInicio.getY());
        }

        if ((modalidad.equals("PVP") || modalidad.equals("PVM")) && jugador2 != null) {
            Zona zonaInicioJ2 = null;
            for (Zona z : configuracion.getZonas()) {
                if (z.getTipo().equals(Zona.TIPO_INICIO) && z != zonaInicio) {
                    zonaInicioJ2 = z;
                    break;
                }
            }
            if (zonaInicioJ2 == null && zonaInicio != null) {
                int nuevaX = Math.min(zonaInicio.getX() + 8, configuracion.getAncho() - 2);
                zonaInicioJ2 = new Zona(nuevaX, zonaInicio.getY(), Zona.TIPO_INICIO);
            }
            this.zonaReaparicionActualJ2 = zonaInicioJ2;
            if (zonaInicioJ2 != null) {
                this.jugador2.setX(zonaInicioJ2.getX());
                this.jugador2.setY(zonaInicioJ2.getY());
            } else {
                this.jugador2.setX(1);
                this.jugador2.setY(1);
                this.zonaReaparicionActualJ2 = new Zona(1, 1, Zona.TIPO_INICIO);
            }
        }

        inicializarEnemigosPerseguidores();
    }

    private void inicializarEnemigosPerseguidores() {
        for (Enemigo e : enemigosActivos) {
            if (e instanceof EnemigoPerseguidor) {
                EnemigoPerseguidor ep = (EnemigoPerseguidor) e;
                if (modalidad.equals("PVP") && jugador2 != null) {
                    double distJ1 = Math.hypot(ep.getX() - jugador1.getX(), ep.getY() - jugador1.getY());
                    double distJ2 = Math.hypot(ep.getX() - jugador2.getX(), ep.getY() - jugador2.getY());
                    ep.setObjetivo(distJ1 < distJ2 ? jugador1 : jugador2);
                } else {
                    ep.setObjetivo(jugador1);
                }
            }
        }
    }

    public void iniciar() {
        this.estado = ESTADO_EN_CURSO;
        ErrorLogger.getInstance().logError("Nivel iniciado: " + configuracion.getNombreNivel());
    }

    public void actualizarEnemigos() {
        if (!estado.equals(ESTADO_EN_CURSO)) return;
        for (Enemigo e : enemigosActivos) {
            e.mover();
        }
        // Actualizar objetivos de perseguidores
        for (Enemigo e : enemigosActivos) {
            if (e instanceof EnemigoPerseguidor) {
                EnemigoPerseguidor ep = (EnemigoPerseguidor) e;
                if (modalidad.equals("PVP") && jugador2 != null) {
                    double distJ1 = Math.hypot(ep.getX() - jugador1.getX(), ep.getY() - jugador1.getY());
                    double distJ2 = Math.hypot(ep.getX() - jugador2.getX(), ep.getY() - jugador2.getY());
                    ep.setObjetivo(distJ1 < distJ2 ? jugador1 : jugador2);
                } else {
                    ep.setObjetivo(jugador1);
                }
            }
        }
    }

    public void verificarColisiones() {
        // Colisiones con enemigos
        for (Enemigo e : enemigosActivos) {
            if (jugador1.colisionaCon(e)) {
                manejarMuerteJ1();
                return;
            }
        }
        if ((modalidad.equals("PVP") || modalidad.equals("PVM")) && jugador2 != null) {
            for (Enemigo e : enemigosActivos) {
                if (jugador2.colisionaCon(e)) {
                    manejarMuerteJ2();
                    return;
                }
            }
        }

        // Colisión entre jugadores
        if ((modalidad.equals("PVP") || modalidad.equals("PVM")) && jugador2 != null && jugador1.colisionaCon(jugador2)) {
            manejarMuerteJ1();
            manejarMuerteJ2();
            return;
        }

        // Recolección de monedas (compartidas)
        Iterator<Moneda> itMonedas = monedasNivel.iterator();
        while (itMonedas.hasNext()) {
            Moneda m = itMonedas.next();
            if (m.estaRecolectada()) {
                itMonedas.remove();
                continue;
            }
            boolean recolectada = false;
            if (jugador1.colisionaCon(m)) {
                jugador1.recolectarMoneda();
                recolectada = true;
                if (m instanceof MonedaSkin) {
                    jugador1.aplicarSkinTemporal(((MonedaSkin)m).getSkinOtorgado(), ((MonedaSkin)m).getDuracion());
                    ErrorLogger.getInstance().logError("J1 recogió MonedaSkin");
                }
            } else if (jugador2 != null && jugador2.colisionaCon(m)) {
                jugador2.recolectarMoneda();
                recolectada = true;
                if (m instanceof MonedaSkin) {
                    jugador2.aplicarSkinTemporal(((MonedaSkin)m).getSkinOtorgado(), ((MonedaSkin)m).getDuracion());
                    ErrorLogger.getInstance().logError("J2 recogió MonedaSkin");
                }
            }
            if (recolectada) {
                m.recolectar();
                itMonedas.remove();
            }
        }

        // Fuentes de vida
        Iterator<FuenteVida> itFuente = fuentesVida.iterator();
        while (itFuente.hasNext()) {
            FuenteVida fv = itFuente.next();
            if (fv.isUsada()) {
                itFuente.remove();
                continue;
            }
            if (jugador1.colisionaCon(fv)) {
                fv.usar();
                jugador1.setVidasExtra(jugador1.getVidasExtra() + 1);
                itFuente.remove();
            } else if (jugador2 != null && jugador2.colisionaCon(fv)) {
                fv.usar();
                jugador2.setVidasExtra(jugador2.getVidasExtra() + 1);
                itFuente.remove();
            }
        }

        // Bombas con jugadores
        for (Bomba bomba : bombas) {
            if (!bomba.isExplotada()) {
                if (jugador1.colisionaCon(bomba)) {
                    manejarMuerteJ1();
                    bomba.explotar();
                } else if (jugador2 != null && jugador2.colisionaCon(bomba)) {
                    manejarMuerteJ2();
                    bomba.explotar();
                }
            }
        }

        // Bombas destruyen enemigos
        Iterator<Enemigo> itEnemigo = enemigosActivos.iterator();
        while (itEnemigo.hasNext()) {
            Enemigo e = itEnemigo.next();
            for (Bomba bomba : bombas) {
                if (bomba.isExplotada()) continue;
                if (e.colisionaCon(bomba)) {
                    itEnemigo.remove();
                    break;
                }
            }
        }

        // Zonas seguras
        for (Zona z : configuracion.getZonas()) {
            if (jugador1.colisionaCon(z)) {
                if (z.getTipo().equals(Zona.TIPO_INTERMEDIA) && !zonaIntermediaActivadaJ1) {
                    zonaIntermediaActivadaJ1 = true;
                    monedasGuardadasJ1 = jugador1.getMonedasRecolectadas();
                    jugador1.restaurarEscudo();
                }
                if (z.esZonaSegura() && !z.equals(zonaReaparicionActualJ1) && !z.getTipo().equals(Zona.TIPO_FINAL)) {
                    zonaReaparicionActualJ1 = z;
                    jugador1.restaurarEscudo();
                }
            }
            if (jugador2 != null && jugador2.colisionaCon(z)) {
                if (z.getTipo().equals(Zona.TIPO_INTERMEDIA) && !zonaIntermediaActivadaJ2) {
                    zonaIntermediaActivadaJ2 = true;
                    monedasGuardadasJ2 = jugador2.getMonedasRecolectadas();
                    jugador2.restaurarEscudo();
                }
                if (z.esZonaSegura() && !z.equals(zonaReaparicionActualJ2) && !z.getTipo().equals(Zona.TIPO_FINAL)) {
                    zonaReaparicionActualJ2 = z;
                    jugador2.restaurarEscudo();
                }
            }
        }

        verificarVictoria();
    }

    private void manejarMuerteJ1() {
        if (regenerandoJ1) return;
        if (jugador1.recibirGolpe()) {
            regenerandoJ1 = true;
            regenerandoJ1 = false;
            return;
        }
        regenerandoJ1 = true;
        if (modalidad.equals("PLAYER")) {
            int monedasRestaurar = zonaIntermediaActivadaJ1 ? monedasGuardadasJ1 : 0;
            restaurarMonedasJ1(monedasRestaurar);
            jugador1.morir(zonaReaparicionActualJ1.getX(), zonaReaparicionActualJ1.getY());
            // Vuelve a asignar las monedas que se habían restaurado (porque morir las puso a 0)
            jugador1.setMonedasRecolectadas(monedasRestaurar);
        } else {
            jugador1.setMonedasRecolectadas(0);
            restaurarTodasLasMonedas();
            jugador1.morir(zonaReaparicionActualJ1.getX(), zonaReaparicionActualJ1.getY());
        }
        regenerandoJ1 = false;
    }

    private void manejarMuerteJ2() {
        if (regenerandoJ2) return;
        if (jugador2.recibirGolpe()) {
            regenerandoJ2 = true;
            regenerandoJ2 = false;
            return;
        }
        regenerandoJ2 = true;
        // En multijugador siempre se pierden todas
        jugador2.setMonedasRecolectadas(0);
        restaurarTodasLasMonedas();
        jugador2.morir(zonaReaparicionActualJ2.getX(), zonaReaparicionActualJ2.getY());
        regenerandoJ2 = false;
    }

    private void restaurarMonedasJ1(int cantidadMonedas) {
        monedasNivel.clear();
        for (Moneda m : configuracion.getMonedas()) {
            if (m instanceof MonedaSkin) {
                MonedaSkin ms = (MonedaSkin) m;
                monedasNivel.add(new MonedaSkin(ms.getX(), ms.getY(), ms.getSkinOtorgado(), ms.getDuracion()));
            } else {
                monedasNivel.add(new Moneda(m.getX(), m.getY()));
            }
        }
        int aRecolectar = cantidadMonedas;
        Iterator<Moneda> it = monedasNivel.iterator();
        while (it.hasNext() && aRecolectar > 0) {
            Moneda m = it.next();
            m.recolectar();
            it.remove();
            aRecolectar--;
        }
        jugador1.setMonedasRecolectadas(cantidadMonedas);
    }

    private void restaurarTodasLasMonedas() {
        monedasNivel.clear();
        for (Moneda m : configuracion.getMonedas()) {
            if (m instanceof MonedaSkin) {
                MonedaSkin ms = (MonedaSkin) m;
                monedasNivel.add(new MonedaSkin(ms.getX(), ms.getY(), ms.getSkinOtorgado(), ms.getDuracion()));
            } else {
                monedasNivel.add(new Moneda(m.getX(), m.getY()));
            }
        }
    }

    public void verificarVictoria() {
        int totalMonedas = configuracion.getMonedas().size();
        Zona zonaFinal = configuracion.getZonaFinal();

        if (modalidad.equals("PLAYER")) {
            if (jugador1.getMonedasRecolectadas() >= totalMonedas && zonaFinal != null && jugador1.colisionaCon(zonaFinal)) {
                this.estado = ESTADO_GANADO;
                this.ganador = "JUGADOR 1";
            }
        } else if ((modalidad.equals("PVP") || modalidad.equals("PVM")) && jugador2 != null) {
            boolean j1Completo = jugador1.getMonedasRecolectadas() >= totalMonedas && zonaFinal != null && jugador1.colisionaCon(zonaFinal);
            boolean j2Completo = jugador2.getMonedasRecolectadas() >= totalMonedas && zonaFinal != null && jugador2.colisionaCon(zonaFinal);
            if (j1Completo) {
                this.estado = ESTADO_GANADO;
                this.ganador = "JUGADOR 1";
            } else if (j2Completo) {
                this.estado = ESTADO_GANADO;
                this.ganador = modalidad.equals("PVM") ? "MÁQUINA" : "JUGADOR 2";
            }
        }
    }

    public void verificarTiempoAgotado() {
        if (tiempoRestante <= 0) this.estado = ESTADO_PERDIDO;
    }

    public void pausar() {
        if (estado.equals(ESTADO_EN_CURSO)) this.estado = ESTADO_PAUSADO;
    }

    public void reanudar() {
        if (estado.equals(ESTADO_PAUSADO)) this.estado = ESTADO_EN_CURSO;
    }

    public void restaurarEstado(int tiempoRestante, Zona reapJ1, Zona reapJ2,
                                boolean zonaInterJ1, int monedasGuardadasJ1,
                                boolean zonaInterJ2, int monedasGuardadasJ2,
                                List<int[]> monedasPendientes) {
        this.tiempoRestante = tiempoRestante;
        this.zonaReaparicionActualJ1 = reapJ1;
        this.zonaIntermediaActivadaJ1 = zonaInterJ1;
        this.monedasGuardadasJ1 = monedasGuardadasJ1;
        if (reapJ2 != null) {
            this.zonaReaparicionActualJ2 = reapJ2;
            this.zonaIntermediaActivadaJ2 = zonaInterJ2;
            this.monedasGuardadasJ2 = monedasGuardadasJ2;
        }
        this.monedasNivel.clear();
        for (int[] pos : monedasPendientes) {
            this.monedasNivel.add(new Moneda(pos[0], pos[1]));
        }
    }

    // Getters
    public Configuracion getConfiguracion() { return configuracion; }
    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }
    public List<Enemigo> getEnemigosActivos() { return enemigosActivos; }
    public List<Moneda> getMonedas() { return monedasNivel; }
    public List<FuenteVida> getFuentesVida() { return fuentesVida; }
    public List<Bomba> getBombas() { return bombas; }
    public int getTiempoRestante() { return tiempoRestante; }
    public void setTiempoRestante(int tiempo) { this.tiempoRestante = tiempo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getTotalMonedas() { return configuracion.getMonedas().size(); }
    public String getGanador() { return ganador; }
    public Zona getZonaReaparicionActualJ1() { return zonaReaparicionActualJ1; }
    public Zona getZonaReaparicionActualJ2() { return zonaReaparicionActualJ2; }
    public boolean isZonaIntermediaActivadaJ1() { return zonaIntermediaActivadaJ1; }
    public boolean isZonaIntermediaActivadaJ2() { return zonaIntermediaActivadaJ2; }
    public int getMonedasGuardadasJ1() { return monedasGuardadasJ1; }
    public int getMonedasGuardadasJ2() { return monedasGuardadasJ2; }
}