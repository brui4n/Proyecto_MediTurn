from rest_framework import viewsets
from rest_framework.decorators import action
from django.contrib.auth.hashers import make_password, check_password
from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework import status
from .models import Doctor, Patient, Slot, Appointment, DoctorSchedule
from django.db import models
from datetime import date as _date, datetime as _datetime, time as _time
from .serializers import DoctorSerializer, PatientSerializer, SlotSerializer, AppointmentSerializer, DoctorScheduleSerializer
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.permissions import IsAuthenticated
from rest_framework.decorators import permission_classes


class DoctorViewSet(viewsets.ModelViewSet):
    queryset = Doctor.objects.all()
    serializer_class = DoctorSerializer
    # permission_classes = [IsAuthenticated]

    def get_queryset(self):
        queryset = Doctor.objects.all()
        specialty = self.request.query_params.get('specialty')
        name = self.request.query_params.get('name')

        if specialty:
            queryset = queryset.filter(specialty__icontains=specialty)  # iexact ignora mayúsculas/minúsculas

        if name:
            queryset = queryset.filter(name__icontains=name)  # icontains = búsqueda parcial sin importar mayúsculas

        return queryset

class PatientViewSet(viewsets.ModelViewSet):
    queryset = Patient.objects.all()
    serializer_class = PatientSerializer
    # permission_classes = [IsAuthenticated]

class SlotViewSet(viewsets.ModelViewSet):
    queryset = Slot.objects.all()  # ✅ agrega esta línea
    serializer_class = SlotSerializer

    def get_queryset(self):
        queryset = Slot.objects.all()
        doctor_id = self.request.query_params.get('doctor_id')
        if doctor_id is not None:
            queryset = queryset.filter(doctor_id=doctor_id)
        return queryset
    # permission_classes = [IsAuthenticated]

class AppointmentViewSet(viewsets.ModelViewSet):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer
    # permission_classes = [IsAuthenticated]

    def get_queryset(self):
        qs = super().get_queryset().select_related('doctor', 'patient', 'slot')
        patient_id = self.request.query_params.get('patient')
        scope = self.request.query_params.get('scope')  # 'upcoming' | 'past'

        if patient_id:
            qs = qs.filter(patient_id=patient_id)

        # Orden por fecha/hora de slot
        qs = qs.order_by('slot__date', 'slot__time')

        if scope in ('upcoming', 'past'):
            today = _date.today()
            now_time = _datetime.now().time()
            if scope == 'upcoming':
                qs = qs.filter(
                    models.Q(slot__date__gt=today) |
                    (models.Q(slot__date=today) & models.Q(slot__time__gte=now_time))
                )
            else:
                qs = qs.filter(
                    models.Q(slot__date__lt=today) |
                    (models.Q(slot__date=today) & models.Q(slot__time__lt=now_time))
                )

        return qs

    @action(detail=True, methods=['post'])
    def cancel(self, request, pk=None):
        try:
            appointment = Appointment.objects.select_related('slot').get(pk=pk)
        except Appointment.DoesNotExist:
            return Response({"error": "Cita no encontrada"}, status=status.HTTP_404_NOT_FOUND)

        # Liberar slot y marcar cancelada
        appointment.slot.available = True
        appointment.slot.save()
        appointment.status = "Cancelada"
        appointment.save()

        return Response(AppointmentSerializer(appointment).data, status=status.HTTP_200_OK)

    @action(detail=True, methods=['post'])
    def reschedule(self, request, pk=None):
        new_slot_id = request.data.get('slot')
        if not new_slot_id:
            return Response({"error": "Debe enviar 'slot'"}, status=status.HTTP_400_BAD_REQUEST)

        try:
            appointment = Appointment.objects.select_related('slot').get(pk=pk)
            new_slot = Slot.objects.get(pk=new_slot_id)
        except Appointment.DoesNotExist:
            return Response({"error": "Cita no encontrada"}, status=status.HTTP_404_NOT_FOUND)
        except Slot.DoesNotExist:
            return Response({"error": "Slot no encontrado"}, status=status.HTTP_404_NOT_FOUND)

        if not new_slot.available:
            return Response({"error": "El nuevo slot no está disponible"}, status=status.HTTP_400_BAD_REQUEST)

        # Liberar slot anterior
        old_slot = appointment.slot
        old_slot.available = True
        old_slot.save()

        # Asignar nuevo slot y bloquearlo
        appointment.slot = new_slot
        appointment.status = "Pendiente"
        appointment.save()
        new_slot.available = False
        new_slot.save()

        return Response(AppointmentSerializer(appointment).data, status=status.HTTP_200_OK)
    @action(detail=False, methods=['post'], url_path='create-appointment')
    def create_appointment(self, request):
        """
        Crea una cita usando los IDs enviados en JSON:
        doctor, patient, slot, consultation_type
        """
        doctor_id = request.data.get('doctor')
        patient_id = request.data.get('patient')
        slot_id = request.data.get('slot')
        consultation_type = request.data.get('consultation_type', 'PRESENCIAL')

        # Validar IDs
        try:
            doctor = Doctor.objects.get(id=doctor_id)
            patient = Patient.objects.get(id=patient_id)
            slot = Slot.objects.get(id=slot_id)
        except (Doctor.DoesNotExist, Patient.DoesNotExist, Slot.DoesNotExist):
            return Response({"error": "ID inválido"}, status=status.HTTP_400_BAD_REQUEST)

        if not slot.available:
            return Response({"error": "Este horario ya está reservado"}, status=status.HTTP_400_BAD_REQUEST)

        # Crear cita
        appointment = Appointment.objects.create(
            doctor=doctor,
            patient=patient,
            slot=slot,
            consultation_type=consultation_type,
            status="Pendiente"
        )

        # Marcar slot como no disponible
        slot.available = False
        slot.save()

        serializer = AppointmentSerializer(appointment)
        return Response(serializer.data, status=status.HTTP_201_CREATED)

class DoctorScheduleViewSet(viewsets.ModelViewSet):
    queryset = DoctorSchedule.objects.all()
    serializer_class = DoctorScheduleSerializer
    # permission_classes = [IsAuthenticated]

    # opcional: filtrar por doctor usando query param
    def get_queryset(self):
        queryset = super().get_queryset()
        doctor_id = self.request.query_params.get('doctor')
        if doctor_id:
            queryset = queryset.filter(doctor_id=doctor_id)
        return queryset




@api_view(['POST'])
def login_patient(request):
    email = request.data.get('email')
    password = request.data.get('password')

    if not email or not password:
        return Response({'error': 'Email y contraseña son obligatorios.'}, status=400)

    patient = Patient.objects.filter(email=email).first()
    if not patient:
        return Response({'error': 'El usuario no existe.'}, status=404)

    if not check_password(password, patient.password):
        return Response({'error': 'Contraseña incorrecta.'}, status=401)

    # 🔹 Generar tokens
    refresh = RefreshToken.for_user(patient)
    access_token = str(refresh.access_token)

    # 🔹 Devolver paciente + token
    serializer = PatientSerializer(patient)
    return Response({
        'patient': serializer.data,
        'access_token': access_token,
        'refresh_token': str(refresh)
    }, status=200)


@api_view(['POST'])
def register_patient(request):
    name = request.data.get('name')
    email = request.data.get('email')
    phone = request.data.get('phone')
    gender = request.data.get('gender')
    password = request.data.get('password')
    age = request.data.get('age')

    if age is None or str(age).strip() == "":
        return Response(
            {'error': 'La edad es obligatoria.'},
            status=status.HTTP_400_BAD_REQUEST
        )

    try:
        age = int(age)
    except (TypeError, ValueError):
        return Response(
            {'error': 'La edad debe ser un número válido.'},
            status=status.HTTP_400_BAD_REQUEST
        )

    if not all([name, email, phone, gender, password, age]):
        return Response(
            {'error': 'Todos los campos son obligatorios.'},
            status=status.HTTP_400_BAD_REQUEST
        )

    if Patient.objects.filter(email=email).exists():
        return Response(
            {'error': 'El correo ya está registrado.'},
            status=status.HTTP_400_BAD_REQUEST
        )

    # 🔹 Creamos el paciente con contraseña encriptada
    patient = Patient.objects.create(
        name=name,
        email=email,
        phone=phone,
        gender=gender,
        password=make_password(password),
        age = age
    )
    serializer = PatientSerializer(patient)
    return Response(serializer.data, status=status.HTTP_201_CREATED)


@api_view(['POST'])
def change_password(request):
    """Cambia la contraseña del paciente autenticado o por id enviado.
    Body: { "patient_id": int, "old_password": str, "new_password": str }
    """
    patient_id = request.data.get('patient_id')
    old_password = request.data.get('old_password')
    new_password = request.data.get('new_password')

    if not all([patient_id, old_password, new_password]):
        return Response({"error": "Faltan datos"}, status=status.HTTP_400_BAD_REQUEST)

    try:
        patient = Patient.objects.get(id=patient_id)
    except Patient.DoesNotExist:
        return Response({"error": "Paciente no encontrado"}, status=status.HTTP_404_NOT_FOUND)

    if not check_password(old_password, patient.password):
        return Response({"error": "Contraseña actual incorrecta"}, status=status.HTTP_400_BAD_REQUEST)

    patient.set_password(new_password)
    patient.save()
    return Response({"message": "Contraseña actualizada correctamente"}, status=status.HTTP_200_OK)