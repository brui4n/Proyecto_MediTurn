from rest_framework.routers import DefaultRouter
from .views import DoctorViewSet, PatientViewSet, SlotViewSet, AppointmentViewSet

router = DefaultRouter()
router.register(r'doctors', DoctorViewSet)
router.register(r'patients', PatientViewSet)
router.register(r'slots', SlotViewSet)
router.register(r'appointments', AppointmentViewSet)

urlpatterns = router.urls