from django.db import models

class Doctor(models.Model):
    SPECIALTY_CHOICES = [
        ('CARDIOLOGIA', 'Cardiología'),
        ('NEUROLOGIA', 'Neurología'),
        ('OFTALMOLOGIA', 'Oftalmología'),
        ('TRAUMATOLOGIA', 'Traumatología'),
        ('MEDICINA_GENERAL', 'Medicina General'),
        ('PEDIATRIA', 'Pediatría'),
    ]

    name = models.CharField(max_length=100)
    specialty = models.CharField(max_length=20, choices=SPECIALTY_CHOICES)
    experience_desc = models.TextField(blank=True, null=True)
    education = models.TextField(blank=True, null=True)
    languages = models.CharField(max_length=200, blank=True, null=True)
    rating = models.FloatField(blank=True, null=True)
    image = models.ImageField(upload_to='doctors/', blank=True, null=True)
    city = models.CharField(max_length=100, blank=True, null=True)

    def average_rating(self):
        ratings = self.ratings.all()
        if ratings.exists():
            return round(sum(r.score for r in ratings) / ratings.count(), 1)
        return 0

    def __str__(self):
        return f"{self.name} - {self.get_specialty_display()}"


class Patient(models.Model):
    name = models.CharField(max_length=100)
    age = models.IntegerField()
    gender = models.CharField(max_length=10)
    email = models.EmailField(unique=True)
    phone = models.CharField(max_length=15)
    password = models.CharField(max_length=128)

    def __str__(self):
        return self.name


class Slot(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    date = models.DateField()
    time = models.TimeField()
    available = models.BooleanField(default=True)

    def __str__(self):
        return f"{self.doctor.name} - {self.date} {self.time}"


class Appointment(models.Model):
    CONSULTATION_TYPE_CHOICES = [
        ('PRESENCIAL', 'Presencial'),
        ('TELECONSULTA', 'Teleconsulta'),
    ]

    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    slot = models.ForeignKey(Slot, on_delete=models.CASCADE)
    status = models.CharField(max_length=20, default="Pendiente")
    consultation_type = models.CharField(
        max_length=15, choices=CONSULTATION_TYPE_CHOICES, default='PRESENCIAL'
    )

    def __str__(self):
        return f"Cita: {self.patient.name} con {self.doctor.name} ({self.consultation_type})"
    
class DoctorRating(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, related_name='ratings')
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    score = models.FloatField()  # 0 a 5
    comment = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ('doctor', 'patient')  

    def __str__(self):
        return f"{self.doctor.name} - {self.score} por {self.patient.name}"
    
class DoctorSchedule(models.Model):
    WEEKDAYS = [
        (0, 'Lunes'),
        (1, 'Martes'),
        (2, 'Miércoles'),
        (3, 'Jueves'),
        (4, 'Viernes'),
        (5, 'Sábado'),
        (6, 'Domingo'),
    ]

    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, related_name='schedules')
    weekday = models.IntegerField(choices=WEEKDAYS)
    start_time = models.TimeField()
    end_time = models.TimeField()

    class Meta:
        unique_together = ('doctor', 'weekday')

    def __str__(self):
        return f"{self.doctor.name} - {self.get_weekday_display()}: {self.start_time} a {self.end_time}"