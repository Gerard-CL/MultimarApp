package com.example.multimarapp

import retrofit2.http.GET

interface ApiService {
    @GET("api/ofertes/Recente")
    suspend fun getPropuestaReciente(): Propuesta

    @GET("api/ofertes/Resumen") // <-- ajusta la ruta a la de tu API
    suspend fun getPropuestas(): List<Propuesta>
}

