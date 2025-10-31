# 🩺 MediTurn – Citas Médicas

**Curso:** Programacion en Móviles (Kotlin + Jetpack Compose)  
**Docente:** Juan León  
**Duración:** 6 días  
**Modalidad:** Trabajo colaborativo (3 integrantes)  

---

## 📘 Descripción del proyecto

**MediTurn** es una aplicación móvil que permite a los pacientes **buscar médicos por especialidad, agendar, editar o cancelar citas**, y visualizar su **calendario médico personal**.  
El objetivo es ofrecer una experiencia moderna, rápida y organizada para la **gestión de citas médicas**, integrando una interfaz intuitiva desarrollada con **Jetpack Compose**.

---

## 🎯 Alcance

- Buscar médicos por nombre o especialidad.  
- Ver información detallada del médico (perfil, experiencia, disponibilidad).  
- Agendar cita seleccionando fecha, hora y motivo.  
- Consultar y administrar citas (editar o cancelar).  
- Visualizar calendario del paciente con próximas citas.  
- Recibir recordatorios de citas.

---

## 👥 Roles del equipo

| Integrante | Rol | Responsabilidad principal |
|-------------|-----|----------------------------|
| Bryan Coronel | Líder técnico | Estructura del proyecto, navegación y arquitectura MVVM |
| Milene Fuentes | Diseñador UI | Prototipo en Figma y diseño visual con Material 3 |
| Santiago Salas | Tester / Documentador | Validación funcional y documentación final del proyecto |


---

## 🏛️ Arquitectura y Estructura del Proyecto

La aplicación está construida sobre la arquitectura **MVVM (Model-View-ViewModel)**, un patrón moderno que separa la lógica de la interfaz de usuario para crear aplicaciones robustas y fáciles de mantener.

*   **Model (Capa de Datos):** Se encarga de toda la lógica de datos. Incluye los modelos (`data/model`), la conexión a la API con Retrofit (`data/remote`) y los **Repositorios** (`data/repository`), que actúan como única fuente de verdad para los ViewModels.
*   **View (Capa de UI):** Es la capa de presentación, construida con Jetpack Compose (`presentation`). Su única función es observar los datos expuestos por el ViewModel y pintar la pantalla. Es una capa "tonta", sin lógica de negocio.
*   **ViewModel:** Sirve de puente. Contiene la lógica de negocio (`viewmodel`), recibe las interacciones del usuario, pide datos a los repositorios y expone el estado de la UI para que la View lo consuma de forma reactiva.

### Estructura de Paquetes

Para implementar esta arquitectura, el proyecto se organizó de la siguiente manera:

```
com.tecsup.mediturn/
├── data/
│   ├── model/      # Contiene las clases de datos (Doctor, Patient, Slot).
│   ├── remote/     # Gestiona la conexión con el backend (Retrofit, Endpoints, DTOs).
│   ├── repository/ # Provee los datos al ViewModel, abstrayendo si vienen de la API o de una BD local.
│   └── session/    # Administra la sesión del usuario (token, datos de usuario logueado).
│
├── presentation/
│   ├── screens/    # Cada pantalla de la aplicación (LoginScreen, HomeScreen, etc.).
│   └── components/ # Componentes de UI reutilizables (botones, cards, etc.).
│
├── viewmodel/
│   # Contiene los ViewModels, responsables de la lógica y el estado de cada pantalla.
│
├── navigation/
│   # Define las rutas y el grafo de navegación de la aplicación con Navigation Compose.
│
├── ui/
│   └── theme/      # Archivos de tema (colores, tipografía y estilos globales).
│
└── util/
    # Clases de utilidad y wrappers (ej. Resource para manejar estados de carga/error/éxito).
```

---

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **Framework:** Jetpack Compose (Material 3)
*   **Arquitectura:** MVVM (Model–View–ViewModel)
*   **Consumo de API:** Retrofit + Gson Converter
*   **Backend:** Django REST Framework
*   **Base de datos del backend:** SQLite
*   **Gestión de sesión:** SharedPreferences
*   **Navegación:** Navigation Compose
*   **Diseño:** Figma
*   **Control de versiones:** Git / GitHub
*   **Testing:** JUnit + Android Test

---

## 🧠 Historias de usuario (provisionales)

1. Como paciente, quiero **buscar médicos por especialidad** para encontrar fácilmente al especialista que necesito.  
2. Como paciente, quiero **ver el perfil y disponibilidad del médico** antes de agendar una cita.  
3. Como paciente, quiero **reservar una cita médica** indicando la fecha, hora y motivo.  
4. Como paciente, quiero **ver mis próximas citas en un calendario** para mantenerme organizado.  
5. Como paciente, quiero **editar o cancelar una cita** en caso de imprevistos.  
6. Como paciente, quiero **recibir recordatorios de mis citas** para no olvidarlas.

---

## 🎨 Prototipo en Figma

📎 Enlace al diseño: [Figma](https://ivory-folder-15860280.figma.site)  

---

## 🌐 Repositorio GitHub

📦 [Proyecto MediTurn en GitHub](https://github.com/brui4n/Proyecto_MediTurn)

---

## 🗓️ Cronograma general

| Día | Fase | Objetivo principal |
|-----|------|--------------------|
| 1 | Planificación y diseño | Alcance, Figma y README |
| 2 | Estructura base | Crear proyecto + navegación |
| 3 | Interfaz (UI/UX) | Implementar pantallas según Figma |
| 4 | Lógica y datos simulados | Conectar UI con modelos y repositorios |
| 5 | Funcionalidades clave | Búsqueda, validaciones y gestión de citas |
| 6 | Presentación final | README completo, demo y entrega final |

---

## 📱 Estado actual del proyecto

✅ Proyecto base creado en Android Studio  
✅ Estructura de paquetes organizada <br>
✅ Primer diseño en figma <br>
🕓 Pendiente: (PR sobre la version 1.0)

---
