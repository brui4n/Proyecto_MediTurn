"""
Comando para verificar y limpiar la integridad de los datos.

Este comando verifica si hay registros huérfanos que puedan causar errores
de FOREIGN KEY constraint.
"""
from django.core.management.base import BaseCommand
from api.models import Slot, Appointment, DoctorRating, DoctorSchedule


class Command(BaseCommand):
    help = 'Verifica y limpia la integridad de los datos'

    def add_arguments(self, parser):
        parser.add_argument(
            '--fix',
            action='store_true',
            help='Elimina registros huérfanos automáticamente',
        )

    def handle(self, *args, **options):
        fix = options['fix']
        
        self.stdout.write('Verificando integridad de datos...\n')
        issues_found = False
        
        # Verificar Slots sin Doctor válido
        all_slots = Slot.objects.all()
        orphan_slot_ids = []
        for slot in all_slots:
            try:
                _ = slot.doctor
            except:
                orphan_slot_ids.append(slot.id)
        
        if orphan_slot_ids:
            issues_found = True
            orphan_slots = Slot.objects.filter(id__in=orphan_slot_ids)
            count = orphan_slots.count()
            self.stdout.write(
                self.style.WARNING(f'⚠ Encontrados {count} slots sin doctor válido')
            )
            if fix:
                orphan_slots.delete()
                self.stdout.write(self.style.SUCCESS(f'✓ Eliminados {count} slots huérfanos'))
        
        # Verificar Appointments sin referencias válidas
        all_appointments = Appointment.objects.all()
        orphan_appointment_ids = []
        for appt in all_appointments:
            try:
                _ = appt.doctor
                _ = appt.patient
                _ = appt.slot
            except:
                orphan_appointment_ids.append(appt.id)
        
        if orphan_appointment_ids:
            issues_found = True
            orphan_appointments = Appointment.objects.filter(id__in=orphan_appointment_ids)
            count = orphan_appointments.count()
            self.stdout.write(
                self.style.WARNING(f'⚠ Encontradas {count} citas con referencias inválidas')
            )
            if fix:
                orphan_appointments.delete()
                self.stdout.write(self.style.SUCCESS(f'✓ Eliminadas {count} citas huérfanas'))
        
        # Verificar DoctorRatings sin referencias válidas
        all_ratings = DoctorRating.objects.all()
        orphan_rating_ids = []
        for rating in all_ratings:
            try:
                _ = rating.doctor
                _ = rating.patient
            except:
                orphan_rating_ids.append(rating.id)
        
        if orphan_rating_ids:
            issues_found = True
            orphan_ratings = DoctorRating.objects.filter(id__in=orphan_rating_ids)
            count = orphan_ratings.count()
            self.stdout.write(
                self.style.WARNING(f'⚠ Encontradas {count} calificaciones con referencias inválidas')
            )
            if fix:
                orphan_ratings.delete()
                self.stdout.write(self.style.SUCCESS(f'✓ Eliminadas {count} calificaciones huérfanas'))
        
        # Verificar DoctorSchedules sin Doctor válido
        all_schedules = DoctorSchedule.objects.all()
        orphan_schedule_ids = []
        for schedule in all_schedules:
            try:
                _ = schedule.doctor
            except:
                orphan_schedule_ids.append(schedule.id)
        
        if orphan_schedule_ids:
            issues_found = True
            orphan_schedules = DoctorSchedule.objects.filter(id__in=orphan_schedule_ids)
            count = orphan_schedules.count()
            self.stdout.write(
                self.style.WARNING(f'⚠ Encontrados {count} horarios sin doctor válido')
            )
            if fix:
                orphan_schedules.delete()
                self.stdout.write(self.style.SUCCESS(f'✓ Eliminados {count} horarios huérfanos'))
        
        if not issues_found:
            self.stdout.write(self.style.SUCCESS('\n✓ No se encontraron problemas de integridad'))
        else:
            self.stdout.write('\n' + self.style.SUCCESS('✓ Verificación completada'))
            if not fix:
                self.stdout.write(
                    self.style.WARNING('\n💡 Ejecuta con --fix para eliminar registros huérfanos automáticamente')
                )
