package com.tecsup.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que decide si el usuario ya tiene una sesión activa
 * para redirigir desde el SplashScreen al Home o al Login.
 *
 */
class SplashViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn

    init {
        viewModelScope.launch {
            sessionManager.userSession.collect { session ->
                // Si existe una sesión, el usuario está logueado
                _isUserLoggedIn.value = session != null
            }
        }
    }
}