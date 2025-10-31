from django.contrib import admin
from .models import Doctor,Slot,Appointment,Patient, DoctorRating, DoctorSchedule

# Register your models here.

@admin.register(Doctor)
class DoctorAdmin(admin.ModelAdmin):
    list_display = ('name', 'specialty', 'city', 'rating', 'has_image')
    list_filter = ('specialty', 'city')
    search_fields = ('name', 'specialty', 'city')
    
    readonly_fields = ('image_preview',)
    
    fieldsets = (
        ('Información básica', {
            'fields': ('name', 'specialty', 'city')
        }),
        ('Información profesional', {
            'fields': ('experience_desc', 'education', 'languages', 'rating')
        }),
        ('Imagen', {
            'fields': ('image', 'image_preview')
        }),
    )
    
    def save_model(self, request, obj, form, change):
        """Sobrescribe el método save para manejar errores de foreign key"""
        from django.db import IntegrityError, transaction
        try:
            with transaction.atomic():
                super().save_model(request, obj, form, change)
        except IntegrityError as e:
            from django.contrib import messages
            error_msg = f"Error de integridad de base de datos. Ejecuta: python manage.py check_data_integrity --fix"
            messages.error(request, error_msg)
            raise
    
    def has_image(self, obj):
        """Muestra si el doctor tiene imagen (para list_display)"""
        if obj and obj.pk and obj.image:
            return True
        return False
    has_image.short_description = 'Tiene imagen'
    has_image.boolean = True
    
    def image_preview(self, obj):
        """Muestra vista previa de la imagen (para formulario de edición)"""
        if obj and obj.pk and obj.image:
            from django.utils.html import format_html
            try:
                return format_html('<img src="{}" style="max-width: 200px; max-height: 200px;" />', obj.image.url)
            except:
                return "Imagen no disponible"
        return "No hay imagen cargada"
    image_preview.short_description = 'Vista previa'

admin.site.register(Slot)
admin.site.register(Appointment)
admin.site.register(Patient)
admin.site.register(DoctorRating)
admin.site.register(DoctorSchedule)