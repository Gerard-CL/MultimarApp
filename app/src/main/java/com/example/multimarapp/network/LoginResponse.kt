package com.example.multimarapp.network

// Esta clase representa la respuesta que recibimos DE LA API
data class LoginResponse(
    val token: String, // Nombre exacto del campo que devuelve tu API (ej. "token")
    val mensaje: String,
    val usuarioId: Int,  // Fíjate que es Int, porque en C# es int
    val rolId: Int       // Fíjate que es Int, porque en C# es int
)
