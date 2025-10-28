package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties
import java.io.File

object RetrofitInstance {

    private val BASE_URL: String by lazy {
        try {
            val props = Properties()
            val file = File("local.properties")
            if (file.exists()) {
                props.load(file.inputStream())
                val url = props.getProperty("BASE_URL")
                if (!url.isNullOrBlank()) url else "http://10.0.2.2:8000/api/"
            } else {
                "http://10.0.2.2:8000/api/"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "http://10.0.2.2:8000/api/"
        }
    }

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}