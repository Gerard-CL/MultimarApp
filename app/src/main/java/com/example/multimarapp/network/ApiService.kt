package com.example.multimarapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/DashboardCliente/Perfil")
    suspend fun getPerfil(): Response<PerfilResponse>

    @GET("api/DashboardCliente/UltimaPropuesta")
    suspend fun getUltimaPropuesta(): Response<PropuestaResponse>

    @GET("api/DashboardCliente/UltimosEnvios")
    suspend fun getUltimosEnvios(): Response<List<EnvioResponse>>

    @GET("api/DashboardCliente/TodasLasPropuestas")
    suspend fun getTodasLasPropuestas(): retrofit2.Response<List<NotificacionResponse>>

    @GET("api/DashboardCliente/DetallePropuesta/{id}")
    suspend fun getDetallePropuesta(@Path("id") id: Int): retrofit2.Response<PropuestaDetalleResponse>

    @PUT("api/DashboardCliente/AceptarPropuesta/{id}")
    suspend fun aceptarPropuesta(@Path("id") id: Int): retrofit2.Response<Any>

    @PUT("api/DashboardCliente/RechazarPropuesta/{id}")
    suspend fun rechazarPropuesta(@Path("id") id: Int, @Body body: RechazoRequest): retrofit2.Response<Any>

    @GET("api/DashboardCliente/PedidosActivos") // Ajusta 'DashboardCliente' si el controlador se llama distinto
    suspend fun getPedidosActivos(@Query("busqueda") busqueda: String? = null): retrofit2.Response<List<PedidoActivoResponse>>

    @GET("api/DashboardCliente/HistorialPedidos")
    suspend fun getHistorialPedidos(): retrofit2.Response<List<HistorialPedidoResponse>>

    @GET("api/DashboardCliente/Seguimiento/{idOperacion}")
    suspend fun getSeguimientoOperacion(@Path("idOperacion") idOperacion: Int): retrofit2.Response<SeguimientoOperacionResponse>

    // Cambia "Cliente" por el nombre de tu Controlador en C#
    @GET("api/DashboardCliente/PerfilCliente")
    suspend fun getPerfilCliente(): Response<PerfilClienteDTO>

    @PUT("api/DashboardCliente/PerfilCliente")
    suspend fun actualizarPerfil(@Body datosActualizados: PerfilClienteDTO): Response<Any>
}