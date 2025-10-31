from django.contrib import admin
from .models import Doctor,Slot,Appointment,Patient, DoctorRating, DoctorSchedule

# Register your models here.
admin.site.register(Doctor)
admin.site.register(Slot)
admin.site.register(Appointment)
admin.site.register(Patient)
admin.site.register(DoctorRating)
admin.site.register(DoctorSchedule)