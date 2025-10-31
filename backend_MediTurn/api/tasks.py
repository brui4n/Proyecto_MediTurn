from datetime import date, timedelta, datetime
from api.models import Doctor, DoctorSchedule, Slot

def generate_daily_slots():
    """
    Genera slots diarios para los próximos 30 días basados en los horarios semanales
    de los doctores (DoctorSchedule).
    """
    hoy = date.today()
    
    doctors = Doctor.objects.all()
    slots_created = 0
    
    for single_date in (hoy + timedelta(n) for n in range(31)):  # Cada día desde hoy hasta +30
        for doctor in doctors:
            schedules = DoctorSchedule.objects.filter(doctor=doctor)
            for schedule in schedules:
                if single_date.weekday() == schedule.weekday:  # Si coincide el día de la semana
                    hora_actual = schedule.start_time
                    while hora_actual < schedule.end_time:  # Cambiado <= por < para evitar slots fuera de horario
                        if not Slot.objects.filter(doctor=doctor, date=single_date, time=hora_actual).exists():
                            Slot.objects.create(doctor=doctor, date=single_date, time=hora_actual, available=True)
                            slots_created += 1
                        hora_actual = (datetime.combine(date.min, hora_actual) + timedelta(hours=1)).time()
    
    return slots_created