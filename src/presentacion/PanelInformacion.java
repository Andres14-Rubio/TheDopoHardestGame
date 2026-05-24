package presentacion;

import javax.swing.*;
import java.awt.*;

public class PanelInformacion extends JPanel {
    
    private JLabel etiquetaTiempo;
    private JLabel etiquetaMuertes;
    private JLabel etiquetaMonedas;
    private JLabel etiquetaModo;
    private JLabel etiquetaSkin;
    private JProgressBar barraProgresoJ1;
    private JProgressBar barraProgresoJ2;
    private JLabel estadoPausa;
    private JLabel etiquetaMuertesJ2;
    private JLabel etiquetaMonedasJ2;
    private JLabel etiquetaEscudoJ1;
    private JLabel etiquetaEscudoJ2;
    
    public PanelInformacion() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));
        
        // Panel superior con indicadores
        JPanel panelIndicadores = new JPanel(new GridLayout(1, 8));
        panelIndicadores.setBackground(new Color(50, 50, 50));
        
        etiquetaTiempo = new JLabel("⏱️ TIEMPO: 00:00");
        etiquetaMuertes = new JLabel("💀 J1:0");
        etiquetaMonedas = new JLabel("🪙 J1:0/0");
        etiquetaMuertesJ2 = new JLabel("💀 J2:0");
        etiquetaMonedasJ2 = new JLabel("🪙 J2:0/0");
        etiquetaModo = new JLabel("🎮 MODO: PLAYER");
        etiquetaSkin = new JLabel("🎨 SKIN: Rojo");
        etiquetaEscudoJ1 = new JLabel("");
        estadoPausa = new JLabel("");
        etiquetaEscudoJ1 = new JLabel("");
        etiquetaEscudoJ1.setFont(new Font("Arial", Font.BOLD, 11));
        
        configurarLabel(etiquetaTiempo);
        configurarLabel(etiquetaMuertes);
        configurarLabel(etiquetaMonedas);
        configurarLabel(etiquetaMuertesJ2);
        configurarLabel(etiquetaMonedasJ2);
        configurarLabel(etiquetaModo);
        configurarLabel(etiquetaSkin);
        configurarLabel(etiquetaEscudoJ1);
        estadoPausa.setForeground(Color.YELLOW);
        
        panelIndicadores.add(etiquetaTiempo);
        panelIndicadores.add(etiquetaMuertes);
        panelIndicadores.add(etiquetaMonedas);
        panelIndicadores.add(etiquetaMuertesJ2);
        panelIndicadores.add(etiquetaMonedasJ2);
        panelIndicadores.add(etiquetaModo);
        panelIndicadores.add(etiquetaSkin);
        panelIndicadores.add(etiquetaEscudoJ1);
        
        // Panel de barras de progreso
        JPanel panelBarras = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBarras.setBackground(new Color(50, 50, 50));
        panelBarras.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Barra de J1
        JPanel panelJ1 = new JPanel(new BorderLayout(5, 0));
        panelJ1.setBackground(new Color(50, 50, 50));
        JLabel labelJ1 = new JLabel("🔴 JUGADOR 1:");
        labelJ1.setForeground(Color.RED);
        labelJ1.setFont(new Font("Arial", Font.BOLD, 12));
        
        barraProgresoJ1 = new JProgressBar(0, 100);
        barraProgresoJ1.setStringPainted(true);
        barraProgresoJ1.setForeground(new Color(0, 200, 0));
        barraProgresoJ1.setBackground(new Color(100, 100, 100));
        
        panelJ1.add(labelJ1, BorderLayout.WEST);
        panelJ1.add(barraProgresoJ1, BorderLayout.CENTER);
        
        // Barra de J2
        JPanel panelJ2 = new JPanel(new BorderLayout(5, 0));
        panelJ2.setBackground(new Color(50, 50, 50));
        JLabel labelJ2 = new JLabel("🔵 JUGADOR 2:");
        labelJ2.setForeground(Color.BLUE);
        labelJ2.setFont(new Font("Arial", Font.BOLD, 12));
        
        barraProgresoJ2 = new JProgressBar(0, 100);
        barraProgresoJ2.setStringPainted(true);
        barraProgresoJ2.setForeground(new Color(0, 200, 0));
        barraProgresoJ2.setBackground(new Color(100, 100, 100));
        barraProgresoJ2.setVisible(false);
        
        panelJ2.add(labelJ2, BorderLayout.WEST);
        panelJ2.add(barraProgresoJ2, BorderLayout.CENTER);
        
        panelBarras.add(panelJ1);
        panelBarras.add(panelJ2);
        
        // Panel de estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEstado.setBackground(new Color(50, 50, 50));
        panelEstado.add(estadoPausa);
        
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(50, 50, 50));
        panelNorte.add(panelIndicadores, BorderLayout.NORTH);
        panelNorte.add(panelBarras, BorderLayout.CENTER);
        panelNorte.add(panelEstado, BorderLayout.SOUTH);
        
        add(panelNorte, BorderLayout.CENTER);
    }
    
    private void configurarLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    public void setTiempo(int segundos) {
        int minutos = segundos / 60;
        int segs = segundos % 60;
        etiquetaTiempo.setText(String.format("⏱️ TIEMPO: %02d:%02d", minutos, segs));
    }
    
    public void setMuertes(int muertes) {
        etiquetaMuertes.setText("💀 J1:" + muertes);
    }
    
    public void setMonedas(int recolectadas, int total) {
        etiquetaMonedas.setText("🪙 J1:" + recolectadas + "/" + total);
        int porcentaje = total > 0 ? (recolectadas * 100 / total) : 0;
        barraProgresoJ1.setValue(porcentaje);
        barraProgresoJ1.setString(porcentaje + "% - " + recolectadas + "/" + total);
    }
    
    public void setModo(String modo) {
        etiquetaModo.setText("🎮 MODO: " + modo);
        barraProgresoJ2.setVisible(modo.equals("PVP"));
    }
    
    public void setSkin(String skin) {
        etiquetaSkin.setText("🎨 SKIN: " + skin);
    }
    
    public void mostrarPausado(boolean pausado) {
        estadoPausa.setText(pausado ? "⏸️ PAUSADO" : "");
    }
    
    public void setMuertesJ2(int muertes) {
        etiquetaMuertesJ2.setText("💀 J2:" + muertes);
    }
    
    public void setMonedasJ2(int recolectadas, int total) {
        etiquetaMonedasJ2.setText("🪙 J2:" + recolectadas + "/" + total);
        int porcentaje = total > 0 ? (recolectadas * 100 / total) : 0;
        barraProgresoJ2.setValue(porcentaje);
        barraProgresoJ2.setString(porcentaje + "% - " + recolectadas + "/" + total);
    }
    
    public void setEstadoEscudoJ1(boolean activo) {
        if (activo) {
            etiquetaEscudoJ1.setText("🛡️ ESCUDO VERDE");
            etiquetaEscudoJ1.setForeground(new Color(0, 200, 0));
        } else {
            etiquetaEscudoJ1.setText("");
        }
    }
    
    public void setEstadoEscudoJ2(boolean activo) {
        // Puedes agregar un label para J2 si lo deseas
    }
    public void setEstadoEscudoJ1(boolean activo, boolean usado) {
    if (activo && !usado) {
        etiquetaEscudoJ1.setText("🛡️ ESCUDO VERDE ACTIVO");
        etiquetaEscudoJ1.setForeground(new Color(0, 200, 0));
    } else if (usado) {
        etiquetaEscudoJ1.setText("⚠️ ESCUDO USADO (velocidad reducida)");
        etiquetaEscudoJ1.setForeground(Color.ORANGE);
    } else {
        etiquetaEscudoJ1.setText("");
    }
}

    
}