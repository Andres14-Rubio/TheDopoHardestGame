package presentacion;

import javax.swing.*;
import java.awt.event.*;
import dominio.*;

public class TemporizadorJuego implements ActionListener {

    private Juego juego;
    private Timer timerTiempo;
    private Timer timerEnemigos;
    private Timer timerMaquina;
    private boolean estaPausado;

    public TemporizadorJuego(Juego juego, int intervaloMs) {
        this.juego = juego;
        this.estaPausado = false;

        // Timer para el tiempo (cada 1 segundo)
        this.timerTiempo = new Timer(intervaloMs, this);

        // Timer para enemigos (cada 100 ms para movimiento suave)
        this.timerEnemigos = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (juego != null && !estaPausado && juego.isCorriendo()) {
                    if (juego.getNivelActual() != null) {
                        juego.getNivelActual().actualizarEnemigos();
                        juego.getNivelActual().verificarColisiones();
                    }
                }
            }
        });

        // Timer para la máquina en modo PvM
        // Velocidad ajustada según perfil: Experta más lenta para ser desafiante
        int intervaloMaquina = calcularIntervaloMaquina(juego);
        this.timerMaquina = new Timer(intervaloMaquina, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (juego != null && !estaPausado && juego.isCorriendo()) {
                    if ("PVM".equals(juego.getModalidad())) {
                        juego.moverMaquina();
                    }
                }
            }
        });
    }

    private int calcularIntervaloMaquina(Juego juego) {
        if (juego.getPerfilMaquina() != null &&
                "Experta".equals(juego.getPerfilMaquina().getNombre())) {
            return 300; // Experta: 1 movimiento cada 300ms — desafiante pero derrotable
        }
        return 500; // Aleatoria: 1 movimiento cada 500ms
    }

    public void iniciar() {
        timerTiempo.start();
        timerEnemigos.start();
        if ("PVM".equals(juego.getModalidad())) {
            timerMaquina.start();
        }
        estaPausado = false;
    }

    public void pausar() {
        if (!estaPausado) {
            timerTiempo.stop();
            timerEnemigos.stop();
            timerMaquina.stop();
            estaPausado = true;
        }
    }

    public void reanudar() {
        if (estaPausado) {
            timerTiempo.start();
            timerEnemigos.start();
            if ("PVM".equals(juego.getModalidad())) {
                timerMaquina.start();
            }
            estaPausado = false;
        }
    }

    public void detener() {
        timerTiempo.stop();
        timerEnemigos.stop();
        timerMaquina.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (juego != null && !estaPausado && juego.isCorriendo()) {
            juego.actualizarTiempo();
        }
    }
}
