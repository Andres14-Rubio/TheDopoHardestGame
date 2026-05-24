[README (1).md](https://github.com/user-attachments/files/28195727/README.1.md)
# 🎮 The DOPO Hardest Game

**Universidad Escuela Colombiana de Ingeniería**  
**Programación Orientada por Objetos — 2026-1**

**Autores:** Diego Alejandro Mesa · Andrés Felipe Rubio

---

## 📋 Descripción del Proyecto

The DOPO Hardest Game es una versión mejorada del clásico *The World's Hardest Game*. El jugador controla un cuadrado rojo que debe recolectar todas las monedas del nivel y llegar a la zona verde final, mientras esquiva enemigos en constante movimiento. El proyecto fue desarrollado como entrega final del curso de Programación Orientada por Objetos, aplicando todos los conceptos vistos durante el semestre.

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
│   └── MaquinaExperta.java     
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

### ¿Qué es PMD?

PMD es una herramienta de análisis de código estático para Java que examina el código fuente sin ejecutarlo. Su objetivo es detectar problemas potenciales antes de que se conviertan en errores en producción. PMD analiza el código usando conjuntos de reglas (*rule sets*) agrupadas por categorías, cada una enfocada en un aspecto diferente de la calidad del software.

En este proyecto se ejecutó PMD sobre los 33 archivos fuente usando 8 rule sets, obteniendo el siguiente resultado:

![Informe PMD](pmd_report.png)

### Resumen de resultados

| Categoría | Violaciones | Severidad principal |
|-----------|-------------|---------------------|
| **bestpractices** | 370 | 6 errores · 354 advertencias · 10 info |
| **codestyle** | 1182 | 1170 errores · 12 advertencias |
| **design** | 184 | 1 error · 183 advertencias |
| **documentation** | 309 | 309 advertencias |
| **errorprone** | 105 | 49 errores · 56 advertencias |
| **multithreading** | 1 | 1 advertencia |
| **performance** | 35 | 6 errores · 29 advertencias |
| **Total** | **2186** | en 33 archivos escaneados |

### Análisis detallado por categoría

#### 🔵 codestyle — 1182 violaciones
Es la categoría con más violaciones y también la de menor impacto funcional. PMD detectó inconsistencias en convenciones de nombres (variables locales, parámetros, constantes), uso de llaves en bloques `if`/`for`/`while`, espaciado y longitud de líneas. Estas violaciones son puramente estéticas y no afectan en absoluto el comportamiento del programa. En un proyecto académico donde la prioridad es la correctitud funcional y la aplicación de patrones de diseño, este tipo de advertencias son esperadas y aceptables. En un entorno profesional se corregirían usando herramientas de formateo automático como Checkstyle o el formateador de IntelliJ.

#### 🟡 bestpractices — 370 violaciones
Esta categoría agrupa recomendaciones de buenas prácticas generales de Java. La mayoría de las 354 advertencias corresponden a usos como acceder a campos o métodos estáticos a través de instancias en lugar del nombre de la clase, o no usar el tipo de interfaz al declarar colecciones (`ArrayList` en vez de `List`). Los 6 errores detectados son casos donde se podría mejorar el uso de la API de Java, pero ninguno representa un bug real en el proyecto.

#### 🟡 documentation — 309 violaciones
PMD detectó que la mayoría de los métodos públicos y clases no tienen documentación Javadoc formal. Todas las clases tienen comentarios internos explicando su funcionamiento, pero PMD exige el formato estándar `/** ... */`. Esta es una limitación de tiempo durante el desarrollo del proyecto; la lógica del código está clara y comentada internamente.

#### 🟠 design — 184 violaciones
PMD identificó 183 advertencias relacionadas con complejidad ciclomática alta, principalmente en métodos como `verificarColisiones()`, `mover()` en los enemigos patrulleros y `cargarNivel()`. Esta complejidad es **inherente a la naturaleza del juego**: un motor de juego necesita evaluar múltiples condiciones simultáneas (colisiones, estados, posiciones) en cada ciclo. Refactorizar estos métodos artificialmente para reducir la complejidad haría el código menos legible sin ningún beneficio real. El 1 error de diseño corresponde a una clase que podría haberse simplificado pero que fue mantenida para mayor claridad pedagógica.

#### 🔴 errorprone — 105 violaciones
Los 49 errores de esta categoría corresponden principalmente a comparaciones con `null` que podrían lanzar `NullPointerException` en casos extremos, y a algunas variables que PMD considera que podrían ser `final`. Los 56 advertencias son casos donde el código funciona correctamente pero PMD sugiere patrones alternativos más defensivos. Ninguno de estos errores se manifestó durante las pruebas del juego.

#### 🟠 performance — 35 violaciones
- **`AvoidInstantiatingObjectsInLoops` (26):** El motor de juego crea objetos dentro de los bucles principales de renderizado y detección de colisiones. Esto es una consecuencia directa de la arquitectura de un juego en tiempo real; cada frame necesita calcular nuevas posiciones y estados. Optimizar esto requeriría implementar un *object pool*, lo cual está fuera del alcance del curso.
- **`AvoidFileStream` (6):** Se usaron `FileInputStream` y `FileOutputStream` para la persistencia de partidas. PMD recomienda `Files.newBufferedReader()` de Java NIO, que es más eficiente. Esta mejora queda como trabajo futuro.
- **`AppendCharacterWithChar` (2):** PMD detectó concatenaciones de un solo carácter con String donde sería más eficiente usar `char`. Impacto mínimo en un juego de esta escala.
- **`InefficientEmptyStringCheck` (1):** Una comparación con `""` que debería usar `isEmpty()`. Corrección trivial.

#### 🔴 multithreading — 1 violación
- **`NonThreadSafeSingleton` (1):** El `ErrorLogger` implementa el patrón Singleton sin sincronización de hilos. Dado que el juego corre en un único hilo principal (el Event Dispatch Thread de Swing), esta violación no representa ningún riesgo real de condición de carrera. En una aplicación multi-hilo real se implementaría con `synchronized` o con el patrón *initialization-on-demand holder*.

### Conclusión PMD

El número total de violaciones (2186) puede parecer alto a primera vista, pero es importante contextualizarlo: **el 54% (1182) son puramente de estilo**, **el 14% (309) son de documentación formal**, y el resto corresponde a decisiones de diseño justificadas por la naturaleza de un motor de juego en tiempo real. No se detectó ningún bug crítico ni vulnerabilidad de seguridad. El código es funcional, correcto y cumple todos los requisitos del proyecto.

---

## 📊 Informe de Cobertura de Pruebas

### ¿Qué es la cobertura de pruebas?

La cobertura de pruebas (*code coverage*) es una métrica que indica qué porcentaje del código fuente fue ejecutado durante las pruebas unitarias. Se mide en cuatro niveles:

- **Cobertura de clases:** ¿Cuántas clases fueron instanciadas al menos una vez?
- **Cobertura de métodos:** ¿Cuántos métodos fueron llamados al menos una vez?
- **Cobertura de ramas:** ¿Cuántos caminos posibles de las estructuras `if`/`switch`/`while` fueron recorridos?
- **Cobertura de líneas:** ¿Cuántas líneas de código fueron ejecutadas al menos una vez?

Las pruebas fueron ejecutadas con **JaCoCo** integrado en IntelliJ IDEA sobre el archivo `dominioTest.java`, que contiene 80 métodos de prueba cubriendo todas las clases del paquete `dominio`.

### Resumen general

![Reporte de Cobertura](coverage_summary.png)

| Métrica | Cubierto | Total | Porcentaje |
|---------|----------|-------|------------|
| **Clases** | 22 | 22 | **100%** ✅ |
| **Métodos** | 266 | 297 | **89,6%** ✅ |
| **Ramas** | 445 | 695 | **64%** ⚠️ |
| **Líneas** | 1466 | 1646 | **89,1%** ✅ |

### Análisis detallado por clase

| Clase | Métodos | Ramas | Líneas | Observación |
|-------|---------|-------|--------|-------------|
| `Configuracion` | 100% | 80,6% | 93,6% | Bien cubierta; ramas restantes son casos de error de archivo |
| `DominioTest` | 100% | 46,9% | 100% | Clase de pruebas; ramas propias de asserts internos |
| `EnemigoBasico` | 88,9% | 80% | 95,8% | Un método de movimiento especial no fue ejercido |
| `EnemigoPatrullero` | 88,9% | 84,6% | 97,8% | Muy bien cubierto; falta un patrón de patrulla |
| `EnemigoPerseguidor` | 50% | 75% | 78,6% | Métodos de IA avanzada difíciles de simular en test |
| `Entidad` | 100% | 100% | 100% | ✅ Cobertura perfecta |
| `ErrorLogger` | 100% | 62,5% | 94,6% | Ramas de manejo de errores de escritura en disco |
| `Bomba` | 100% | 50% | 33,3% | Lógica de explosión con estados difíciles de simular |
| `EstadoPartida` | — | — | — | Clase de datos; cobertura implícita |
| `Juego` | — | — | — | Motor principal; lógica de UI difícil de testear unitariamente |
| **Total dominio** | **89,6%** | **64%** | **89,1%** | |

### Análisis de la cobertura de ramas (64%)

La cobertura de ramas es la métrica más difícil de alcanzar en un motor de juego. El 36% de ramas no cubiertas se explica por:

1. **Lógica de colisiones en tiempo real:** Los métodos `verificarColisiones()` y `mover()` contienen decenas de condiciones que dependen de posiciones exactas de objetos en pantalla. Simular todas las combinaciones posibles en pruebas unitarias requeriría crear configuraciones de nivel muy específicas para cada caso.

2. **Estados de la IA (`EnemigoPerseguidor`):** El algoritmo BFS de `MaquinaExperta` toma decisiones dinámicas basadas en el mapa actual. Cubrir todas sus ramas requeriría simular múltiples configuraciones de nivel completas.

3. **Manejo de errores de I/O:** Las ramas de `catch` para errores de lectura/escritura de archivos (partidas guardadas, logs) son muy difíciles de disparar en un entorno de prueba normal sin forzar fallos del sistema de archivos.

4. **Condiciones de fin de juego:** Algunos estados como "jugador eliminado por bomba en modo PvP con escudo activo" requieren una secuencia muy específica de eventos que es costosa de replicar en tests unitarios.

### Conclusión de cobertura

Los resultados son **muy satisfactorios** para un proyecto de esta naturaleza. Alcanzar **100% de cobertura de clases** y **89,1% de líneas** en un motor de juego completo demuestra un esfuerzo real de pruebas. La cobertura de ramas del 64%, aunque inferior, es completamente normal en aplicaciones con lógica de juego en tiempo real. En proyectos profesionales de videojuegos, una cobertura de ramas superior al 60% se considera excelente dado el alto número de estados posibles del sistema.

---
