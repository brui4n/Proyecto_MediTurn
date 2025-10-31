"""
Comando de gestión para regenerar los slots diarios basados en los horarios semanales de los doctores.

Uso:
    python manage.py regenerate_slots
    
Esto generará slots para los próximos 30 días basados en los DoctorSchedule existentes.
"""
from django.core.management.base import BaseCommand
from api.tasks import generate_daily_slots


class Command(BaseCommand):
    help = 'Regenera los slots diarios basados en los horarios semanales de los doctores'

    def handle(self, *args, **options):
        self.stdout.write('Generando slots diarios...')
        try:
            slots_created = generate_daily_slots()
            self.stdout.write(
                self.style.SUCCESS(f'✓ Slots regenerados exitosamente. Creados: {slots_created} nuevos slots')
            )
        except Exception as e:
            self.stdout.write(
                self.style.ERROR(f'✗ Error al generar slots: {str(e)}')
            )
            raise

