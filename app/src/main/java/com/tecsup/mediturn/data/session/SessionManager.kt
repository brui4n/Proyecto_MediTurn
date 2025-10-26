package com.tecsup.mediturn.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia de DataStore
val Context.dataStore by preferencesDataStore("user_prefs")

/**
 * Administra la sesión del usuario logueado usando DataStore.
 * Permite guardar, leer y limpiar los datos del paciente actual.
 */
class SessionManager(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
    }

    // Guarda los datos del usuario actual
    suspend fun saveUserSession(id: Int, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    // Recupera los datos del usuario actual (flujo reactivo)
    val userSession: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        val id = prefs[USER_ID] ?: return@map null
        val name = prefs[USER_NAME] ?: ""
        val email = prefs[USER_EMAIL] ?: ""
        UserSession(id, name, email)
    }

    // Limpia la sesión (logout)
    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}

// Representa la sesión activa del usuario
data class UserSession(
    val id: Int,
    val name: String,
    val email: String
)