from rest_framework import serializers
from .models import Doctor, Patient, Slot, Appointment, DoctorRating, DoctorSchedule

class DoctorSerializer(serializers.ModelSerializer):
    image = serializers.SerializerMethodField()
    
    class Meta:
        model = Doctor
        fields = '__all__'
    
    def get_image(self, obj):
        if obj and obj.image:
            request = self.context.get('request')
            if request:
                try:
                    return request.build_absolute_uri(obj.image.url)
                except:
                    return None
            try:
                return obj.image.url
            except:
                return None
        return None

class PatientSerializer(serializers.ModelSerializer):
    class Meta:
        model = Patient
        fields = '__all__'

class SlotSerializer(serializers.ModelSerializer):
    class Meta:
        model = Slot
        fields = '__all__'

class AppointmentSerializer(serializers.ModelSerializer):
    doctor = DoctorSerializer(read_only=True)
    patient = PatientSerializer(read_only=True)
    slot = SlotSerializer(read_only=True)

    class Meta:
        model = Appointment
        fields = '__all__'

class DoctorRatingSerializer(serializers.ModelSerializer):
    class Meta:
        model = DoctorRating
        fields = '__all__'

class DoctorScheduleSerializer(serializers.ModelSerializer):
    weekday_name = serializers.CharField(source='get_weekday_display', read_only=True)
    
    class Meta:
        model = DoctorSchedule
        fields = ['id', 'doctor', 'weekday', 'weekday_name', 'start_time', 'end_time']