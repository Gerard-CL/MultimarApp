package com.example.multimarapp.network

data class LoginResponse(
    val token: String,
    val mensaje: String,
    val usuarioId: Int,
    val rolId: Int
)
