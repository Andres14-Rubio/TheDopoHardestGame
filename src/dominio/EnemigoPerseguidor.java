package dominio;

public class EnemigoPerseguidor extends Enemigo {

    private Jugador objetivo;
    private int anchoTablero;
    private int altoTablero;

    public EnemigoPerseguidor(int x, int y, Jugador objetivo, int anchoTablero, int altoTablero) {
        super(x, y, 1);
        this.objetivo = objetivo;
        this.anchoTablero = anchoTablero;
        this.altoTablero = altoTablero;
    }

    @Override
    public void mover() {
        if (objetivo == null) return;

        int dx = Integer.compare(objetivo.getX(), x);
        int dy = Integer.compare(objetivo.getY(), y);

        if (Math.abs(objetivo.getX() - x) > Math.abs(objetivo.getY() - y)) {
            x += dx;
        } else {
            y += dy;
        }

        x = Math.max(0, Math.min(x, anchoTablero - 1));
        y = Math.max(0, Math.min(y, altoTablero - 1));
    }

    public void setObjetivo(Jugador objetivo) {
        this.objetivo = objetivo;
    }

    @Override
    public String getIcono() {
        return "👁️";
    }
}