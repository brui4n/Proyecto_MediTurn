from django.db import models

class Doctor(models.Model):
    name = models.CharField(max_length=100)
    specialty = models.CharField(max_length=100)
    experience = models.IntegerField()
    rating = models.FloatField()
    image_url = models.URLField(blank=True, null=True)

    def __str__(self):
        return f"{self.name} - {self.specialty}"


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
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    slot = models.ForeignKey(Slot, on_delete=models.CASCADE)
    status = models.CharField(max_length=20, default="Pendiente")

    def __str__(self):
        return f"Cita: {self.patient.name} con {self.doctor.name}"