#!/usr/bin/env python
"""
Script para diagnosticar y corregir errores de FOREIGN KEY constraint
"""
import os
import sys
import django

# Configurar Django
sys.path.append(os.path.dirname(os.path.abspath(__file__)))
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'mediturn_backend.settings')
django.setup()

from api.models import Doctor, Slot, Appointment, DoctorRating, DoctorSchedule, Patient

def check_and_fix():
    print("Verificando integridad de datos...\n")
    issues = []
    
    # Verificar Doctor con ID 1 específicamente
    try:
        doctor_1 = Doctor.objects.get(pk=1)
        print(f"✓ Doctor con ID 1 existe: {doctor_1.name}")
    except Doctor.DoesNotExist:
        print("✗ ERROR: Doctor con ID 1 no existe!")
        issues.append("Doctor 1 no existe")
        return
    
    # Verificar slots que referencian doctores inexistentes
    all_slots = Slot.objects.all()
    for slot in all_slots:
        try:
            _ = slot.doctor_id
            doctor = slot.doctor
        except Exception as e:
            print(f"✗ Slot {slot.id} tiene referencia inválida al doctor: {e}")
            issues.append(f"Slot {slot.id}")
            slot.delete()
            print(f"  → Slot {slot.id} eliminado")
    
    # Verificar appointments
    all_appointments = Appointment.objects.all()
    for appt in all_appointments:
        try:
            doctor = appt.doctor
            patient = appt.patient
            slot = appt.slot
        except Exception as e:
            print(f"✗ Appointment {appt.id} tiene referencia inválida: {e}")
            issues.append(f"Appointment {appt.id}")
            appt.delete()
            print(f"  → Appointment {appt.id} eliminado")
    
    # Verificar ratings
    all_ratings = DoctorRating.objects.all()
    for rating in all_ratings:
        try:
            doctor = rating.doctor
            patient = rating.patient
        except Exception as e:
            print(f"✗ DoctorRating {rating.id} tiene referencia inválida: {e}")
            issues.append(f"Rating {rating.id}")
            rating.delete()
            print(f"  → Rating {rating.id} eliminado")
    
    # Verificar schedules
    all_schedules = DoctorSchedule.objects.all()
    for schedule in all_schedules:
        try:
            doctor = schedule.doctor
        except Exception as e:
            print(f"✗ DoctorSchedule {schedule.id} tiene referencia inválida: {e}")
            issues.append(f"Schedule {schedule.id}")
            schedule.delete()
            print(f"  → Schedule {schedule.id} eliminado")
    
    # Verificar si hay conflictos de índices únicos
    schedules_by_doctor = {}
    for schedule in DoctorSchedule.objects.all():
        key = (schedule.doctor_id, schedule.weekday)
        if key in schedules_by_doctor:
            print(f"⚠ DoctorSchedule duplicado encontrado: Doctor {schedule.doctor_id}, Weekday {schedule.weekday}")
            print(f"  IDs: {schedules_by_doctor[key]}, {schedule.id}")
        else:
            schedules_by_doctor[key] = schedule.id
    
    if not issues:
        print("\n✓ No se encontraron problemas de integridad")
    else:
        print(f"\n✓ Se corrigieron {len(issues)} problemas")
    
    # Verificar doctor 1 específicamente
    print("\n" + "="*50)
    print("Información del Doctor ID 1:")
    print("="*50)
    doctor = Doctor.objects.get(pk=1)
    print(f"Nombre: {doctor.name}")
    print(f"Slots relacionados: {doctor.slot_set.count()}")
    print(f"Appointments relacionados: {doctor.appointment_set.count()}")
    print(f"Ratings relacionados: {doctor.ratings.count()}")
    print(f"Schedules relacionados: {doctor.schedules.count()}")
    
    print("\n✓ Verificación completada")

if __name__ == '__main__':
    check_and_fix()

