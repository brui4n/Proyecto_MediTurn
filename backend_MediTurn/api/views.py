from rest_framework import viewsets
from rest_framework.decorators import action
from django.contrib.auth.hashers import make_password, check_password
from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework import status
from .models import Doctor, Patient, Slot, Appointment, DoctorSchedule
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