package dominio;

import java.io.*;
import java.util.*;

/**
 * EstadoPartida - Guarda y carga el estado completo de una partida en curso.
 * AHORA con una sola lista de monedas pendientes (compartidas).
 */
public class EstadoPartida {

    private String modalidad;
    private String perfilMaquina;
    private String configArchivo;
    private int tiempoRestante;

    // Jugador 1
    private String skinJ1;
    private int xJ1, yJ1;
    private int muertesJ1;
    private int monedasJ1;
    private int reapXJ1, reapYJ1;
    private boolean zonaInterJ1;
    private int monedasGuardadasJ1;

    // Jugador 2
    private String skinJ2;
    private int xJ2, yJ2;
    private int muertesJ2;
    private int monedasJ2count;
    private int reapXJ2, reapYJ2;
    private boolean zonaInterJ2;
    private int monedasGuardadasJ2;

    // Monedas pendientes (compartidas)
    private List<int[]> monedasPendientes = new ArrayList<>();

    public EstadoPartida() {}

    // ==========================================
    // CAPTURAR ESTADO DESDE EL JUEGO EN CURSO
    // ==========================================

    public static EstadoPartida capturar(Juego juego, String configArchivo) {
        EstadoPartida ep = new EstadoPartida();
        Nivel nivel = juego.getNivelActual();

        ep.modalidad     = juego.getModalidad();
        ep.perfilMaquina = juego.getPerfilMaquina() != null ? juego.getPerfilMaquina().getNombre() : null;
        ep.configArchivo = configArchivo;
        ep.tiempoRestante = nivel.getTiempoRestante();

        // Jugador 1
        Jugador j1 = nivel.getJugador1();
        ep.skinJ1            = j1.getSkin();
        ep.xJ1               = j1.getX();
        ep.yJ1               = j1.getY();
        ep.muertesJ1         = j1.getMuertes();
        ep.monedasJ1         = j1.getMonedasRecolectadas();
        ep.reapXJ1           = nivel.getZonaReaparicionActualJ1().getX();
        ep.reapYJ1           = nivel.getZonaReaparicionActualJ1().getY();
        ep.zonaInterJ1       = nivel.isZonaIntermediaActivadaJ1();
        ep.monedasGuardadasJ1 = nivel.getMonedasGuardadasJ1();

        // Jugador 2
        Jugador j2 = nivel.getJugador2();
        if (j2 != null) {
            ep.skinJ2            = j2.getSkin();
            ep.xJ2               = j2.getX();
            ep.yJ2               = j2.getY();
            ep.muertesJ2         = j2.getMuertes();
            ep.monedasJ2count    = j2.getMonedasRecolectadas();
            ep.reapXJ2           = nivel.getZonaReaparicionActualJ2().getX();
            ep.reapYJ2           = nivel.getZonaReaparicionActualJ2().getY();
            ep.zonaInterJ2       = nivel.isZonaIntermediaActivadaJ2();
            ep.monedasGuardadasJ2 = nivel.getMonedasGuardadasJ2();
        }

        // Monedas pendientes (las que aún NO han sido recolectadas por NADIE)
        for (Moneda m : nivel.getMonedas()) {
            if (!m.estaRecolectada()) {
                ep.monedasPendientes.add(new int[]{m.getX(), m.getY()});
            }
        }

        return ep;
    }

    // ==========================================
    // GUARDAR A ARCHIVO
    // ==========================================

    public void guardar(String rutaArchivo) throws JuegoException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("# Partida guardada - The DOPO Hardest Game");
            pw.println("MODALIDAD:" + modalidad);
            if (perfilMaquina != null) pw.println("PERFIL_MAQUINA:" + perfilMaquina);
            pw.println("CONFIG_ARCHIVO:" + configArchivo);
            pw.println("TIEMPO_RESTANTE:" + tiempoRestante);

            pw.println("J1_SKIN:" + skinJ1);
            pw.println("J1_X:" + xJ1);
            pw.println("J1_Y:" + yJ1);
            pw.println("J1_MUERTES:" + muertesJ1);
            pw.println("J1_MONEDAS:" + monedasJ1);
            pw.println("J1_REAP_X:" + reapXJ1);
            pw.println("J1_REAP_Y:" + reapYJ1);
            pw.println("J1_ZONA_INTER:" + zonaInterJ1);
            pw.println("J1_MONEDAS_GUARDADAS:" + monedasGuardadasJ1);

            if (skinJ2 != null) {
                pw.println("J2_SKIN:" + skinJ2);
                pw.println("J2_X:" + xJ2);
                pw.println("J2_Y:" + yJ2);
                pw.println("J2_MUERTES:" + muertesJ2);
                pw.println("J2_MONEDAS:" + monedasJ2count);
                pw.println("J2_REAP_X:" + reapXJ2);
                pw.println("J2_REAP_Y:" + reapYJ2);
                pw.println("J2_ZONA_INTER:" + zonaInterJ2);
                pw.println("J2_MONEDAS_GUARDADAS:" + monedasGuardadasJ2);
            }

            pw.println("MONEDAS_PENDIENTES:" + serializarMonedas(monedasPendientes));

            ErrorLogger.getInstance().logError("Partida guardada en: " + rutaArchivo);
        } catch (IOException e) {
            throw new JuegoException("Error al guardar partida: " + rutaArchivo, e);
        }
    }

    // ==========================================
    // CARGAR DESDE ARCHIVO
    // ==========================================

    public static EstadoPartida cargar(String rutaArchivo) throws JuegoException {
        EstadoPartida ep = new EstadoPartida();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                int sep = linea.indexOf(':');
                if (sep < 0) continue;
                String clave = linea.substring(0, sep);
                String valor = linea.substring(sep + 1);
                switch (clave) {
                    case "MODALIDAD":             ep.modalidad = valor; break;
                    case "PERFIL_MAQUINA":        ep.perfilMaquina = valor; break;
                    case "CONFIG_ARCHIVO":        ep.configArchivo = valor; break;
                    case "TIEMPO_RESTANTE":       ep.tiempoRestante = Integer.parseInt(valor); break;
                    case "J1_SKIN":               ep.skinJ1 = valor; break;
                    case "J1_X":                  ep.xJ1 = Integer.parseInt(valor); break;
                    case "J1_Y":                  ep.yJ1 = Integer.parseInt(valor); break;
                    case "J1_MUERTES":            ep.muertesJ1 = Integer.parseInt(valor); break;
                    case "J1_MONEDAS":            ep.monedasJ1 = Integer.parseInt(valor); break;
                    case "J1_REAP_X":             ep.reapXJ1 = Integer.parseInt(valor); break;
                    case "J1_REAP_Y":             ep.reapYJ1 = Integer.parseInt(valor); break;
                    case "J1_ZONA_INTER":         ep.zonaInterJ1 = Boolean.parseBoolean(valor); break;
                    case "J1_MONEDAS_GUARDADAS":  ep.monedasGuardadasJ1 = Integer.parseInt(valor); break;
                    case "J2_SKIN":               ep.skinJ2 = valor; break;
                    case "J2_X":                  ep.xJ2 = Integer.parseInt(valor); break;
                    case "J2_Y":                  ep.yJ2 = Integer.parseInt(valor); break;
                    case "J2_MUERTES":            ep.muertesJ2 = Integer.parseInt(valor); break;
                    case "J2_MONEDAS":            ep.monedasJ2count = Integer.parseInt(valor); break;
                    case "J2_REAP_X":             ep.reapXJ2 = Integer.parseInt(valor); break;
                    case "J2_REAP_Y":             ep.reapYJ2 = Integer.parseInt(valor); break;
                    case "J2_ZONA_INTER":         ep.zonaInterJ2 = Boolean.parseBoolean(valor); break;
                    case "J2_MONEDAS_GUARDADAS":  ep.monedasGuardadasJ2 = Integer.parseInt(valor); break;
                    case "MONEDAS_PENDIENTES":    ep.monedasPendientes = deserializarMonedas(valor); break;
                }
            }
            ErrorLogger.getInstance().logError("Partida cargada desde: " + rutaArchivo);
        } catch (FileNotFoundException e) {
            throw new JuegoException("Archivo de partida no encontrado: " + rutaArchivo, e);
        } catch (IOException e) {
            throw new JuegoException("Error al leer partida: " + rutaArchivo, e);
        } catch (NumberFormatException e) {
            throw new JuegoException("Formato inválido en partida: " + rutaArchivo, e);
        }
        return ep;
    }

    // ==========================================
    // RESTAURAR EL JUEGO DESDE ESTE ESTADO
    // ==========================================

    public Juego restaurar() throws JuegoException {
        Configuracion config = Configuracion.cargarDesdeArchivo(configArchivo);
        config.setTiempoLimite(tiempoRestante);

        Juego juego;
        if ("PVM".equals(modalidad)) {
            PerfilMaquina perfil = "Experta".equals(perfilMaquina)
                    ? new MaquinaExperta()
                    : new MaquinaAleatoria();
            juego = new Juego(modalidad, perfil);
        } else {
            juego = new Juego(modalidad);
        }

        juego.iniciarJuego(config, skinJ1, skinJ2);

        Nivel nivel = juego.getNivelActual();

        // Restaurar estado J1
        Jugador j1 = nivel.getJugador1();
        j1.setX(xJ1);
        j1.setY(yJ1);
        j1.setMuertes(muertesJ1);
        j1.setMonedasRecolectadas(monedasJ1);

        // Restaurar estado J2
        Jugador j2 = nivel.getJugador2();
        if (j2 != null && skinJ2 != null) {
            j2.setX(xJ2);
            j2.setY(yJ2);
            j2.setMuertes(muertesJ2);
            j2.setMonedasRecolectadas(monedasJ2count);
        }

        // Restaurar monedas pendientes (compartidas)
        nivel.restaurarEstado(
                tiempoRestante,
                new Zona(reapXJ1, reapYJ1, Zona.TIPO_INICIO),
                (skinJ2 != null) ? new Zona(reapXJ2, reapYJ2, Zona.TIPO_INICIO) : null,
                zonaInterJ1, monedasGuardadasJ1,
                zonaInterJ2, monedasGuardadasJ2,
                monedasPendientes
        );

        return juego;
    }

    // ==========================================
    // UTILIDADES
    // ==========================================

    private String serializarMonedas(List<int[]> monedas) {
        if (monedas.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < monedas.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(monedas.get(i)[0]).append(",").append(monedas.get(i)[1]);
        }
        return sb.toString();
    }

    private static List<int[]> deserializarMonedas(String valor) {
        List<int[]> lista = new ArrayList<>();
        if (valor == null || valor.trim().isEmpty()) return lista;
        for (String par : valor.split("\\|")) {
            String[] xy = par.split(",");
            if (xy.length == 2) {
                lista.add(new int[]{Integer.parseInt(xy[0].trim()), Integer.parseInt(xy[1].trim())});
            }
        }
        return lista;
    }

    // Getters
    public String getModalidad()      { return modalidad; }
    public String getPerfilMaquina()  { return perfilMaquina; }
    public String getConfigArchivo()  { return configArchivo; }
    public String getSkinJ1()         { return skinJ1; }
    public String getSkinJ2()         { return skinJ2; }
}