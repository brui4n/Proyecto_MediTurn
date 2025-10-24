### Documentacion del segundo dia

# 📘 Día 2 – Configuración del Proyecto y Estructura Base

## 🎯 Objetivo
Montar el proyecto base en *Kotlin + Jetpack Compose, definir la arquitectura del sistema, y configurar la **navegación inicial* entre pantallas principales.

---

## ⚙ Actividades realizadas

### 1. Creación del proyecto
- Se creó el proyecto *MediTurn* en *Android Studio* con el paquete base com.tecsup.mediturn.  
- Se configuraron las dependencias de *Jetpack Compose* y *Navigation Compose* en el archivo build.gradle.

---

### 2. Definición de la arquitectura
Se implementó la estructura basada en el patrón *MVVM (Model–View–ViewModel)* con las siguientes capas y paquetes:
com.tecsup.mediturn
├── data          → acceso a datos locales (DAO, DB, modelos)
├── repository    → capa de abstracción entre datos y lógica
├── viewmodel     → control de estados y comunicación con UI
├── presentation  → pantallas principales y Activity principal
├── navigation    → gestión de rutas y NavHost
├── ui            → configuración visual (colores, tipografía, tema)
└── util          → clases de utilidad y constantes globales
---

### 3. Configuración de la navegación
- Se creó el archivo Routes.kt con las rutas base:  
  ```kotlin
  object Routes {
      const val SPLASH = "splash"
      const val LOGIN = "login"
      const val REGISTER = "register"
      const val HOME = "home"
  }

	•	Se implementó el archivo NavGraph.kt para controlar la navegación entre las pantallas principales (SplashScreen, LoginScreen, RegisterScreen, HomeScreen).
	•	En MainActivity.kt, se integró el NavHostController con el NavGraph como punto de entrada de la app.

⸻

4. Creación de pantallas iniciales

Se desarrollaron las pantallas base dentro de presentation/screens/:
	•	SplashScreen.kt
	•	LoginScreen.kt
	•	RegisterScreen.kt
	•	HomeScreen.kt

Cada una contiene un diseño inicial en Jetpack Compose, preparado para conectar lógica y datos más adelante.

⸻

5. Organización del equipo y ramas
	•	Se creó la rama principal main y una rama por integrante:
	•	coronel → líder técnico
	•	salas → tester/documentador
	•	fuentes → diseñador UI

Esto facilita el trabajo colaborativo mediante commits y merges controlados.

⸻
✅ Resultado del día
	•	✅ Proyecto base creado correctamente
	•	✅ Arquitectura MVVM organizada
	•	✅ Rutas y navegación configuradas
	•	✅ Pantallas iniciales implementadas
	•	✅ Ramas individuales creadas para trabajo colaborativo