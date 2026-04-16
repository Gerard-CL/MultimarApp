package com.example.multimarapp.network

data class PropuestaDetalleResponse(
    val id: Int,
    val operadorLogistico: String,
    val fechaCaducidad: String,
    val origen: String,
    val destino: String,
    val tipoTransporte: String,
    val incoterm: String,
    val peso: Double?,
    val volumen: Double?,
    val flujo: String,
    val precio: Double
)

data class RechazoRequest(
    val razon: String
)