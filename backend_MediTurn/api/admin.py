from django.contrib import admin
from .models import Doctor,Slot,Appointment,Patient

# Register your models here.
admin.site.register(Doctor)
admin.site.register(Slot)
admin.site.register(Appointment)
admin.site.register(Patient)