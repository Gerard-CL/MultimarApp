package com.example.multimarapp.network

data class SeguimientoOperacionResponse(
    val idOperacion: Int,
    val ciudadOrigen: String,
    val ciudadDestino: String,
    val incoterm: String,
    val tracking: List<PasoTrackingResponse>
)
