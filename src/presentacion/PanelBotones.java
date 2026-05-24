package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelBotones extends JPanel {
    
    private JButton botonPausa;
    private JButton botonReanudar;
    private JButton botonTerminar;
    private JButton botonGuardar;
    private JButton botonCargar;
    
    // Variables para almacenar los listeners
    private ActionListener listenerPausa;
    private ActionListener listenerReanudar;
    private ActionListener listenerTerminar;
    private ActionListener listenerGuardar;
    private ActionListener listenerCargar;
    
    public PanelBotones() {
        setLayout(new FlowLayout());
        setBackground(new Color(60, 60, 60));
        
        botonPausa = new JButton("⏸️ Pausa");
        botonReanudar = new JButton("▶️ Reanudar");
        botonTerminar = new JButton("❌ Terminar");
        botonGuardar = new JButton("💾 Guardar");
        botonCargar = new JButton("📂 Cargar");
        
        configurarBoton(botonPausa);
        configurarBoton(botonReanudar);
        configurarBoton(botonTerminar);
        configurarBoton(botonGuardar);
        configurarBoton(botonCargar);
        
        // Agregar ActionListeners directamente
        botonPausa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listenerPausa != null) listenerPausa.actionPerformed(e);
            }
        });
        
        botonReanudar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listenerReanudar != null) listenerReanudar.actionPerformed(e);
            }
        });
        
        botonTerminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listenerTerminar != null) listenerTerminar.actionPerformed(e);
            }
        });
        
        botonGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listenerGuardar != null) listenerGuardar.actionPerformed(e);
            }
        });
        
        botonCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listenerCargar != null) listenerCargar.actionPerformed(e);
            }
        });
        
        add(botonPausa);
        add(botonReanudar);
        add(botonTerminar);
        add(botonGuardar);
        add(botonCargar);
    }
    
    private void configurarBoton(JButton boton) {
        boton.setBackground(new Color(200, 200, 200));
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
    }
    
    // Métodos para establecer los listeners
    public void setListenerPausa(ActionListener listener) {
        this.listenerPausa = listener;
    }
    
    public void setListenerReanudar(ActionListener listener) {
        this.listenerReanudar = listener;
    }
    
    public void setListenerTerminar(ActionListener listener) {
        this.listenerTerminar = listener;
    }
    
    public void setListenerGuardar(ActionListener listener) {
        this.listenerGuardar = listener;
    }
    
    public void setListenerCargar(ActionListener listener) {
        this.listenerCargar = listener;
    }
}