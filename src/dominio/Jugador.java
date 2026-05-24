package dominio;

import java.awt.Color;

public class Jugador extends Entidad implements Movible {

    public static final String SKIN_ROJO = "Rojo";
    public static final String SKIN_AZUL = "Azul";
    public static final String SKIN_VERDE = "Verde";

    private static final double VELOCIDAD_BASE_ROJO = 1.0;
    private static final double VELOCIDAD_BASE_AZUL = 1.5;
    private static final double VELOCIDAD_BASE_VERDE = 1.0;
    private static final double TAMANIO_BASE_ROJO = 1.0;
    private static final double TAMANIO_BASE_AZUL = 1.5;
    private static final double TAMANIO_BASE_VERDE = 1.0;

    private int muertes;
    private int monedasRecolectadas;
    private String skin;
    private double velocidad;
    private double tamanio;
    private int vidasExtra;
    private Color colorBorde;

    private boolean escudoActivo;
    private boolean escudoUsado;
    private double velocidadOriginal;

    private String skinTemporal;
    private int duracionSkinTemporal;
    private boolean efectoActivo;

    public Jugador(int x, int y) {
        this(x, y, SKIN_ROJO);
    }

    public Jugador(int x, int y, String skin) {
        super(x, y);
        this.muertes = 0;
        this.monedasRecolectadas = 0;
        this.skin = skin;
        this.vidasExtra = 0;
        this.skinTemporal = null;
        this.duracionSkinTemporal = 0;
        this.efectoActivo = false;

        if (skin.equals(SKIN_VERDE)) {
            this.escudoActivo = true;
            this.escudoUsado = false;
            this.velocidadOriginal = VELOCIDAD_BASE_VERDE;
        } else {
            this.escudoActivo = false;
            this.escudoUsado = false;
            this.velocidadOriginal = 1.0;
        }
        aplicarStatsPorSkin();
    }

    private void aplicarStatsPorSkin() {
        String skinActual = (skinTemporal != null && efectoActivo) ? skinTemporal : skin;
        switch (skinActual) {
            case SKIN_ROJO:
                this.velocidad = VELOCIDAD_BASE_ROJO;
                this.tamanio = TAMANIO_BASE_ROJO;
                this.colorBorde = Color.RED;
                break;
            case SKIN_AZUL:
                this.velocidad = VELOCIDAD_BASE_AZUL;
                this.tamanio = TAMANIO_BASE_AZUL;
                this.colorBorde = Color.BLUE;
                break;
            case SKIN_VERDE:
                this.velocidad = velocidadOriginal;
                this.tamanio = TAMANIO_BASE_VERDE;
                this.colorBorde = Color.GREEN;
                break;
            default:
                this.velocidad = VELOCIDAD_BASE_ROJO;
                this.tamanio = TAMANIO_BASE_ROJO;
                this.colorBorde = Color.RED;
        }
    }

    public boolean recibirGolpe() {
        if (vidasExtra > 0) {
            vidasExtra--;
            return true;
        }
        if (skin.equals(SKIN_VERDE) && escudoActivo && !escudoUsado) {
            escudoUsado = true;
            this.velocidad = 0.7;
            return true;
        }
        return false;
    }

    public void restaurarEscudo() {
        if (skin.equals(SKIN_VERDE)) {
            this.escudoUsado = false;
            this.velocidadOriginal = VELOCIDAD_BASE_VERDE;
            this.velocidad = VELOCIDAD_BASE_VERDE;
        }
    }

    public void aplicarSkinTemporal(String skinTemporal, int duracion) {
        this.skinTemporal = skinTemporal;
        this.duracionSkinTemporal = duracion;
        this.efectoActivo = true;
        aplicarStatsPorSkin();
    }

    public void actualizarEfectoTemporal() {
        if (efectoActivo) {
            duracionSkinTemporal--;
            if (duracionSkinTemporal <= 0) {
                skinTemporal = null;
                efectoActivo = false;
                aplicarStatsPorSkin();
            }
        }
    }

    public void mover(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public void mover() { }

    public void recolectarMoneda() {
        this.monedasRecolectadas++;
    }

    public void morir(int xReaparicion, int yReaparicion) {
        this.muertes++;
        this.x = xReaparicion;
        this.y = yReaparicion;
        this.monedasRecolectadas = 0;

        this.skinTemporal = null;
        this.efectoActivo = false;

        if (skin.equals(SKIN_VERDE)) {
            this.escudoUsado = false;
            this.velocidadOriginal = VELOCIDAD_BASE_VERDE;
            this.velocidad = VELOCIDAD_BASE_VERDE;
            this.escudoActivo = true;
        } else {
            this.escudoActivo = false;
        }
        aplicarStatsPorSkin();
    }

    public void reiniciar(int xInicio, int yInicio) {
        this.muertes = 0;
        this.monedasRecolectadas = 0;
        this.x = xInicio;
        this.y = yInicio;
        this.vidasExtra = 0;
        this.skinTemporal = null;
        this.efectoActivo = false;

        if (skin.equals(SKIN_VERDE)) {
            this.escudoUsado = false;
            this.velocidadOriginal = VELOCIDAD_BASE_VERDE;
            this.velocidad = VELOCIDAD_BASE_VERDE;
            this.escudoActivo = true;
        } else {
            this.escudoActivo = false;
        }
        aplicarStatsPorSkin();
    }

    // Getters y Setters
    public int getMuertes() { return muertes; }
    public void setMuertes(int muertes) { this.muertes = muertes; }
    public int getMonedasRecolectadas() { return monedasRecolectadas; }
    public void setMonedasRecolectadas(int monedas) { this.monedasRecolectadas = monedas; }
    public String getSkin() { return skin; }
    public void setSkin(String skin) { this.skin = skin; }
    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }
    public double getTamanio() { return tamanio; }
    public void setTamanio(double tamanio) { this.tamanio = tamanio; }
    public int getVidasExtra() { return vidasExtra; }
    public void setVidasExtra(int vidasExtra) { this.vidasExtra = vidasExtra; }
    public Color getColorBorde() { return colorBorde; }
    public void setColorBorde(Color colorBorde) { this.colorBorde = colorBorde; }
    public boolean isEscudoActivo() { return skin.equals(SKIN_VERDE) && escudoActivo && !escudoUsado; }
    public boolean isEscudoUsado() { return escudoUsado; }
    public String getSkinTemporal() { return skinTemporal; }
    public boolean isEfectoActivo() { return efectoActivo; }

    @Override
    public String getIcono() {
        String skinActual = (skinTemporal != null && efectoActivo) ? skinTemporal : skin;
        switch (skinActual) {
            case SKIN_ROJO: return "🟥";
            case SKIN_AZUL: return "🟦";
            case SKIN_VERDE: return "🟩";
            default: return "⬜";
        }
    }

    public int getTamanioDibujo(int tamCeldaBase) {
        return (int)(tamCeldaBase * tamanio);
    }

    @Override
    public String toString() {
        return "Jugador{skin='" + skin + "', x=" + x + ", y=" + y +
                ", muertes=" + muertes + ", monedas=" + monedasRecolectadas +
                ", vidasExtra=" + vidasExtra + "}";
    }
}