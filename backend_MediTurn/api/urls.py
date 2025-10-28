from rest_framework.routers import DefaultRouter
from django.urls import path
from .views import (
    DoctorViewSet,
    PatientViewSet,
    SlotViewSet,
    AppointmentViewSet,
    login_patient,  
    register_patient
)
from django.conf import settings
from django.conf.urls.static import static

# Rutas automáticas generadas por los ViewSets
router = DefaultRouter()
router.register(r'doctors', DoctorViewSet)
router.register(r'patients', PatientViewSet)
router.register(r'slots', SlotViewSet)
router.register(r'appointments', AppointmentViewSet)


urlpatterns = [
    path('login/', login_patient, name='login_patient'),
    path('register/', register_patient, name='register_patient'),

]


urlpatterns += router.urls

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)