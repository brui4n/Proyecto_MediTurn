### Documentacion del tercer dia

# 📘 Día 3 – Implementación de Interfaz y Paleta de Colores

## 🎯 Objetivo

Integrar la interfaz inicial con Jetpack Compose, aplicando la paleta de colores personalizada y las primeras animaciones visuales de la aplicación MediTurn.

---

## ⚙ Actividades realizadas

### 1. Diseño de la pantalla Splash

- Se implementó la `SplashScreen.kt`, encargada de mostrar el logo animado y transicionar automáticamente hacia la pantalla de Login.
- Se incorporó una animación de puntos con `animateFloatAsState` y `LaunchedEffect` para dar retroalimentación visual al usuario durante la carga.
- Se configuró la navegación hacia `Routes.Login` después de 3 segundos de espera.

### 2. Creación del componente visual del logo

- Se diseñó el componente reutilizable `MediTurnLogo()` usando `Card` y emojis representativos 🩺💚 para simular el logotipo del prototipo Figma.
- Se aplicaron sombras y esquinas redondeadas (`RoundedCornerShape`) con estilo Material 3.

### 3. Integración de la paleta de colores del tema

Se creó el archivo `Color.kt` dentro de `ui/theme/` con la paleta oficial del proyecto:

```kotlin
val BluePrimary = Color(0xFF1E88E5)
val GreenAccent = Color(0xFF43A047)
val WhiteText = Color.White
val WhiteTransparent = Color.White.copy(alpha = 0.85f)
val BackgroundGradient = listOf(BluePrimary, GreenAccent.copy(alpha = 0.8f))
```

- Se reemplazaron todos los colores hardcodeados por los de la paleta, centralizando la gestión visual y garantizando consistencia con el diseño Figma.

### 4. Aplicación del degradado de fondo

- Se utilizó `Brush.verticalGradient(BackgroundGradient)` para crear un fondo dinámico con transición entre el azul primario y el verde acento.

### 5. Resultado visual

- La app ahora inicia con una pantalla Splash moderna, animada y coherente con la identidad visual de MediTurn.
- Se mantiene la arquitectura MVVM y navegación limpia definida el día anterior.

---

## 👥 Colaboración del equipo

| Integrante      | Contribución                                          |
|-----------------|-------------------------------------------------------|
| Bryan Coronel   | Integración del tema y animaciones Compose            |
| Milene Fuentes  | Revisión del diseño Figma y adaptación visual al código |
| Santiago Salas  | Pruebas funcionales y documentación técnica           |

---

## ✅ Resultado del día

- ✅ SplashScreen implementada con animaciones.
- ✅ Paleta de colores aplicada globalmente.
- ✅ Integración visual alineada con Figma.
- ✅ Navegación automática al Login funcional.
