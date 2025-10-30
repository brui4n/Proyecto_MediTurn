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

## 🧩 Arquitectura base

El proyecto sigue el patrón MVVM (Model–View–ViewModel), combinando consumo de API REST (Django + Retrofit) con una organización modular clara: <br>

com.tecsup.mediturn <br>
├── data <br>
│   ├── model          → Modelos de datos (Doctor, Appointment, Patient, etc.) <br>
│   ├── remote         → Conexión con el backend (Retrofit, Endpoints, DTOs, AuthInterceptor) <br>
│   ├── repository     → Intermediarios entre ViewModels y capa remota (lógica de red y manejo de errores) <br>
│   └── session        → Gestión de sesión y token del usuario <br>
│
├── presentation <br>
│   ├── screens        → Pantallas principales (Login, Registro, Home, Citas, etc.) <br>
│   ├── components     → Componentes reutilizables (BottomBar, DoctorCard, etc.) <br>
│   └── MainActivity.kt → Contenedor principal del flujo de navegación <br>
│
├── viewmodel          → Manejo de estados, peticiones y lógica de negocio (uno por entidad principal) <br>
│
├── navigation          → Definición de rutas y NavHost <br>
│
├── ui/theme            → Definición de colores, tipografía y estilos globales <br>
│
└── util                → Utilidades compartidas (Constantes, envoltorio Resource) <br>
## 🛠️ Tecnologías utilizadas

	•	Lenguaje: Kotlin
	•	Framework: Jetpack Compose (Material 3)
	•	Arquitectura: MVVM (Model–View–ViewModel)
	•	Consumo de API: Retrofit + Gson Converter
	•	Backend: Django REST Framework
	•	Base de datos del backend: SQLite (predeterminada de Django)
	•	Gestión de sesión: SharedPreferences mediante SessionManager
	•	Navegación: Navigation Compose
	•	Diseño: Figma
	•	Control de versiones: Git / GitHub
	•	Testing: JUnit + pruebas instrumentadas (Android Test)
	•	Configuración de red: network_security_config.xml para conexión con backend local o remoto
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

📎 Enlace al diseño: [Figma]([https://www.figma.com/make/ndETWppBQuz1Z7Q8LpdmD8/MediTurn-Mobile-App-Prototype?node-id=0-1&t=8vPChdFO6xze56WZ-1](https://ivory-folder-15860280.figma.site))

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
