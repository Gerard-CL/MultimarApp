package com.example.multimarapp

import retrofit2.http.GET

interface ApiService {
    @GET("api/ofertes/Recente")
    suspend fun getPropuestaReciente(): Propuesta

    @GET("api/ofertes/Resumen")
    suspend fun getPropuestas(): List<Propuesta>
}

