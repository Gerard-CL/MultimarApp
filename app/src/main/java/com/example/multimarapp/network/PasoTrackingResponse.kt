package com.example.multimarapp.network

data class PasoTrackingResponse(
    val nombrePaso: String,
    val estado: String,
    val fechaCompletado: String?, // Puede ser null si aún no se ha completado
    val orden: Int
)
