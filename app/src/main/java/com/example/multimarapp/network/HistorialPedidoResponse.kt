package com.example.multimarapp.network

data class HistorialPedidoResponse(
    val idPedido: Int,
    val ciudadOrigen: String,
    val ciudadDestino: String,
    val fechaFinalizacion: String?
)
