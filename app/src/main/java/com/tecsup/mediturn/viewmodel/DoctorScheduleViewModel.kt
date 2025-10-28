package com.tecsup.mediturn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.DoctorSchedule
import com.tecsup.mediturn.data.repository.DoctorScheduleRepository
import com.tecsup.mediturn.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DoctorScheduleRepository(application)

    private val _schedules = MutableStateFlow<Resource<List<DoctorSchedule>>>(Resource.Loading())
    val schedules: StateFlow<Resource<List<DoctorSchedule>>> = _schedules

    fun loadSchedulesByDoctor(doctorId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getDoctorSchedulesByDoctor(doctorId)
                _schedules.value = Resource.Success(response)
            } catch (e: Exception) {
                _schedules.value = Resource.Error(e.localizedMessage ?: "Error al obtener horarios")
            }
        }
    }
}