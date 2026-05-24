package dominio;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

class DominioTest {

    // ==================== JUGADOR ====================
    @Test
    void testJugadorConstructor() {
        Jugador j = new Jugador(5, 5, Jugador.SKIN_ROJO);
        assertEquals(5, j.getX());
        assertEquals(5, j.getY());
        assertEquals(Jugador.SKIN_ROJO, j.getSkin());
        assertEquals(0, j.getMuertes());
        assertEquals(0, j.getMonedasRecolectadas());
        assertEquals(1.0, j.getVelocidad());
        assertEquals(1.0, j.getTamanio());
        assertFalse(j.isEscudoActivo());
    }

    @Test
    void testJugadorSkinAzul() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_AZUL);
        assertEquals(1.5, j.getVelocidad());
        assertEquals(1.5, j.getTamanio());
        assertEquals(java.awt.Color.BLUE, j.getColorBorde());
    }

    @Test
    void testJugadorSkinVerde() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_VERDE);
        assertEquals(1.0, j.getVelocidad());
        assertTrue(j.isEscudoActivo());
        assertFalse(j.isEscudoUsado());
    }

    @Test
    void testJugadorRecibirGolpeConVidaExtra() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_ROJO);
        j.setVidasExtra(1);
        assertTrue(j.recibirGolpe());
        assertEquals(0, j.getVidasExtra());
    }

    @Test
    void testJugadorRecibirGolpeSkinVerde() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_VERDE);
        assertTrue(j.recibirGolpe());
        assertTrue(j.isEscudoUsado());
        assertEquals(0.7, j.getVelocidad());
        assertFalse(j.recibirGolpe());
    }

    @Test
    void testJugadorMover() {
        Jugador j = new Jugador(5, 5);
        j.mover(1, 0);
        assertEquals(6, j.getX());
        j.mover(0, -1);
        assertEquals(4, j.getY());
    }

    @Test
    void testJugadorMorir() {
        Jugador j = new Jugador(5, 5, Jugador.SKIN_VERDE);
        j.recolectarMoneda();
        j.morir(1, 1);
        assertEquals(1, j.getMuertes());
        assertEquals(1, j.getX());
        assertEquals(1, j.getY());
        assertEquals(0, j.getMonedasRecolectadas());
        assertTrue(j.isEscudoActivo());
        assertFalse(j.isEscudoUsado());
        assertEquals(1.0, j.getVelocidad());
    }

    @Test
    void testJugadorSkinTemporal() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_ROJO);
        j.aplicarSkinTemporal(Jugador.SKIN_AZUL, 5);
        assertTrue(j.isEfectoActivo());
        assertEquals(Jugador.SKIN_AZUL, j.getSkinTemporal());
        assertEquals(1.5, j.getVelocidad());
        for (int i = 0; i < 5; i++) j.actualizarEfectoTemporal();
        assertFalse(j.isEfectoActivo());
        assertEquals(1.0, j.getVelocidad());
    }

    // ==================== MONEDA Y MONEDASKIN ====================
    @Test
    void testMoneda() {
        Moneda m = new Moneda(3, 4);
        assertEquals(3, m.getX());
        assertEquals(4, m.getY());
        assertFalse(m.estaRecolectada());
        m.recolectar();
        assertTrue(m.estaRecolectada());
        assertEquals("🟨", m.getIcono());
    }

    @Test
    void testMonedaSkin() {
        MonedaSkin ms = new MonedaSkin(1, 2, Jugador.SKIN_AZUL, 10);
        assertEquals(Jugador.SKIN_AZUL, ms.getSkinOtorgado());
        assertEquals(10, ms.getDuracion());
        assertEquals("🔵", ms.getIcono());
    }

    // ==================== ENEMIGOS ====================
    @Test
    void testEnemigoBasicoHorizontal() {
        EnemigoBasico eb = new EnemigoBasico(5, 5, true, 20, 15);
        assertEquals(1, eb.getVelocidadBase());
        assertEquals(1, eb.getSentido());
        eb.mover();
        assertEquals(6, eb.getX());
        eb.setX(19);
        eb.mover();
        assertEquals(18, eb.getX());
        assertEquals(-1, eb.getSentido());
    }

    @Test
    void testEnemigoBasicoVertical() {
        EnemigoBasico eb = new EnemigoBasico(5, 5, false, 20, 15);
        eb.mover();
        assertEquals(6, eb.getY());
        eb.setY(14);
        eb.mover();
        assertEquals(13, eb.getY());
    }

    @Test
    void testEnemigoRapido() {
        EnemigoRapido er = new EnemigoRapido(5, 5, true, 20, 15);
        assertEquals(2, er.getVelocidadBase());
        er.mover();
        assertEquals(7, er.getX());
    }

    @Test
    void testEnemigoPatrulleroCircular() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_CIRCULAR, 5, 20, 15);
        boolean seMovio = false;
        for (int i = 0; i < 10; i++) {
            ep.mover();
            if (ep.getX() != 10 || ep.getY() != 10) {
                seMovio = true;
                break;
            }
        }
        assertTrue(seMovio, "El enemigo patrullero debería moverse después de varios ciclos");
    }

    @Test
    void testEnemigoPatrulleroCuadrado() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_CUADRADO, 5, 20, 15);
        ep.mover();
        assertNotNull(ep.getPatron());
        assertEquals(5, ep.getRadio());
    }

    @Test
    void testEnemigoPerseguidor() {
        Jugador objetivo = new Jugador(15, 15);
        EnemigoPerseguidor ep = new EnemigoPerseguidor(10, 10, objetivo, 20, 15);
        int distInicial = Math.abs(ep.getX() - 15) + Math.abs(ep.getY() - 15);
        ep.mover();
        int distFinal = Math.abs(ep.getX() - 15) + Math.abs(ep.getY() - 15);
        assertTrue(distFinal < distInicial);
    }

    // ==================== CONFIGURACION ====================
    @TempDir
    Path tempDir;

    @Test
    void testConfiguracionGuardarYCargar() throws JuegoException, IOException {
        Configuracion cfg = new Configuracion();
        cfg.setNombreNivel("Test");
        cfg.setTiempoLimite(90);
        cfg.setAncho(20);
        cfg.setAlto(15);
        cfg.getZonas().add(new Zona(1, 1, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(18, 7, Zona.TIPO_FINAL));
        cfg.getMonedas().add(new Moneda(5, 5));
        cfg.getMonedas().add(new MonedaSkin(7, 7, "Azul", 20));
        cfg.getEnemigos().add(new EnemigoBasico(3, 3, true, 20, 15));
        cfg.getEnemigos().add(new EnemigoRapido(10, 10, false, 20, 15));
        cfg.getEnemigos().add(new EnemigoPatrullero(12, 12, "CIRCULAR", 4, 20, 15));
        cfg.getFuentesVida().add(new FuenteVida(8, 8, 1, 1));
        cfg.getBombas().add(new Bomba(9, 9, 1, 1));
        cfg.getParedes().add(new int[]{4, 4});

        Path archivo = tempDir.resolve("test_config.txt");
        cfg.guardarAArchivo(archivo.toString());

        Configuracion cargada = Configuracion.cargarDesdeArchivo(archivo.toString());
        assertEquals("Test", cargada.getNombreNivel());
        assertEquals(90, cargada.getTiempoLimite());
        assertEquals(2, cargada.getZonas().size());
        assertEquals(2, cargada.getMonedas().size());
        assertEquals(3, cargada.getEnemigos().size());
        assertEquals(1, cargada.getFuentesVida().size());
        assertEquals(1, cargada.getBombas().size());
        assertEquals(1, cargada.getParedes().size());
    }

    @Test
    void testConfiguracionCargarArchivoInexistente() {
        assertThrows(JuegoException.class, () -> Configuracion.cargarDesdeArchivo("no_existe.txt"));
    }

    // ==================== NIVEL ====================
    private Configuracion crearConfigBasica() {
        Configuracion cfg = new Configuracion();
        cfg.setAncho(20);
        cfg.setAlto(15);
        cfg.getZonas().add(new Zona(2, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(17, 7, Zona.TIPO_FINAL));
        cfg.getMonedas().add(new Moneda(5, 5));
        cfg.getMonedas().add(new Moneda(5, 6));
        cfg.getEnemigos().add(new EnemigoBasico(8, 3, true, 20, 15));
        return cfg;
    }

    @Test
    void testNivelRecoleccionMoneda() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(0, 0, Jugador.SKIN_ROJO);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        // Recolocar al jugador sobre la moneda (5,5)
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMonedasRecolectadas());
        assertEquals(1, nivel.getMonedas().size());
    }

    @Test
    void testNivelMonedaSkin() {
        Configuracion cfg = crearConfigBasica();
        cfg.getMonedas().clear();
        cfg.getMonedas().add(new MonedaSkin(5, 5, Jugador.SKIN_AZUL, 30));
        Jugador j1 = new Jugador(0, 0, Jugador.SKIN_ROJO);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(Jugador.SKIN_AZUL, j1.getSkinTemporal());
        assertTrue(j1.isEfectoActivo());
    }

    @Test
    void testNivelMuerteJugador() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(0, 0, Jugador.SKIN_ROJO);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        // Colocar al jugador en la misma posición que el enemigo (8,3)
        j1.setX(8);
        j1.setY(3);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMuertes());
        assertEquals(cfg.getZonaInicio().getX(), j1.getX());
        assertEquals(cfg.getZonaInicio().getY(), j1.getY());
        assertEquals(2, nivel.getMonedas().size());
    }

    @Test
    void testNivelZonaIntermedia() {
        Configuracion cfg = crearConfigBasica();
        cfg.getZonas().add(new Zona(10, 7, Zona.TIPO_INTERMEDIA));
        Jugador j1 = new Jugador(0, 0);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        // Recolectar una moneda
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMonedasRecolectadas());
        // Ir a zona intermedia
        j1.setX(10);
        j1.setY(7);
        nivel.verificarColisiones();
        assertTrue(nivel.isZonaIntermediaActivadaJ1());
        assertEquals(1, nivel.getMonedasGuardadasJ1());
        // Morir (colocar en posición del enemigo)
        j1.setX(8);
        j1.setY(3);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMonedasRecolectadas()); // debe conservar la moneda
        assertEquals(1, nivel.getMonedas().size());   // una moneda pendiente
    }

    @Test
    void testNivelVictoria() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(0, 0);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        j1.setMonedasRecolectadas(2);
        j1.setX(17);
        j1.setY(7);
        nivel.verificarColisiones();
        assertEquals(Nivel.ESTADO_GANADO, nivel.getEstado());
    }

    @Test
    void testNivelPVPCompartido() {
        Configuracion cfg = crearConfigBasica();
        cfg.getZonas().clear();
        cfg.getZonas().add(new Zona(2, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(17, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(10, 7, Zona.TIPO_FINAL));
        cfg.getMonedas().clear();
        cfg.getMonedas().add(new Moneda(5, 5));
        Jugador j1 = new Jugador(0, 0, "Rojo");
        Jugador j2 = new Jugador(0, 0, "Azul");
        Nivel nivel = new Nivel(cfg, j1, j2, "PVP");
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMonedasRecolectadas());
        assertEquals(0, j2.getMonedasRecolectadas());
        assertTrue(nivel.getMonedas().isEmpty());
        j2.setX(5);
        j2.setY(5);
        nivel.verificarColisiones();
        assertEquals(0, j2.getMonedasRecolectadas());
    }

    @Test
    void testNivelBombaDestruyeJugador() {
        Configuracion cfg = crearConfigBasica();
        cfg.getBombas().add(new Bomba(5, 5, 1, 1));
        Jugador j1 = new Jugador(0, 0);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMuertes());
        assertEquals(cfg.getZonaInicio().getX(), j1.getX());
    }

    @Test
    void testNivelFuenteVida() {
        Configuracion cfg = crearConfigBasica();
        cfg.getFuentesVida().add(new FuenteVida(5, 5, 1, 1));
        Jugador j1 = new Jugador(0, 0);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        j1.setX(5);
        j1.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getVidasExtra());
        assertTrue(nivel.getFuentesVida().isEmpty());
    }

    // ==================== JUEGO ====================
    private Juego crearJuegoDePruebaConArchivo(String modalidad, Path configPath) throws JuegoException, IOException {
        Configuracion cfg = crearConfigBasica();
        cfg.setNombreArchivo(configPath.toString());
        cfg.guardarAArchivo(configPath.toString());
        Juego juego = new Juego(modalidad);
        juego.iniciarJuego(cfg, "Rojo", null);
        return juego;
    }

    @Test
    void testJuegoIniciarPlayer() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("nivel_test.txt");
        Juego juego = crearJuegoDePruebaConArchivo("PLAYER", configFile);
        assertTrue(juego.isCorriendo());
        assertFalse(juego.isPausado());
        assertEquals("PLAYER", juego.getModalidad());
    }

    @Test
    void testJuegoMovimiento() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("nivel_test.txt");
        Juego juego = crearJuegoDePruebaConArchivo("PLAYER", configFile);
        int x0 = juego.getNivelActual().getJugador1().getX();
        juego.moverJugador1(1, 0);
        assertEquals(x0 + 1, juego.getNivelActual().getJugador1().getX());
    }

    @Test
    void testJuegoPausa() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("nivel_test.txt");
        Juego juego = crearJuegoDePruebaConArchivo("PLAYER", configFile);
        juego.pausarJuego();
        assertTrue(juego.isPausado());
        juego.reanudarJuego();
        assertFalse(juego.isPausado());
    }

    @Test
    void testJuegoGuardarCargarPartida() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("nivel_test.txt");
        Juego juego = crearJuegoDePruebaConArchivo("PLAYER", configFile);
        juego.moverJugador1(1, 0);
        Path saveFile = tempDir.resolve("partida.save");
        juego.guardarPartida(saveFile.toString());
        EstadoPartida estado = EstadoPartida.cargar(saveFile.toString());
        Juego juegoCargado = estado.restaurar();
        assertEquals(juego.getNivelActual().getJugador1().getX(),
                juegoCargado.getNivelActual().getJugador1().getX());
    }

    @Test
    void testEstadoPartidaSerializacion() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("nivel_test.txt");
        Juego juego = crearJuegoDePruebaConArchivo("PLAYER", configFile);
        juego.moverJugador1(1, 0);
        EstadoPartida estado = EstadoPartida.capturar(juego, configFile.toString());
        Path file = tempDir.resolve("estado.save");
        estado.guardar(file.toString());
        EstadoPartida cargado = EstadoPartida.cargar(file.toString());
        assertEquals(estado.getModalidad(), cargado.getModalidad());
        assertEquals(estado.getSkinJ1(), cargado.getSkinJ1());
    }

    // ==================== MAQUINAS ====================
    @Test
    void testMaquinaAleatoria() {
        MaquinaAleatoria maq = new MaquinaAleatoria();
        Configuracion cfg = crearConfigBasica();
        Jugador j = new Jugador(10, 10);
        List<Moneda> monedas = new ArrayList<>();
        int[] mov = maq.decidirMovimiento(j, cfg, monedas);
        assertTrue(mov[0] >= -1 && mov[0] <= 1);
        assertTrue(mov[1] >= -1 && mov[1] <= 1);
    }

    @Test
    void testMaquinaExperta() {
        MaquinaExperta maq = new MaquinaExperta();
        Configuracion cfg = crearConfigBasica();
        cfg.getMonedas().clear();
        cfg.getMonedas().add(new Moneda(15, 7));
        Jugador j = new Jugador(10, 7);
        List<Moneda> monedas = cfg.getMonedas();
        int[] mov = maq.decidirMovimiento(j, cfg, monedas);
        assertEquals(1, mov[0]);
        assertEquals(0, mov[1]);
    }

    @Test
    void testMaquinaExpertaSinCamino() {
        MaquinaExperta maq = new MaquinaExperta();
        Configuracion cfg = crearConfigBasica();
        cfg.getParedes().clear();
        cfg.getParedes().add(new int[]{11,7});
        cfg.getParedes().add(new int[]{9,7});
        cfg.getParedes().add(new int[]{10,6});
        cfg.getParedes().add(new int[]{10,8});
        Jugador j = new Jugador(10, 7);
        List<Moneda> monedas = new ArrayList<>();
        monedas.add(new Moneda(15,7));
        int[] mov = maq.decidirMovimiento(j, cfg, monedas);
        assertNotNull(mov);
    }

    // ==================== EXCEPCIONES Y LOGGER ====================
    @Test
    void testJuegoException() {
        JuegoException ex = new JuegoException("Error test");
        assertEquals("Error test", ex.getMessage());
        assertEquals(1000, ex.getCodigoError());
        assertNotNull(ex.getTimestamp());
    }

    @Test
    void testErrorLoggerSingleton() {
        ErrorLogger logger1 = ErrorLogger.getInstance();
        ErrorLogger logger2 = ErrorLogger.getInstance();
        assertSame(logger1, logger2);
        logger1.logError("Prueba log");
        logger1.guardarLog();
        logger1.limpiarLog();
    }

    // ==================== ZONA ====================
    @Test
    void testZona() {
        Zona z = new Zona(5, 5, Zona.TIPO_INTERMEDIA);
        assertTrue(z.esZonaSegura());
        assertFalse(z.esZonaFinal());
        assertFalse(z.esZonaInicio());
        assertEquals("🟩", z.getIcono());
        z.activar();
        assertTrue(z.isActiva());
    }

    // ==================== ELEMENTOS ESPECIALES ====================
    @Test
    void testBomba() {
        Bomba b = new Bomba(1, 1, 32, 32);
        assertFalse(b.isExplotada());
        b.explotar();
        assertTrue(b.isExplotada());
    }

    @Test
    void testFuenteVida() {
        FuenteVida fv = new FuenteVida(2, 2, 32, 32);
        assertFalse(fv.isUsada());
        fv.usar();
        assertTrue(fv.isUsada());
    }

    @Test
    void testEntidadColision() {
        Jugador j1 = new Jugador(5, 5);
        Jugador j2 = new Jugador(5, 5);
        assertTrue(j1.colisionaCon(j2));
        j2.mover(1, 0);
        assertFalse(j1.colisionaCon(j2));
    }
    @Test
    void testJugadorVidasExtraMultiples() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_ROJO);
        j.setVidasExtra(3);
        assertTrue(j.recibirGolpe());
        assertEquals(2, j.getVidasExtra());
        assertTrue(j.recibirGolpe());
        assertEquals(1, j.getVidasExtra());
        assertTrue(j.recibirGolpe());
        assertEquals(0, j.getVidasExtra());
        assertFalse(j.recibirGolpe()); // ya no tiene vidas
    }

    @Test
    void testJugadorSkinTemporalExpiracionPrecisa() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_ROJO);
        j.aplicarSkinTemporal(Jugador.SKIN_VERDE, 3);
        assertTrue(j.isEfectoActivo());
        assertEquals(Jugador.SKIN_VERDE, j.getSkinTemporal());
        j.actualizarEfectoTemporal(); // 2
        j.actualizarEfectoTemporal(); // 1
        assertTrue(j.isEfectoActivo());
        j.actualizarEfectoTemporal(); // 0 -> expira
        assertFalse(j.isEfectoActivo());
        assertNull(j.getSkinTemporal());
        assertEquals(1.0, j.getVelocidad());
    }

    @Test
    void testJugadorSetearAtributos() {
        Jugador j = new Jugador(5, 5);
        j.setMuertes(10);
        j.setMonedasRecolectadas(7);
        j.setVelocidad(2.5);
        j.setTamanio(2.0);
        j.setVidasExtra(2);
        j.setColorBorde(java.awt.Color.MAGENTA);
        assertEquals(10, j.getMuertes());
        assertEquals(7, j.getMonedasRecolectadas());
        assertEquals(2.5, j.getVelocidad());
        assertEquals(2.0, j.getTamanio());
        assertEquals(2, j.getVidasExtra());
        assertEquals(java.awt.Color.MAGENTA, j.getColorBorde());
    }
    @Test
    void testEnemigoPatrulleroCuadradoMovimiento() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_CUADRADO, 5, 20, 15);
        int x0 = ep.getX(), y0 = ep.getY();
        // Debe moverse después de varios pasos
        boolean seMovio = false;
        for (int i = 0; i < 40; i++) {
            ep.mover();
            if (ep.getX() != x0 || ep.getY() != y0) {
                seMovio = true;
                break;
            }
        }
        assertTrue(seMovio);
    }

    @Test
    void testEnemigoPatrulleroOcho() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_OCHO, 5, 20, 15);
        ep.mover(); // solo verificar que no lance excepción
        assertNotNull(ep.getPatron());
    }

    @Test
    void testEnemigoPerseguidorConObjetivoNulo() {
        EnemigoPerseguidor ep = new EnemigoPerseguidor(5, 5, null, 20, 15);
        ep.mover(); // no debe hacer nada
        assertEquals(5, ep.getX());
        assertEquals(5, ep.getY());
    }

    @Test
    void testEnemigoBasicoRebotar() {
        EnemigoBasico eb = new EnemigoBasico(10, 10, true, 20, 15);
        int sentidoOriginal = eb.getSentido();
        eb.rebotar();
        assertEquals(-sentidoOriginal, eb.getSentido());
    }
    @Test
    void testNivelPVMVictoriaMaquina() {
        Configuracion cfg = crearConfigBasica();
        cfg.getZonas().clear();
        cfg.getZonas().add(new Zona(2, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(17, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(10, 7, Zona.TIPO_FINAL));
        cfg.getMonedas().clear();
        cfg.getMonedas().add(new Moneda(10, 7)); // una moneda en la zona final
        Jugador j1 = new Jugador(2, 7, "Rojo");
        Jugador j2 = new Jugador(17, 7, "Azul");
        Nivel nivel = new Nivel(cfg, j1, j2, "PVM");
        // J2 (máquina) recolecta la moneda y va a la final
        j2.setX(10);
        j2.setY(7);
        nivel.verificarColisiones();
        assertEquals(1, j2.getMonedasRecolectadas());
        // Llega a la final
        nivel.verificarVictoria();
        assertEquals(Nivel.ESTADO_GANADO, nivel.getEstado());
        assertEquals("MÁQUINA", nivel.getGanador());
    }

    @Test
    void testNivelTiempoAgotado() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(2, 7);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        nivel.setTiempoRestante(0);
        nivel.verificarTiempoAgotado();
        assertEquals(Nivel.ESTADO_PERDIDO, nivel.getEstado());
    }

    @Test
    void testNivelPausaYReanudacion() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(2, 7);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        assertEquals(Nivel.ESTADO_EN_CURSO, nivel.getEstado());
        nivel.pausar();
        assertEquals(Nivel.ESTADO_PAUSADO, nivel.getEstado());
        nivel.reanudar();
        assertEquals(Nivel.ESTADO_EN_CURSO, nivel.getEstado());
    }

    @Test
    void testNivelColisionJugadoresPVP() {
        Configuracion cfg = crearConfigBasica();
        cfg.getZonas().clear();
        cfg.getZonas().add(new Zona(2, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(3, 7, Zona.TIPO_INICIO)); // muy cerca
        cfg.getZonas().add(new Zona(10, 7, Zona.TIPO_FINAL));
        cfg.getMonedas().clear();
        cfg.getMonedas().add(new Moneda(5,5));
        Jugador j1 = new Jugador(2, 7, "Rojo");
        Jugador j2 = new Jugador(3, 7, "Azul");
        Nivel nivel = new Nivel(cfg, j1, j2, "PVP");
        // Colocar j1 y j2 en la misma celda
        j1.setX(5);
        j1.setY(5);
        j2.setX(5);
        j2.setY(5);
        nivel.verificarColisiones();
        assertEquals(1, j1.getMuertes());
        assertEquals(1, j2.getMuertes());
        // Ambos deben reaparecer en sus zonas de inicio
        assertEquals(2, j1.getX());
        assertEquals(7, j1.getY());
        assertEquals(3, j2.getX());
        assertEquals(7, j2.getY());
    }
    @Test
    void testConfiguracionCargarFormatoInvalido() throws IOException {
        Path archivoInvalido = tempDir.resolve("invalido.txt");
        Files.write(archivoInvalido, "ZONA:INICIO,abc,def".getBytes());
        assertThrows(JuegoException.class, () -> Configuracion.cargarDesdeArchivo(archivoInvalido.toString()));
    }

    @Test
    void testConfiguracionGuardarConMonedasRecolectadas() throws JuegoException, IOException {
        Configuracion cfg = crearConfigBasica();
        cfg.getMonedas().get(0).recolectar(); // marcar como recolectada
        Path archivo = tempDir.resolve("config_con_recolectada.txt");
        cfg.guardarAArchivo(archivo.toString());
        // Al guardar, las monedas recolectadas no deben aparecer en el archivo
        String contenido = Files.readString(archivo);
        assertFalse(contenido.contains("MONEDA:5,5")); // esa moneda ya no se guarda
    }

    @Test
    void testEstadoPartidaSerializacionCompleta() throws JuegoException, IOException {
        // Crear un nivel con todas las características y guardar estado
        Configuracion cfg = crearConfigBasica();
        cfg.getFuentesVida().add(new FuenteVida(3,3,1,1));
        cfg.getBombas().add(new Bomba(4,4,1,1));
        Path cfgFile = tempDir.resolve("nivel_completo.txt");
        cfg.guardarAArchivo(cfgFile.toString());

        Juego juego = new Juego("PLAYER");
        juego.iniciarJuego(cfg, "Verde", null);
        juego.moverJugador1(1, 0);
        EstadoPartida estado = EstadoPartida.capturar(juego, cfgFile.toString());
        Path saveFile = tempDir.resolve("completa.save");
        estado.guardar(saveFile.toString());
        EstadoPartida cargado = EstadoPartida.cargar(saveFile.toString());
        assertEquals(estado.getSkinJ1(), cargado.getSkinJ1());
    }
    @Test
    void testErrorLoggerConExcepcion() {
        ErrorLogger logger = ErrorLogger.getInstance();
        Exception ex = new RuntimeException("Fallo simulado");
        logger.logError("Error de prueba", ex);
        // Solo verificar que no lance excepción
    }
    @Test
    void testJuegoExceptionConCodigoYCausa() {
        Exception causa = new IOException("Causa real");
        JuegoException ex = new JuegoException("Mensaje", 500, causa);
        assertEquals(500, ex.getCodigoError());
        assertEquals(causa, ex.getCause());
    }
    @Test
    void testBombaDibujar() {
        // No podemos probar Graphics fácilmente, pero podemos instanciar y llamar a métodos
        Bomba b = new Bomba(10, 10, 32, 32);
        assertFalse(b.isExplotada());
        b.explotar();
        assertTrue(b.isExplotada());
        // Probar getBounds
        java.awt.Rectangle rect = b.getBounds();
        assertEquals(10, rect.x);
        assertEquals(10, rect.y);
        assertEquals(32, rect.width);
        assertEquals(32, rect.height);
        // Probar setAncho/setAlto si existen
        b.setAncho(40);
        b.setAlto(40);
        assertEquals(40, b.getAncho());
        assertEquals(40, b.getAlto());
    }
    @Test
    void testElementoEspecialGetBounds() {
        FuenteVida fv = new FuenteVida(5, 5, 20, 20);
        java.awt.Rectangle rect = fv.getBounds();
        assertEquals(5, rect.x);
        assertEquals(5, rect.y);
        assertEquals(20, rect.width);
        assertEquals(20, rect.height);
    }
    @Test
    void testEnemigoAbstracto() {
        Enemigo e = new EnemigoBasico(0, 0, true, 20, 15);
        e.setVelocidad(5);
        assertEquals(5, e.getVelocidad());
    }
    @Test
    void testEnemigoPatrulleroOchoMovimiento() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_OCHO, 5, 20, 15);
        int x0 = ep.getX(), y0 = ep.getY();
        boolean seMovio = false;
        for (int i = 0; i < 50; i++) {
            ep.mover();
            if (ep.getX() != x0 || ep.getY() != y0) {
                seMovio = true;
                break;
            }
        }
        assertTrue(seMovio);
    }

    @Test
    void testEnemigoPatrulleroCuadradoCompleto() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_CUADRADO, 5, 20, 15);
        // Recorrer suficientes pasos para cubrir los 4 lados
        for (int i = 0; i < 80; i++) {
            ep.mover();
        }
        // Solo verificar que no lance excepción
        assertNotNull(ep.getPatron());
    }
    @Test
    void testJugadorRecibirGolpeSinVidasNiEscudo() {
        Jugador j = new Jugador(0, 0, Jugador.SKIN_ROJO);
        assertFalse(j.recibirGolpe());
    }

    @Test
    void testEnemigoPerseguidorNoSeMueveSiObjetivoEsNull() {
        EnemigoPerseguidor ep = new EnemigoPerseguidor(5, 5, null, 20, 15);
        ep.mover();
        assertEquals(5, ep.getX());
        assertEquals(5, ep.getY());
    }
    @Test
    void testConfiguracionGetters() {
        Configuracion cfg = crearConfigBasica();
        cfg.setNombreArchivo("test.txt");
        assertEquals("test.txt", cfg.getNombreArchivo());
        assertNotNull(cfg.getZonaInicio());
        assertNotNull(cfg.getZonaFinal());
        assertNotNull(cfg.getZonas());
        assertNotNull(cfg.getMonedas());
        assertNotNull(cfg.getEnemigos());
        assertNotNull(cfg.getParedes());
        assertNotNull(cfg.getFuentesVida());
        assertNotNull(cfg.getBombas());
    }
    @Test
    void testNivelRestaurarEstadoConListaVacia() {
        Configuracion cfg = crearConfigBasica();
        Jugador j1 = new Jugador(2, 7);
        Nivel nivel = new Nivel(cfg, j1, "PLAYER");
        List<int[]> listaVacia = new ArrayList<>();
        nivel.restaurarEstado(50, new Zona(1,1, Zona.TIPO_INICIO), null, false, 0, false, 0, listaVacia);
        assertTrue(nivel.getMonedas().isEmpty());
    }


    @Test
    void testJuegoMovimientoInvalidoPared() throws IOException, JuegoException {
        Path configFile = tempDir.resolve("pared.txt");
        Configuracion cfg = crearConfigBasica();
        cfg.getParedes().add(new int[]{3,7});
        cfg.guardarAArchivo(configFile.toString());
        Juego juego = new Juego("PLAYER");
        juego.iniciarJuego(cfg, "Rojo", null);
        int x0 = juego.getNivelActual().getJugador1().getX();
        juego.moverJugador1(1, 0); // hacia la pared (3,7)
        assertEquals(x0, juego.getNivelActual().getJugador1().getX()); // no se mueve
    }
    @Test
    void testBombaExplotarSoloUnaVez() {
        Bomba b = new Bomba(1,1,1,1);
        b.explotar();
        assertTrue(b.isExplotada());
        b.explotar(); // segunda vez no debe cambiar
        assertTrue(b.isExplotada());
    }
    @Test
    void testBombaCompleta() {
        Bomba b = new Bomba(10, 10, 32, 32);
        // Probar getBounds
        java.awt.Rectangle rect = b.getBounds();
        assertEquals(10, rect.x);
        assertEquals(32, rect.width);
        // Probar isExplotada inicial
        assertFalse(b.isExplotada());
        // Probar explotar
        b.explotar();
        assertTrue(b.isExplotada());
        // Probar getAncho / getAlto (si existen)
        assertEquals(32, b.getAncho());
        assertEquals(32, b.getAlto());
        b.setAncho(40);
        b.setAlto(40);
        assertEquals(40, b.getAncho());
        assertEquals(40, b.getAlto());
        // Probar dibujar (aunque no renderice, solo para cubrir el método)
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics g = img.getGraphics();
        b.dibujar(g);
        g.dispose();
    }
    @Test
    void testFuenteVidaCompleta() {
        FuenteVida fv = new FuenteVida(5, 5, 20, 20);
        assertFalse(fv.isUsada());
        // Probar getBounds
        java.awt.Rectangle rect = fv.getBounds();
        assertEquals(5, rect.x);
        assertEquals(20, rect.width);
        // Usar la fuente
        fv.usar();
        assertTrue(fv.isUsada());
        // Probar dibujar (solo para cubrir)
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics g = img.getGraphics();
        fv.dibujar(g);
        g.dispose();
    }
    @Test
    void testEnemigoRapidoCompleto() {
        EnemigoRapido er = new EnemigoRapido(5, 5, true, 20, 15);
        assertEquals(2, er.getVelocidadBase());
        assertEquals("💨", er.getIcono());
        // Mover y verificar que se mueve más rápido
        int x0 = er.getX();
        er.mover();
        assertEquals(x0 + 2, er.getX());
    }
    @Test
    void testElementoEspecialMetodos() {
        // Usamos FuenteVida como concreto
        FuenteVida fv = new FuenteVida(1, 1, 10, 10);
        assertEquals("⭐", fv.getIcono()); // método heredado
        java.awt.Rectangle rect = fv.getBounds();
        assertNotNull(rect);
    }

    @Test
    void testJuegoModoPVPCompleto() throws JuegoException, IOException {
        Path configFile = tempDir.resolve("pvp_real.txt");
        Configuracion cfg = crearConfigBasica();
        cfg.getZonas().clear();
        cfg.getZonas().add(new Zona(2, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(17, 7, Zona.TIPO_INICIO));
        cfg.getZonas().add(new Zona(10, 7, Zona.TIPO_FINAL));
        cfg.guardarAArchivo(configFile.toString());

        Juego juego = new Juego("PVP");
        juego.iniciarJuego(cfg, "Rojo", "Azul");
        // Mover jugador 2 en diagonal
        juego.moverJugador2(1, 1);
        // Mover jugador 1 con movimiento inválido (pared)
        juego.moverJugador1(1, 0);
        // Pausar y reanudar
        juego.pausarJuego();
        assertTrue(juego.isPausado());
        juego.reanudarJuego();
        assertFalse(juego.isPausado());
        // Terminar juego
        juego.terminarJuego();
        assertFalse(juego.isCorriendo());
    }

    @Test
    void testJuegoModoPVMExperta() throws JuegoException, IOException {
        Path configFile = tempDir.resolve("pvm_exp.txt");
        Configuracion cfg = crearConfigBasica();
        cfg.guardarAArchivo(configFile.toString());

        Juego juego = new Juego("PVM", new MaquinaExperta());
        juego.iniciarJuego(cfg, "Rojo", "Azul");
        juego.moverMaquina();
        assertTrue(juego.isCorriendo());
        // Probar actualización de tiempo y enemigos
        juego.actualizarTiempo();
        juego.actualizarEnemigos();
    }


    @Test
    void testJuegoExceptionTodosConstructores() {
        JuegoException ex1 = new JuegoException("Mensaje");
        assertEquals(1000, ex1.getCodigoError());

        Exception causa = new RuntimeException("Causa");
        JuegoException ex2 = new JuegoException("Mensaje", causa);
        assertEquals(1001, ex2.getCodigoError());
        assertEquals(causa, ex2.getCause());

        JuegoException ex3 = new JuegoException("Mensaje", 500, causa);
        assertEquals(500, ex3.getCodigoError());
        assertEquals(causa, ex3.getCause());
        assertNotNull(ex3.getTimestamp());
    }
    @Test
    void testEnemigoBasicoSetters() {
        EnemigoBasico eb = new EnemigoBasico(0, 0, true, 20, 15);
        eb.setDireccionHorizontal(false);
        assertFalse(eb.isDireccionHorizontal());
        eb.setVelocidadBase(3);
        assertEquals(3, eb.getVelocidadBase());
    }

    @Test
    void testEnemigoPatrulleroVelocidadAngular() {
        EnemigoPatrullero ep = new EnemigoPatrullero(10, 10, EnemigoPatrullero.PATRON_CIRCULAR, 5, 45, 20, 15);
        ep.mover(); // debe cambiar
        assertNotEquals(10, ep.getX());
    }
    @Test
    void testJuegoExceptionCompleto() {
        Exception causa = new IOException("fallo");
        JuegoException ex = new JuegoException("msg", 123, causa);
        assertEquals(123, ex.getCodigoError());
        assertEquals(causa, ex.getCause());
        assertNotNull(ex.getTimestamp());
    }
}