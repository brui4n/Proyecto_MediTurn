import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Slot
import com.tecsup.mediturn.data.repository.SlotRepository
import com.tecsup.mediturn.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SlotViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SlotRepository(RetrofitInstance.slotApi(application))

    private val _slots = MutableStateFlow<List<Slot>>(emptyList())
    val slots: StateFlow<List<Slot>> = _slots

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSlots(doctorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getSlotsByDoctor(doctorId)
                _slots.value = data
                _error.value = null

                // Aquí agregas los logs de depuración
                println("DEBUG: Slots recibidos: ${data.size}")
                data.forEach { println("-> ${it.date} ${it.time}") }

            } catch (e: Exception) {
                _error.value = "Error al cargar horarios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}