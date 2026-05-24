# 🎮 The DOPO Hardest Game

**Universidad Escuela Colombiana de Ingeniería**  
**Programación Orientada por Objetos — 2026-1**

**Autores:** Diego Alejandro Mesa · Andrés Felipe Rubio

---

## 📋 Descripción del Proyecto

The DOPO Hardest Game es una versión mejorada del clásico *The World's Hardest Game*. El jugador controla un cuadrado que debe recolectar todas las monedas y llegar a la zona verde final, esquivando enemigos en movimiento.

---

## ✨ Características implementadas

| Característica | Descripción |
|----------------|-------------|
| **3 Modalidades** | Player (1 jugador), PvP (2 jugadores), PvM (vs IA) |
| **3 Skins de jugador** | Rojo (estándar), Azul (rápido + grande), Verde (resistente con escudo) |
| **4 Tipos de enemigos** | Básico, Rápido, Patrullero (circular/cuadrado/ocho), Perseguidor (IA) |
| **2 Perfiles de IA** | Aleatoria (movimientos al azar), Experta (BFS pathfinding) |
| **Elementos especiales** | Bombas (destruyen jugadores/enemigos), Fuentes de vida (vidas extra) |
| **Zonas seguras** | Inicio, Intermedia (checkpoint), Final |
| **Persistencia** | Guardar/Cargar partida (.save), Configuraciones desde .txt |
| **Sistema de logs** | Registro de errores y eventos del juego |

---

## 🧠 Temas de POO implementados

| Tema | Implementación |
|------|----------------|
| **Herencia** | `Enemigo` → `EnemigoBasico`, `EnemigoRapido`, `EnemigoPatrullero`, `EnemigoPerseguidor`<br>`Entidad` → `Jugador`, `Moneda`, `Zona`, `ElementoEspecial` |
| **Polimorfismo** | Método `mover()` en cada enemigo, `dibujar()` en elementos especiales |
| **Encapsulamiento** | Atributos `private` con getters/setters en todas las clases |
| **Interfaces** | `Movible` (para entidades que se mueven), `PerfilMaquina` (para IAs) |
| **Clases abstractas** | `Enemigo`, `Entidad`, `ElementoEspecial` |
| **Singleton** | `ErrorLogger` (única instancia global) |
| **Manejo de excepciones** | `JuegoException` personalizada |
| **Colecciones** | `ArrayList<>`, `List<>`, `Iterator` para monedas, enemigos, bombas |
| **Archivos** | `BufferedReader`, `PrintWriter`, `FileReader` para configuración y guardado |
| **Patrón BFS** | `MaquinaExperta` usa búsqueda en anchura para encontrar camino óptimo |

---

## 🗂️ Estructura del Proyecto

```
src/
├── dominio/
│   ├── Entidad.java            # Clase base abstracta
│   ├── Jugador.java
│   ├── Enemigo.java            # Clase abstracta
│   ├── EnemigoBasico.java
│   ├── EnemigoRapido.java
│   ├── EnemigoPatrullero.java
│   ├── EnemigoPerseguidor.java
│   ├── Moneda.java
│   ├── MonedaSkin.java
│   ├── Zona.java
│   ├── Bomba.java
│   ├── FuenteVida.java
│   ├── ElementoEspecial.java
│   ├── Nivel.java
│   ├── Juego.java
│   ├── EstadoPartida.java
│   ├── Configuracion.java
│   ├── ErrorLogger.java        # Singleton
│   ├── JuegoException.java
│   ├── Movible.java            # Interfaz
│   ├── PerfilMaquina.java      # Interfaz
│   ├── MaquinaAleatoria.java
│   └── MaquinaExperta.java     # BFS pathfinding
├── presentacion/
│   ├── VentanaPrincipal.java
│   ├── PanelJuego.java
│   ├── PanelInformacion.java
│   ├── PanelBotones.java
│   ├── DialogoConfiguracion.java
│   ├── DialogoFinJuego.java
│   ├── ControladorTeclado.java
│   ├── Renderizador.java
│   └── TemporizadorJuego.java
└── test/
    └── dominioTest.java
```

---

## 🔍 Informe PMD

PMD es una herramienta de análisis de código estático que detecta malas prácticas, problemas de estilo, complejidad ciclomática alta y errores potenciales.

### Resultado: 2186 violaciones en 33 archivos · 8 rule sets

![Informe PMD](images/pmd_report.png)

| Categoría | Violaciones | Detalle |
|-----------|-------------|---------|
| **bestpractices** | 370 | 6 errores · 354 advertencias · 10 info |
| **codestyle** | 1182 | 1170 errores · 12 advertencias |
| **design** | 184 | 1 error · 183 advertencias |
| **documentation** | 309 | 309 advertencias |
| **errorprone** | 105 | 49 errores · 56 advertencias |
| **multithreading** | 1 | `NonThreadSafeSingleton` |
| **performance** | 35 | `AvoidFileStream`(6), `AppendCharacterWithChar`(2), `AvoidInstantiatingObjectsInLoops`(26), `InefficientEmptyStringCheck`(1) |
| **Total** | **2186** | en 33 archivos escaneados |

### Análisis de las violaciones más relevantes

- **`codestyle` (1182):** La mayoría son convenciones de nombres y formato. Son advertencias de estilo que no afectan la funcionalidad.
- **`AvoidInstantiatingObjectsInLoops` (26):** Se crean objetos dentro de bucles del motor de juego; es consecuencia directa de la lógica de renderizado y colisiones en tiempo real.
- **`AvoidFileStream` (6):** Uso de `FileInputStream`/`FileOutputStream`; se puede mejorar usando `Files.newBufferedReader()` en versiones futuras.
- **`NonThreadSafeSingleton` (1):** El `ErrorLogger` (Singleton) no es thread-safe; aceptable dado que el juego corre en un solo hilo principal.
- **`documentation` (309):** Faltan Javadocs en varios métodos; las clases están comentadas internamente.

---

## 📊 Cobertura de Pruebas (Coverage)

Las pruebas unitarias fueron ejecutadas con JaCoCo desde IntelliJ IDEA sobre el paquete `dominio`.

### Resumen general

![Reporte de Cobertura](images/coverage_summary.png)

| Métrica | Resultado |
|---------|-----------|
| **Clases** | 100% (22/22) |
| **Métodos** | 89,6% (266/297) |
| **Ramas** | 64% (445/695) |
| **Líneas** | 89,1% (1466/1646) |

> ✅ **100% de clases cubiertas.** La cobertura de ramas (64%) refleja caminos condicionales del motor de juego difíciles de simular en pruebas unitarias puras (colisiones, IA, renderizado).

---

## 🚀 Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/TheDOPOHardestGame.git
   ```
2. Abrir en IntelliJ IDEA como proyecto Java.
3. Agregar las librerías de `lib/` al classpath del proyecto.
4. Ejecutar `VentanaPrincipal.java` como clase principal.

### Requisitos

- Java 11 o superior
- IntelliJ IDEA (recomendado) o cualquier IDE Java
- JUnit 5 (incluido en `lib/`)

---

## 🧪 Ejecutar pruebas

Desde IntelliJ: clic derecho sobre `dominioTest.java` → **Run 'dominioTest'**.

Para ejecutar PMD manualmente:
```bash
pmd check -d src/ -R rulesets/java/quickstart.xml -f text
```

---

## 📄 Licencia

Proyecto académico — Universidad Escuela Colombiana de Ingeniería, 2026.
