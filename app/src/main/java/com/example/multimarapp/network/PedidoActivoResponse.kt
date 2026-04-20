package com.example.multimarapp.network

data class PedidoActivoResponse(
    val idPedido: Int,
    val pasoActual: String,
    val ciudadOrigen: String,
    val ciudadDestino: String
)
