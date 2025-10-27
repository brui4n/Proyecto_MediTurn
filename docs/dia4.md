### Documentacion del cuarto dia
# 🩺 MediTurn – Día 4: Lógica y Datos Simulados

## 🎯 Objetivo
Conectar la interfaz de usuario con los modelos y repositorios locales, utilizando datos simulados que representen la lógica del sistema de gestión de citas médicas.

---

## 🧱 Modelos Definidos

Se implementaron las clases principales del dominio en el paquete data.model:

### *Doctor*
```kotlin
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val experience: Int,
    val rating: Double,
    val imageUrl: String
)
```
### *Patient*
```kotlin
data class Patient(
    val id: Int,
val name: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val password: String
)
```
### *Slot*
```kotlin
data class Slot(
    val id: Int,
    val doctorId: Int,
    val date: String,
    val time: String,
    val available: Boolean
)
```
### *Appointment*
```kotlin
data class Appointment(
    val id: Int,
    val doctor: Doctor,
    val patient: Patient,
    val slot: Slot,
    val status: String
)
```
---

## 💾 Repositorios Locales

Se crearon repositorios con datos simulados (almacenados en memoria) para representar la capa de datos de la aplicación.

### *DoctorRepository*

Contiene una lista de médicos con distintas especialidades:
```kotlin
private val doctors = mutableListOf(
    Doctor(1, "Dr. Juan Pérez", "Cardiología", 10, 4.7, ""),
    Doctor(2, "Dra. Ana Torres", "Pediatría", 6, 4.5, ""),
    Doctor(3, "Dr. Luis García", "Dermatología", 8, 4.2, ""),
    Doctor(4, "Dra. Carla Medina", "Ginecología", 12, 4.8, ""),
    Doctor(5, "Dr. Marco Rojas", "Medicina General", 4, 4.0, "")
)
```
### *SlotRepository*
Contiene horarios disponibles (slots) para los doctores registrados:
```kotlin
private val slots = mutableListOf(
    Slot(1, 1, "2025-11-01", "09:00", true),
    Slot(2, 1, "2025-11-01", "09:30", true),
    Slot(3, 1, "2025-11-01", "10:00", true),
    Slot(4, 2, "2025-11-01", "11:00", true),
    Slot(5, 2, "2025-11-02", "08:30", true),
    Slot(6, 3, "2025-11-03", "14:00", true)
)
```
### *AppointmentRepository*

Diseñado para registrar las citas médicas generadas en el sistema, aún sin datos iniciales.

---

## 🔍 Búsqueda por Especialidad / Nombre

Se implementaron funciones dentro de `DoctorRepository` que permiten obtener:
- Todos los doctores disponibles.
- Filtrar por especialidad.
- Buscar doctores por nombre.
```kotlin
fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
    return doctors.filter { it.specialty == specialty }
}
```
Estas funciones permiten que la interfaz conecte los datos dinámicamente según lo que el usuario seleccione en la app.

---

## 🧩 Flujo de Reserva Simulado (Pendiente)

Durante el Día 4 se planificó el flujo completo de reserva de citas médicas, el cual incluye:
- Selección de especialidad.
- Filtrado de doctores por especialidad.
- Selección de doctor.
- Elección de fecha y horario (slot disponible).
- Confirmación de cita y creación del objeto `Appointment`.

Sin embargo, esta parte quedó pendiente de implementación y será desarrollada al inicio del Día 5, integrando el formulario de agendar cita (`ScheduleAppointmentScreen`) y conectando la lógica con los repositorios locales para simular el flujo completo.
