package com.example.multimarapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/ofertes/Recente")
    suspend fun getPropuestaReciente(): Propuesta

    @GET("api/ofertes/Resumen")
    suspend fun getPropuestas(): List<Propuesta>

    @GET("api/usuarios/mi-perfil")
    suspend fun getDatosPerfil(): PerfilResponse

    //Pagina de Envios de cliente
    @GET("api/ofertas/envios")
    suspend fun getListaEnvios(): List<Envio>

    
}

