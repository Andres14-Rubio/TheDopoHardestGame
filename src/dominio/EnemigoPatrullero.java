package dominio;

public class EnemigoPatrullero extends Enemigo {

    public static final String PATRON_CIRCULAR = "CIRCULAR";
    public static final String PATRON_CUADRADO = "CUADRADO";
    public static final String PATRON_OCHO = "OCHO";

    private String patron;
    private int radio;
    private double angulo;
    private double centroX;
    private double centroY;
    private int velocidadAngular;
    private int paso;
    private int lado;
    private int puntosPorLado;

    public EnemigoPatrullero(int x, int y, String patron, int radio, int anchoTablero, int altoTablero) {
        super(x, y, 1);
        this.patron = patron;
        this.radio = radio;
        this.centroX = x;
        this.centroY = y;
        this.angulo = 0;
        this.velocidadAngular = 30;
        this.paso = 0;
        this.lado = radio * 2;
        this.puntosPorLado = lado * 4;
    }

    public EnemigoPatrullero(int x, int y, String patron, int radio, int velocidadAngular, int anchoTablero, int altoTablero) {
        this(x, y, patron, radio, anchoTablero, altoTablero);
        this.velocidadAngular = velocidadAngular;
    }

    @Override
    public void mover() {
        switch (patron) {
            case PATRON_CIRCULAR:
                moverCircular();
                break;
            case PATRON_CUADRADO:
                moverCuadrado();
                break;
            case PATRON_OCHO:
                moverOcho();
                break;
        }
    }

    private void moverCircular() {
        angulo += velocidadAngular;
        if (angulo >= 360) angulo -= 360;
        double rad = Math.toRadians(angulo);
        int nuevoX = (int) Math.round(centroX + radio * Math.cos(rad));
        int nuevoY = (int) Math.round(centroY + radio * Math.sin(rad));
        this.x = nuevoX;
        this.y = nuevoY;
    }

    private void moverCuadrado() {
        paso++;
        if (paso >= puntosPorLado) paso = 0;
        int segmento = paso / lado;
        int offset = paso % lado;
        switch (segmento) {
            case 0:
                this.x = (int)(centroX - radio + offset);
                this.y = (int)(centroY - radio);
                break;
            case 1:
                this.x = (int)(centroX + radio);
                this.y = (int)(centroY - radio + offset);
                break;
            case 2:
                this.x = (int)(centroX + radio - offset);
                this.y = (int)(centroY + radio);
                break;
            case 3:
                this.x = (int)(centroX - radio);
                this.y = (int)(centroY + radio - offset);
                break;
        }
    }

    private void moverOcho() {
        angulo += velocidadAngular;
        if (angulo >= 360) angulo -= 360;
        double rad = Math.toRadians(angulo);
        double xOffset = radio * Math.sin(rad);
        double yOffset = radio * Math.sin(2 * rad) / 2;
        this.x = (int) Math.round(centroX + xOffset);
        this.y = (int) Math.round(centroY + yOffset);
    }

    public String getPatron() { return patron; }
    public int getRadio() { return radio; }

    @Override
    public String getIcono() {
        return "🌀";
    }
}