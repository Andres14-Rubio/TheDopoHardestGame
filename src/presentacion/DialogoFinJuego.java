package presentacion;

import javax.swing.*;
import java.awt.*;

/**
 * Clase DialogoFinJuego - Diálogo que muestra victoria o derrota.
 * 
 * @author Kevin Andrey Angel - Santiago Andres Garcia
 * @version 1.0 - Abril 2026
 */
public class DialogoFinJuego extends JDialog {
    
    private JButton botonReiniciar;
    private JButton botonSalir;
    private boolean reiniciar;
    
    public DialogoFinJuego(JFrame parent, boolean victoria, int tiempo, int muertes) {
        super(parent, victoria ? "¡Victoria!" : "Derrota", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        JPanel panelMensaje = new JPanel();
        panelMensaje.setLayout(new BoxLayout(panelMensaje, BoxLayout.Y_AXIS));
        
        JLabel titulo = new JLabel(victoria ? "🎉 ¡VICTORIA! 🎉" : "💀 DERROTA 💀");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel detalle = new JLabel();
        if (victoria) {
            detalle.setText("Tiempo: " + tiempo + " segundos | Muertes: " + muertes);
        } else {
            detalle.setText("Has perdido. Muertes: " + muertes);
        }
        detalle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelMensaje.add(Box.createVerticalStrut(30));
        panelMensaje.add(titulo);
        panelMensaje.add(Box.createVerticalStrut(20));
        panelMensaje.add(detalle);
        panelMensaje.add(Box.createVerticalStrut(30));
        
        JPanel panelBotones = new JPanel();
        botonReiniciar = new JButton("🔄 Reiniciar");
        botonSalir = new JButton("🚪 Salir");
        
        botonReiniciar.addActionListener(e -> {
            reiniciar = true;
            setVisible(false);
        });
        
        botonSalir.addActionListener(e -> {
            reiniciar = false;
            setVisible(false);
        });
        
        panelBotones.add(botonReiniciar);
        panelBotones.add(botonSalir);
        
        add(panelMensaje, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public boolean isReiniciar() {
        return reiniciar;
    }
}