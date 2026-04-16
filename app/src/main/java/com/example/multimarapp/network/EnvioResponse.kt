package com.example.multimarapp.network

data class EnvioResponse(
    val id: Int,
    val ciudadOrigen: String,
    val ciudadDestino: String,
    val pasoActual: String
)