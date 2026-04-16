package com.example.multimarapp.network

// Esta clase representa los datos que enviamos A LA API
data class LoginRequest(
    val email: String,      // Nombre exacto del campo en tu JSON/BD C#
    val password: String  // Nombre exacto del campo en tu JSON/BD C#
)
