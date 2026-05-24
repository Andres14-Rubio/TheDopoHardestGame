package dominio;

public class EnemigoBasico extends Enemigo {

    private boolean direccionHorizontal;
    private int sentido;
    protected int velocidadBase;
    private int anchoTablero;
    private int altoTablero;

    public EnemigoBasico(int x, int y, boolean direccionHorizontal,
                         int anchoTablero, int altoTablero) {
        super(x, y, 1);
        this.direccionHorizontal = direccionHorizontal;
        this.sentido = 1;
        this.velocidadBase = 1;
        this.anchoTablero = anchoTablero;
        this.altoTablero = altoTablero;
    }

    @Override
    public void mover() {
        if (direccionHorizontal) {
            int nuevaX = x + sentido * velocidadBase;
            if (nuevaX < 0 || nuevaX >= anchoTablero) {
                sentido *= -1;
                nuevaX = x + sentido * velocidadBase;
            }
            x = nuevaX;
        } else {
            int nuevaY = y + sentido * velocidadBase;
            if (nuevaY < 0 || nuevaY >= altoTablero) {
                sentido *= -1;
                nuevaY = y + sentido * velocidadBase;
            }
            y = nuevaY;
        }
    }

    public void rebotar() {
        this.sentido *= -1;
    }

    public boolean isDireccionHorizontal() {
        return direccionHorizontal;
    }

    public void setDireccionHorizontal(boolean direccionHorizontal) {
        this.direccionHorizontal = direccionHorizontal;
    }

    public int getSentido() {
        return sentido;
    }

    public int getVelocidadBase() {
        return velocidadBase;
    }

    public void setVelocidadBase(int velocidadBase) {
        this.velocidadBase = velocidadBase;
    }

    @Override
    public String getIcono() {
        return "🔵";
    }
}