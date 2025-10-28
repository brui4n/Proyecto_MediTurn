from rest_framework import viewsets
from django.contrib.auth.hashers import make_password, check_password
from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework import status
from .models import Doctor, Patient, Slot, Appointment
from .serializers import DoctorSerializer, PatientSerializer, SlotSerializer, AppointmentSerializer
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.permissions import IsAuthenticated
from rest_framework.decorators import permission_classes


class DoctorViewSet(viewsets.ModelViewSet):
    queryset = Doctor.objects.all()
    serializer_class = DoctorSerializer
    permission_classes = [IsAuthenticated]

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
    permission_classes = [IsAuthenticated]

class SlotViewSet(viewsets.ModelViewSet):
    queryset = Slot.objects.all()
    serializer_class = SlotSerializer
    permission_classes = [IsAuthenticated]

class AppointmentViewSet(viewsets.ModelViewSet):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer
    permission_classes = [IsAuthenticated]




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