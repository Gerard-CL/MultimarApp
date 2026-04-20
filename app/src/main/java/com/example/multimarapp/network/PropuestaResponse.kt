package com.example.multimarapp.network

data class PropuestaResponse(
    val id: Int,
    val nombreOperador: String,
    val precio: Double,
    val fechaInicio: String, // Ojo, C# devuelve DateTime, GSON lo lee como String
    val fechaCaducidad: String
)