package dominio;

import java.io.*;
import java.util.*;

public class Configuracion {

    private List<Zona> zonas;
    private List<Moneda> monedas;
    private List<Enemigo> enemigos;
    private int tiempoLimite;
    private int ancho;
    private int alto;
    private List<int[]> paredes;
    private String nombreNivel;
    private String nombreArchivo;
    private List<FuenteVida> fuentesVida;
    private List<Bomba> bombas;

    public Configuracion() {
        this.zonas = new ArrayList<>();
        this.monedas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.paredes = new ArrayList<>();
        this.fuentesVida = new ArrayList<>();
        this.bombas = new ArrayList<>();
        this.tiempoLimite = 60;
        this.ancho = 20;
        this.alto = 15;
        this.nombreNivel = "Sin nombre";
    }

    public static Configuracion cargarDesdeArchivo(String rutaArchivo) throws JuegoException {
        Configuracion config = new Configuracion();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(":");
                if (partes.length < 2) continue;

                String clave = partes[0];
                String valor = partes[1];

                switch (clave) {
                    case "NOMBRE":
                        config.nombreNivel = valor;
                        break;
                    case "DIMENSIONES":
                        String[] dims = valor.split(",");
                        config.ancho = Integer.parseInt(dims[0]);
                        config.alto = Integer.parseInt(dims[1]);
                        break;
                    case "TIEMPO":
                        config.tiempoLimite = Integer.parseInt(valor);
                        break;
                    case "ZONA":
                        String[] zonaData = valor.split(",");
                        String tipoZona = zonaData[0];
                        int xZona = Integer.parseInt(zonaData[1]);
                        int yZona = Integer.parseInt(zonaData[2]);
                        config.zonas.add(new Zona(xZona, yZona, tipoZona));
                        break;
                    case "MONEDA":
                        String[] monedaData = valor.split(",");
                        int xMoneda = Integer.parseInt(monedaData[0]);
                        int yMoneda = Integer.parseInt(monedaData[1]);
                        config.monedas.add(new Moneda(xMoneda, yMoneda));
                        break;
                    case "MONEDA_SKIN":
                        // Formato: MONEDA_SKIN:X,Y,SKIN,DURACION
                        // Ejemplo:  MONEDA_SKIN:7,5,Azul,30
                        String[] skinData = valor.split(",");
                        int xSkin = Integer.parseInt(skinData[0]);
                        int ySkin = Integer.parseInt(skinData[1]);
                        String skinOtorgado = skinData[2];
                        int duracion = Integer.parseInt(skinData[3]);
                        config.monedas.add(new MonedaSkin(xSkin, ySkin, skinOtorgado, duracion));
                        break;
                    case "FUENTE_VIDA":
                        String[] fuenteData = valor.split(",");
                        int xFuente = Integer.parseInt(fuenteData[0]);
                        int yFuente = Integer.parseInt(fuenteData[1]);
                        config.fuentesVida.add(new FuenteVida(xFuente, yFuente, 1, 1));
                        break;
                    case "BOMBA":
                        String[] bombaData = valor.split(",");
                        int xBomba = Integer.parseInt(bombaData[0]);
                        int yBomba = Integer.parseInt(bombaData[1]);
                        config.bombas.add(new Bomba(xBomba, yBomba, 1, 1));
                        break;
                    case "ENEMIGO":
                        String[] enemigoData = valor.split(",");
                        String tipoEnemigo = enemigoData[0];

                        switch (tipoEnemigo) {
                            case "BASICO":
                                int xBasico = Integer.parseInt(enemigoData[1]);
                                int yBasico = Integer.parseInt(enemigoData[2]);
                                boolean horiz = enemigoData[3].equals("HORIZONTAL");
                                EnemigoBasico enemBasico = new EnemigoBasico(xBasico, yBasico, horiz, config.ancho, config.alto);
                                config.enemigos.add(enemBasico);
                                break;
                            case "RAPIDO":
                                int xRapido = Integer.parseInt(enemigoData[1]);
                                int yRapido = Integer.parseInt(enemigoData[2]);
                                boolean horizRapido = enemigoData[3].equals("HORIZONTAL");
                                EnemigoRapido enemRapido = new EnemigoRapido(xRapido, yRapido, horizRapido, config.ancho, config.alto);
                                config.enemigos.add(enemRapido);
                                break;
                            case "PATRULLERO":
                                int xPat = Integer.parseInt(enemigoData[1]);
                                int yPat = Integer.parseInt(enemigoData[2]);
                                String patron = enemigoData[3];
                                int radio = Integer.parseInt(enemigoData[4]);
                                EnemigoPatrullero enemPat = new EnemigoPatrullero(xPat, yPat, patron, radio, config.ancho, config.alto);
                                config.enemigos.add(enemPat);
                                break;
                            case "PERSEGUIDOR":
                                int xPer = Integer.parseInt(enemigoData[1]);
                                int yPer = Integer.parseInt(enemigoData[2]);
                                EnemigoPerseguidor enemPer = new EnemigoPerseguidor(xPer, yPer, null, config.ancho, config.alto);
                                config.enemigos.add(enemPer);
                                break;
                        }
                        break;
                    case "PARED":
                        String[] paredData = valor.split(",");
                        int xPared = Integer.parseInt(paredData[0]);
                        int yPared = Integer.parseInt(paredData[1]);
                        config.paredes.add(new int[]{xPared, yPared});
                        break;
                }
            }

            ErrorLogger.getInstance().logError("Configuración cargada: " + config.nombreNivel);
            config.nombreArchivo = rutaArchivo;

        } catch (FileNotFoundException e) {
            throw new JuegoException("Archivo no encontrado: " + rutaArchivo, e);
        } catch (IOException e) {
            throw new JuegoException("Error al leer archivo: " + rutaArchivo, e);
        } catch (NumberFormatException e) {
            throw new JuegoException("Error de formato en archivo: " + rutaArchivo, e);
        }

        return config;
    }

    public void guardarAArchivo(String rutaArchivo) throws JuegoException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("# Archivo de configuración - The DOPO Hardest Game");
            pw.println("NOMBRE:" + nombreNivel);
            pw.println("DIMENSIONES:" + ancho + "," + alto);
            pw.println("TIEMPO:" + tiempoLimite);

            for (Zona z : zonas) {
                pw.println("ZONA:" + z.getTipo() + "," + z.getX() + "," + z.getY());
            }

            for (Moneda m : monedas) {
                if (!m.estaRecolectada()) {
                    if (m instanceof MonedaSkin) {
                        MonedaSkin ms = (MonedaSkin) m;
                        pw.println("MONEDA_SKIN:" + ms.getX() + "," + ms.getY() + ","
                                + ms.getSkinOtorgado() + "," + ms.getDuracion());
                    } else {
                        pw.println("MONEDA:" + m.getX() + "," + m.getY());
                    }
                }
            }

            for (FuenteVida fv : fuentesVida) {
                if (!fv.isUsada()) {
                    pw.println("FUENTE_VIDA:" + fv.getX() + "," + fv.getY());
                }
            }

            for (Bomba b : bombas) {
                pw.println("BOMBA:" + b.getX() + "," + b.getY());
            }

            for (Enemigo e : enemigos) {
                if (e instanceof EnemigoBasico && !(e instanceof EnemigoRapido)) {
                    EnemigoBasico eb = (EnemigoBasico) e;
                    String dir = eb.isDireccionHorizontal() ? "HORIZONTAL" : "VERTICAL";
                    pw.println("ENEMIGO:BASICO," + e.getX() + "," + e.getY() + "," + dir);
                } else if (e instanceof EnemigoRapido) {
                    EnemigoRapido er = (EnemigoRapido) e;
                    String dir = er.isDireccionHorizontal() ? "HORIZONTAL" : "VERTICAL";
                    pw.println("ENEMIGO:RAPIDO," + e.getX() + "," + e.getY() + "," + dir);
                } else if (e instanceof EnemigoPatrullero) {
                    EnemigoPatrullero ep = (EnemigoPatrullero) e;
                    pw.println("ENEMIGO:PATRULLERO," + e.getX() + "," + e.getY() + "," + ep.getPatron() + "," + ep.getRadio());
                } else if (e instanceof EnemigoPerseguidor) {
                    pw.println("ENEMIGO:PERSEGUIDOR," + e.getX() + "," + e.getY());
                }
            }

            for (int[] pared : paredes) {
                pw.println("PARED:" + pared[0] + "," + pared[1]);
            }

        } catch (IOException e) {
            throw new JuegoException("Error al guardar archivo: " + rutaArchivo, e);
        }
    }

    // Getters y Setters
    public List<Zona> getZonas() { return zonas; }
    public List<Moneda> getMonedas() { return monedas; }
    public List<Enemigo> getEnemigos() { return enemigos; }
    public int getTiempoLimite() { return tiempoLimite; }
    public void setTiempoLimite(int tiempoLimite) { this.tiempoLimite = tiempoLimite; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public List<int[]> getParedes() { return paredes; }
    public String getNombreNivel() { return nombreNivel; }
    public void setNombreNivel(String nombreNivel) { this.nombreNivel = nombreNivel; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public List<FuenteVida> getFuentesVida() { return fuentesVida; }
    public List<Bomba> getBombas() { return bombas; }

    public Zona getZonaInicio() {
        for (Zona z : zonas) {
            if (z.esZonaInicio()) return z;
        }
        return null;
    }

    public Zona getZonaFinal() {
        for (Zona z : zonas) {
            if (z.getTipo().equals(Zona.TIPO_FINAL)) return z;
        }
        return null;
    }
    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}