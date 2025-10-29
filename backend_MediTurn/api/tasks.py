from datetime import date, timedelta, datetime
from api.models import Doctor, DoctorSchedule, Slot

def generate_daily_slots():
    hoy = date.today()
    fecha_limite = hoy + timedelta(days=30)

    doctors = Doctor.objects.all()
    for single_date in (hoy + timedelta(n) for n in range(31)):  # Cada día desde hoy hasta +30
        for doctor in doctors:
            schedules = DoctorSchedule.objects.filter(doctor=doctor)
            for schedule in schedules:
                if single_date.weekday() == schedule.weekday:  # Si coincide el día de la semana
                    hora_actual = schedule.start_time
                    while hora_actual <= schedule.end_time:
                        if not Slot.objects.filter(doctor=doctor, date=single_date, time=hora_actual).exists():
                            Slot.objects.create(doctor=doctor, date=single_date, time=hora_actual)
                        hora_actual = (datetime.combine(date.min, hora_actual) + timedelta(hours=1)).time()