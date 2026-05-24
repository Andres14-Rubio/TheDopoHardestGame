package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dominio.*;

public class VentanaPrincipal extends JFrame {

    private PanelJuego panelJuego;
    private PanelInformacion panelInformacion;
    private PanelBotones panelBotones;
    private Juego juego;
    private TemporizadorJuego temporizador;

    public VentanaPrincipal() {
        setTitle("The DOPO Hardest Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(950, 750);
        setLocationRelativeTo(null);

        mostrarDialogoConfiguracion();
    }

    private void mostrarDialogoConfiguracion() {
        DialogoConfiguracion dialog = new DialogoConfiguracion(this);
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String modalidad  = dialog.getModalidad();
            String skinJ1     = dialog.getSkinJ1();
            String skinJ2     = dialog.getSkinJ2();
            String configPath = dialog.getConfiguracion();
            String perfil     = dialog.getPerfilMaquina();
            Color  colorJ1    = dialog.getColorBordeJ1();
            Color  colorJ2    = dialog.getColorBordeJ2();
            iniciarJuego(configPath, modalidad, skinJ1, skinJ2, perfil, colorJ1, colorJ2);
        } else {
            System.exit(0);
        }
    }

    private void iniciarJuego(String rutaConfig, String modalidad, String skinJ1, String skinJ2,
                              String perfilNombre, Color colorBordeJ1, Color colorBordeJ2) {
        try {
            Configuracion config = Configuracion.cargarDesdeArchivo(rutaConfig);

            if ("PVM".equals(modalidad)) {
                dominio.PerfilMaquina perfil = "Experta".equals(perfilNombre)
                        ? new dominio.MaquinaExperta()
                        : new dominio.MaquinaAleatoria();
                juego = new Juego(modalidad, perfil);
            } else {
                juego = new Juego(modalidad);
            }

            juego.iniciarJuego(config, skinJ1, skinJ2);

            // Aplicar colores de borde personalizados
            juego.getNivelActual().getJugador1().setColorBorde(colorBordeJ1);
            if (juego.getNivelActual().getJugador2() != null) {
                juego.getNivelActual().getJugador2().setColorBorde(colorBordeJ2);
            }

            panelJuego       = new PanelJuego(juego);
            panelInformacion = new PanelInformacion();
            panelBotones     = new PanelBotones();

            add(panelInformacion, BorderLayout.NORTH);
            add(panelJuego,       BorderLayout.CENTER);
            add(panelBotones,     BorderLayout.SOUTH);

            configurarBotones();

            ControladorTeclado controlador = new ControladorTeclado(juego);
            addKeyListener(controlador);
            setFocusable(true);

            temporizador = new TemporizadorJuego(juego, 1000);
            temporizador.iniciar();

            actualizarUI();

            setVisible(true);
            revalidate();
            repaint();

        } catch (JuegoException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            System.exit(0);
        }
    }

    private void configurarBotones() {
        panelBotones.setListenerPausa(e -> pausarJuego());
        panelBotones.setListenerReanudar(e -> reanudarJuego());
        panelBotones.setListenerTerminar(e -> terminarJuego());
        panelBotones.setListenerGuardar(e -> guardarPartida());
        panelBotones.setListenerCargar(e -> cargarPartida());
    }

    private void pausarJuego() {
        if (juego != null && juego.isCorriendo() && !juego.isPausado()) {
            juego.pausarJuego();
            temporizador.pausar();
            panelInformacion.mostrarPausado(true);
        }
    }

    private void reanudarJuego() {
        if (juego != null && juego.isCorriendo() && juego.isPausado()) {
            juego.reanudarJuego();
            temporizador.reanudar();
            panelInformacion.mostrarPausado(false);
            requestFocusInWindow();
        }
    }

    private void terminarJuego() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres terminar?", "Terminar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            temporizador.detener();
            juego.terminarJuego();
            dispose();
        }
    }

    private void guardarPartida() {
        if (juego == null || !juego.isCorriendo()) {
            JOptionPane.showMessageDialog(this, "No hay partida en curso para guardar.");
            return;
        }
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Guardar partida");
        fc.setSelectedFile(new java.io.File("partida.save"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = fc.getSelectedFile().getPath();
            if (!ruta.endsWith(".save")) ruta += ".save";
            try {
                juego.pausarJuego();
                temporizador.pausar();
                juego.guardarPartida(ruta);
                JOptionPane.showMessageDialog(this, "✅ Partida guardada en:\n" + ruta);
                juego.reanudarJuego();
                temporizador.reanudar();
                requestFocusInWindow();
            } catch (dominio.JuegoException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar: " + ex.getMessage());
            }
        }
    }

    private void cargarPartida() {
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Cargar partida guardada (.save)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Partidas guardadas", "save"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                if (temporizador != null) temporizador.detener();

                dominio.EstadoPartida estado = dominio.EstadoPartida.cargar(fc.getSelectedFile().getPath());
                juego = estado.restaurar();

                panelJuego.setJuego(juego);
                temporizador = new TemporizadorJuego(juego, 1000);
                temporizador.iniciar();

                ControladorTeclado controlador = new ControladorTeclado(juego);
                // Remover listeners anteriores
                for (KeyListener kl : getKeyListeners()) removeKeyListener(kl);
                addKeyListener(controlador);

                JOptionPane.showMessageDialog(this, "✅ Partida cargada correctamente.");
                requestFocusInWindow();
            } catch (dominio.JuegoException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al cargar partida: " + ex.getMessage());
            }
        }
    }

    private void actualizarUI() {
        Timer timerUI = new Timer(50, e -> {
            if (juego != null && juego.isCorriendo()) {
                Nivel nivel = juego.getNivelActual();
                if (nivel != null) {
                    // Información general
                    panelInformacion.setTiempo(juego.getTiempoRestante());
                    panelInformacion.setModo(juego.getModalidad());

                    if (juego.getModalidad().equals("PVP") || juego.getModalidad().equals("PVM")) {
                        // Modo PvP / PvM: mostrar ambos jugadores
                        panelInformacion.setMuertes(juego.getMuertes());
                        panelInformacion.setMonedas(juego.getMonedasRecolectadas(), juego.getTotalMonedas());
                        panelInformacion.setMuertesJ2(juego.getMuertesJugador2());
                        panelInformacion.setMonedasJ2(juego.getMonedasRecolectadasJugador2(), juego.getTotalMonedas());

                        String skinJ1 = nivel.getJugador1().getSkin();
                        String etiquetaJ2 = juego.getModalidad().equals("PVM")
                                ? "IA " + (juego.getPerfilMaquina() != null ? juego.getPerfilMaquina().getNombre() : "")
                                : nivel.getJugador2().getSkin();
                        panelInformacion.setSkin(skinJ1 + " vs " + etiquetaJ2);

                        if (skinJ1.equals("Verde")) {
                            panelInformacion.setEstadoEscudoJ1(nivel.getJugador1().isEscudoActivo());
                        }
                        if (nivel.getJugador2() != null && nivel.getJugador2().getSkin().equals("Verde")) {
                            panelInformacion.setEstadoEscudoJ2(nivel.getJugador2().isEscudoActivo());
                        }
                    } else {
                        // Modo Player: solo un jugador
                        panelInformacion.setMuertes(juego.getMuertes());
                        panelInformacion.setMonedas(juego.getMonedasRecolectadas(), juego.getTotalMonedas());
                        panelInformacion.setMuertesJ2(0);
                        panelInformacion.setMonedasJ2(0, juego.getTotalMonedas());
                        panelInformacion.setSkin(nivel.getJugador1().getSkin());

                        // Mostrar estado de escudo para skin verde
                        if (nivel.getJugador1().getSkin().equals("Verde")) {
                            panelInformacion.setEstadoEscudoJ1(nivel.getJugador1().isEscudoActivo());
                        }
                    }

                    panelJuego.repaint();

                    // Verificar victoria
                    if (nivel.getEstado().equals(Nivel.ESTADO_GANADO)) {
                        ((Timer)e.getSource()).stop();
                        temporizador.detener();
                        String ganador = juego.getGanador();
                        if (ganador != null) {
                            JOptionPane.showMessageDialog(this, "🎉 " + ganador + " HA GANADO! 🎉");
                        } else {
                            JOptionPane.showMessageDialog(this, "🎉 VICTORIA! 🎉");
                        }
                        reiniciarJuego();
                    } else if (nivel.getEstado().equals(Nivel.ESTADO_PERDIDO)) {
                        ((Timer)e.getSource()).stop();
                        temporizador.detener();
                        JOptionPane.showMessageDialog(this, "💀 DERROTA - Tiempo agotado");
                        reiniciarJuego();
                    }
                }
            }
        });
        timerUI.start();
    }

    private void reiniciarJuego() {
        dispose();
        new VentanaPrincipal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}