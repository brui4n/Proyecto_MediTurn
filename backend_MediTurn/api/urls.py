from rest_framework.routers import DefaultRouter
from django.urls import path
from .views import (
    DoctorViewSet,
    PatientViewSet,
    SlotViewSet,
    AppointmentViewSet,
    login_patient,  
    register_patient,
    change_password,
    DoctorScheduleViewSet
)
# Rutas automáticas generadas por los ViewSets
router = DefaultRouter()
router.register(r'doctors', DoctorViewSet)
router.register(r'patients', PatientViewSet)
router.register(r'slots', SlotViewSet)
router.register(r'appointments', AppointmentViewSet)
router.register(r'doctor-schedules', DoctorScheduleViewSet)


urlpatterns = [
    path('login/', login_patient, name='login_patient'),
    path('register/', register_patient, name='register_patient'),
    path('change-password/', change_password, name='change_password'),

]


urlpatterns += router.urls