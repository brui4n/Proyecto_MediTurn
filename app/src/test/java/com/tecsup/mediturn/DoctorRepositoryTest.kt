package com.tecsup.mediturn

import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DoctorRepositoryTest {

    @Test
    fun testGetDoctors() = runBlocking {
        val repo = DoctorRepository()
        val doctors = repo.getDoctors()
        println("✅ Número de doctores: ${doctors.size}")
        assert(doctors.isNotEmpty())
    }
}