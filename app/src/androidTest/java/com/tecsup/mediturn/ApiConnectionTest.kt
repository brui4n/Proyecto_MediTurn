package com.tecsup.mediturn

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tecsup.mediturn.data.remote.RetrofitInstance
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApiConnectionTest {

    @Test
    fun testGetDoctorsFromEmulator() = runBlocking {
        val doctors = RetrofitInstance.api.getDoctors()
        println("✅ Doctores recibidos: ${doctors.size}")
        assert(doctors.isNotEmpty()) { "La lista de doctores está vacía" }
    }
}