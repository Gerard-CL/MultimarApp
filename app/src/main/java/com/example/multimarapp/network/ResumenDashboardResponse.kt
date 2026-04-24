package com.example.multimarapp.network

data class ResumenDashboardResponse(
    val totalOperaciones: Int,
    val totalClientes: Int,
    val operacionesEnCurso: Int
)