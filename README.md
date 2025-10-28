# 🩺 MediTurn – Citas Médicas

**Curso:** Programacion en Móviles (Kotlin + Jetpack Compose)  
**Docente:** Juan León  
**Duración:** 6 días  
**Modalidad:** Trabajo colaborativo (2–3 integrantes)  

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

El proyecto está desarrollado siguiendo el patrón **MVVM (Model–View–ViewModel)** y la arquitectura modular:
com.coronel.mediturn <br>

├── data        → manejo de datos locales (Room / JSON) <br>
├── repository  → capa de repositorio e intermediación de datos <br>
├── viewmodel   → lógica de negocio y estados de UI <br>
├── presentation → pantallas y navegación principal <br>
├── ui          → temas, colores, tipografía <br>
└── navigation  → rutas y NavHost <br>

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin  
- **Framework:** Jetpack Compose  
- **Arquitectura:** MVVM  
- **Navegación:** Navigation Compose  
- **Base de datos (opcional):** Room o JSON simulado  
- **Diseño:** Figma  
- **Control de versiones:** Git / GitHub

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

📎 Enlace al diseño: [Figma](https://www.figma.com/make/ndETWppBQuz1Z7Q8LpdmD8/MediTurn-Mobile-App-Prototype?node-id=0-1&t=8vPChdFO6xze56WZ-1)

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
🕓 Pendiente: (Día 4-5)

---
