from rest_framework.routers import DefaultRouter
from .views import DoctorViewSet, PatientViewSet, SlotViewSet, AppointmentViewSet
from django.conf import settings
from django.conf.urls.static import static

router = DefaultRouter()
router.register(r'doctors', DoctorViewSet)
router.register(r'patients', PatientViewSet)
router.register(r'slots', SlotViewSet)
router.register(r'appointments', AppointmentViewSet)

urlpatterns = router.urls
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)