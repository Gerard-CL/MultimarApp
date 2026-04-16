package com.example.multimarapp.network

data class NotificacionResponse(
    val id: Int,
    val nombreOperador: String,
    val fechaInicio: String,
    val fechaCaducidad: String,
    val precio: Double
)