package presentacion;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DialogoConfiguracion extends JDialog {

    private JComboBox<String> comboModalidad;
    private JComboBox<String> comboSkinJ1;
    private JComboBox<String> comboSkinJ2;
    private JComboBox<String> comboPerfil;
    private JComboBox<String> comboConfiguracion;
    private JButton botonAceptar;
    private JButton botonCancelar;
    private boolean aceptado;
    private String modalidadSeleccionada;
    private String configSeleccionada;
    private String skinJ1Seleccionada;
    private String skinJ2Seleccionada;
    private String perfilMaquinaSeleccionado;

    private JLabel labelSkinJ2;
    private JLabel labelPerfil;

    // Colores de borde elegidos por cada jugador
    private Color colorBordeJ1 = Color.WHITE;
    private Color colorBordeJ2 = Color.YELLOW;

    // Botones que muestran el color actual como preview
    private JButton botonColorJ1;
    private JButton botonColorJ2;
    private JLabel labelColorJ2;

    public DialogoConfiguracion(JFrame parent) {
        super(parent, "Configuración del Juego - The DOPO Hardest Game", true);
        setSize(600, 620);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("⚙️ CONFIGURACIÓN DEL JUEGO ⚙️");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(200, 0, 0));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, gbc);
        gbc.gridwidth = 1;

        // Modalidad
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("🎮 Modalidad:"), gbc);
        gbc.gridx = 1;
        comboModalidad = new JComboBox<>(new String[]{"PLAYER", "PVP", "PVM"});
        comboModalidad.setFont(new Font("Arial", Font.BOLD, 12));
        add(comboModalidad, gbc);

        // Skin Jugador 1
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel labelSkinJ1 = new JLabel("🔴 Skin Jugador 1:");
        labelSkinJ1.setFont(new Font("Arial", Font.BOLD, 12));
        add(labelSkinJ1, gbc);
        gbc.gridx = 1;
        comboSkinJ1 = new JComboBox<>(new String[]{
                "🔴 Rojo (Blinky) - Velocidad normal | Tamaño normal",
                "🔵 Azul (Inky) - Velocidad rápida | Tamaño grande",
                "🟢 Verde (Clyde) - Resistente (absorbe 1 golpe)"
        });
        comboSkinJ1.setFont(new Font("Arial", Font.PLAIN, 11));
        add(comboSkinJ1, gbc);

        // Color de borde Jugador 1
        gbc.gridy = 3; gbc.gridx = 0;
        add(new JLabel("🎨 Color borde J1:"), gbc);
        gbc.gridx = 1;
        botonColorJ1 = crearBotonColor(colorBordeJ1);
        botonColorJ1.addActionListener(e -> {
            Color elegido = JColorChooser.showDialog(
                    this, "Elegir color de borde - Jugador 1", colorBordeJ1);
            if (elegido != null) {
                colorBordeJ1 = elegido;
                actualizarBotonColor(botonColorJ1, colorBordeJ1);
            }
        });
        add(botonColorJ1, gbc);

        // Skin Jugador 2 (solo PVP)
        gbc.gridy = 4; gbc.gridx = 0;
        labelSkinJ2 = new JLabel("🔵 Skin Jugador 2:");
        labelSkinJ2.setFont(new Font("Arial", Font.BOLD, 12));
        add(labelSkinJ2, gbc);
        gbc.gridx = 1;
        comboSkinJ2 = new JComboBox<>(new String[]{
                "🔴 Rojo (Blinky) - Velocidad normal | Tamaño normal",
                "🔵 Azul (Inky) - Velocidad rápida | Tamaño grande",
                "🟢 Verde (Clyde) - Resistente (absorbe 1 golpe)"
        });
        comboSkinJ2.setFont(new Font("Arial", Font.PLAIN, 11));
        comboSkinJ2.setEnabled(false);
        add(comboSkinJ2, gbc);

        // Color de borde Jugador 2 (solo PVP)
        gbc.gridy = 5; gbc.gridx = 0;
        labelColorJ2 = new JLabel("🎨 Color borde J2:");
        labelColorJ2.setEnabled(false);
        add(labelColorJ2, gbc);
        gbc.gridx = 1;
        botonColorJ2 = crearBotonColor(colorBordeJ2);
        botonColorJ2.setEnabled(false);
        botonColorJ2.addActionListener(e -> {
            Color elegido = JColorChooser.showDialog(
                    this, "Elegir color de borde - Jugador 2", colorBordeJ2);
            if (elegido != null) {
                colorBordeJ2 = elegido;
                actualizarBotonColor(botonColorJ2, colorBordeJ2);
            }
        });
        add(botonColorJ2, gbc);

        // Perfil Máquina (solo PVM)
        gbc.gridy = 6; gbc.gridx = 0;
        labelPerfil = new JLabel("🤖 Perfil de la Máquina:");
        labelPerfil.setFont(new Font("Arial", Font.BOLD, 12));
        labelPerfil.setEnabled(false);
        add(labelPerfil, gbc);
        gbc.gridx = 1;
        comboPerfil = new JComboBox<>(new String[]{
                "🎲 Aleatoria - Movimientos al azar",
                "🧠 Experta - Pathfinding BFS (más difícil)"
        });
        comboPerfil.setFont(new Font("Arial", Font.PLAIN, 11));
        comboPerfil.setEnabled(false);
        add(comboPerfil, gbc);

        // Configuración / Nivel
        gbc.gridy = 7; gbc.gridx = 0;
        add(new JLabel("🗺️ Nivel / Configuración:"), gbc);
        gbc.gridx = 1;
        comboConfiguracion = new JComboBox<>(cargarConfiguraciones());
        comboConfiguracion.setFont(new Font("Arial", Font.PLAIN, 11));
        add(comboConfiguracion, gbc);

        // Panel info elementos especiales
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel panelInfoEspeciales = new JPanel();
        panelInfoEspeciales.setBackground(new Color(240, 240, 240));
        panelInfoEspeciales.setBorder(BorderFactory.createTitledBorder("✨ NUEVOS ELEMENTOS ESPECIALES ✨"));
        panelInfoEspeciales.setLayout(new GridLayout(2, 1, 5, 5));
        JLabel infoBombas  = new JLabel("💣 BOMBAS: Destruyen jugadores y enemigos al tocarlas");
        JLabel infoFuentes = new JLabel("❤️ FUENTES DE VIDA: Dan una vida extra al recolectarlas");
        infoBombas.setFont(new Font("Arial", Font.PLAIN, 11));
        infoFuentes.setFont(new Font("Arial", Font.PLAIN, 11));
        infoBombas.setForeground(new Color(150, 0, 0));
        infoFuentes.setForeground(new Color(0, 150, 0));
        panelInfoEspeciales.add(infoBombas);
        panelInfoEspeciales.add(infoFuentes);
        add(panelInfoEspeciales, gbc);

        // Panel info skins
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel panelInfoSkins = new JPanel();
        panelInfoSkins.setBackground(new Color(240, 240, 240));
        panelInfoSkins.setBorder(BorderFactory.createTitledBorder("🎨 INFORMACIÓN DE SKINS 🎨"));
        panelInfoSkins.setLayout(new GridLayout(3, 1, 5, 5));
        JLabel infoRojo  = new JLabel("🔴 Rojo: Velocidad 1.0x | Tamaño 1.0x | Sin habilidad especial");
        JLabel infoAzul  = new JLabel("🔵 Azul: Velocidad 1.5x | Tamaño 1.5x | Más rápido pero más grande");
        JLabel infoVerde = new JLabel("🟢 Verde: Velocidad 1.0x | Absorbe 1 golpe, luego velocidad 0.7x");
        infoRojo.setFont(new Font("Arial", Font.PLAIN, 10));
        infoAzul.setFont(new Font("Arial", Font.PLAIN, 10));
        infoVerde.setFont(new Font("Arial", Font.PLAIN, 10));
        panelInfoSkins.add(infoRojo);
        panelInfoSkins.add(infoAzul);
        panelInfoSkins.add(infoVerde);
        add(panelInfoSkins, gbc);

        // Botones aceptar/cancelar
        gbc.gridy = 10; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout());
        botonAceptar  = new JButton("✅ Aceptar");
        botonCancelar = new JButton("❌ Cancelar");
        botonAceptar.setBackground(new Color(0, 150, 0));
        botonAceptar.setForeground(Color.WHITE);
        botonAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        botonCancelar.setBackground(new Color(150, 0, 0));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);
        add(panelBotones, gbc);

        // Listener modalidad → activar/desactivar controles
        comboModalidad.addActionListener(e -> {
            String modo = (String) comboModalidad.getSelectedItem();
            boolean esPVP = "PVP".equals(modo);
            boolean esPVM = "PVM".equals(modo);

            comboSkinJ2.setEnabled(esPVP);
            labelSkinJ2.setEnabled(esPVP);

            botonColorJ2.setEnabled(esPVP);
            labelColorJ2.setEnabled(esPVP);

            comboPerfil.setEnabled(esPVM);
            labelPerfil.setEnabled(esPVM);
        });

        botonAceptar.addActionListener(e -> {
            modalidadSeleccionada      = (String) comboModalidad.getSelectedItem();
            configSeleccionada         = (String) comboConfiguracion.getSelectedItem();
            skinJ1Seleccionada         = extraerSkinDeTexto((String) comboSkinJ1.getSelectedItem());

            if ("PVP".equals(modalidadSeleccionada)) {
                skinJ2Seleccionada = extraerSkinDeTexto((String) comboSkinJ2.getSelectedItem());
            } else {
                skinJ2Seleccionada = null;
            }

            if ("PVM".equals(modalidadSeleccionada)) {
                perfilMaquinaSeleccionado = extraerPerfilDeTexto((String) comboPerfil.getSelectedItem());
            } else {
                perfilMaquinaSeleccionado = null;
            }

            aceptado = true;
            setVisible(false);
        });

        botonCancelar.addActionListener(e -> {
            aceptado = false;
            setVisible(false);
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // Crea un botón cuadrado que muestra el color como fondo
    private JButton crearBotonColor(Color color) {
        JButton boton = new JButton("  Elegir color  ");
        boton.setFont(new Font("Arial", Font.PLAIN, 11));
        boton.setBackground(color);
        // El texto se pone negro o blanco según la luminosidad del fondo
        boton.setForeground(colorTextoSobreFondo(color));
        boton.setOpaque(true);
        boton.setBorderPainted(true);
        return boton;
    }

    // Actualiza el color de fondo del botón preview
    private void actualizarBotonColor(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(colorTextoSobreFondo(color));
        boton.repaint();
    }

    // Devuelve negro o blanco según la luminosidad, para que el texto sea legible
    private Color colorTextoSobreFondo(Color fondo) {
        double luminosidad = (0.299 * fondo.getRed()
                + 0.587 * fondo.getGreen()
                + 0.114 * fondo.getBlue()) / 255.0;
        return luminosidad > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private String extraerSkinDeTexto(String texto) {
        if (texto.contains("Rojo"))  return "Rojo";
        if (texto.contains("Azul"))  return "Azul";
        if (texto.contains("Verde")) return "Verde";
        return "Rojo";
    }

    private String extraerPerfilDeTexto(String texto) {
        if (texto.contains("Experta")) return "Experta";
        return "Aleatoria";
    }

    private String[] cargarConfiguraciones() {
        String[] carpetasCandidatas = {"recursos", "."};
        for (String ruta : carpetasCandidatas) {
            File carpeta = new File(ruta);
            if (carpeta.exists() && carpeta.isDirectory()) {
                String[] archivos = carpeta.list((dir, name) ->
                        name.endsWith(".txt") && !name.contains("error"));
                if (archivos != null && archivos.length > 0) {
                    java.util.Arrays.sort(archivos);
                    for (int i = 0; i < archivos.length; i++) {
                        archivos[i] = ruta + "/" + archivos[i];
                    }
                    return archivos;
                }
            }
        }
        return new String[]{"recursos/nivel1.txt"};
    }

    // Getters
    public boolean isAceptado()          { return aceptado; }
    public String getModalidad()          { return modalidadSeleccionada; }
    public String getSkinJ1()             { return skinJ1Seleccionada; }
    public String getSkinJ2()             { return skinJ2Seleccionada; }
    public String getConfiguracion()      { return configSeleccionada; }
    public String getPerfilMaquina()      { return perfilMaquinaSeleccionado; }
    public Color  getColorBordeJ1()       { return colorBordeJ1; }
    public Color  getColorBordeJ2()       { return colorBordeJ2; }
}