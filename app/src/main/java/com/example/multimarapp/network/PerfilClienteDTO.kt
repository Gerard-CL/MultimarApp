package com.example.multimarapp.network

data class PerfilClienteDTO(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val idioma: String,
    val dniSubido: Boolean // <-- NUEVO CAMPO
)
