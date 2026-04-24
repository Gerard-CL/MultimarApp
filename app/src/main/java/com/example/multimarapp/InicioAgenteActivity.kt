package com.example.multimarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class InicioAgenteActivity : AppCompatActivity() {
    private lateinit var tvSaludoAgente: TextView // 1. Nueva variable
    // Variables para el resumen
    private lateinit var tvTotalOperaciones: TextView
    private lateinit var tvTotalClientes: TextView
    private lateinit var tvOperacionesEnCurso: TextView

    // Variable para las alertas
    private lateinit var contenedorAlertas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_agente)

        // Enlazamos las vistas con los nuevos IDs que creamos en el XML
        tvTotalOperaciones = findViewById(R.id.tv_total_operaciones)
        tvTotalClientes = findViewById(R.id.tv_total_clientes)
        tvOperacionesEnCurso = findViewById(R.id.tv_operaciones_curso)
        contenedorAlertas = findViewById(R.id.contenedor_alertas)
        tvSaludoAgente = findViewById(R.id.tv_saludo_agente)

        // Llamamos a la API
        cargarDatosDashboard()
    }

    private fun cargarDatosDashboard() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getApiService(this@InicioAgenteActivity)

                // CARGAR NOMBRE
                val responsePerfil = apiService.getPerfilBasicoAgente()
                if (responsePerfil.isSuccessful && responsePerfil.body() != null) {
                    val nombre = responsePerfil.body()!!.nom
                    tvSaludoAgente.text = "Hola, $nombre"
                }
                // CARGAR RESUMEN
                val responseResumen = apiService.getResumenAgente()
                if (responseResumen.isSuccessful && responseResumen.body() != null) {
                    val resumen = responseResumen.body()!!
                    tvTotalOperaciones.text = resumen.totalOperaciones.toString()
                    tvTotalClientes.text = resumen.totalClientes.toString()
                    tvOperacionesEnCurso.text = resumen.operacionesEnCurso.toString()
                }

                // CARGAR ALERTAS GLOBALES
                val responseAlertas = apiService.getAlertasGlobals()
                if (responseAlertas.isSuccessful && responseAlertas.body() != null) {
                    val alertas = responseAlertas.body()!!

                    contenedorAlertas.removeAllViews()

                    val inflater = LayoutInflater.from(this@InicioAgenteActivity)

                    // Recorremos la lista de alertas y creamos una tarjeta amarilla por cada una
                    for (alerta in alertas) {

                        val vistaAlerta = inflater.inflate(R.layout.item_alerta, contenedorAlertas, false)

                        val tvTitulo = vistaAlerta.findViewById<TextView>(R.id.tv_alerta_titulo)
                        val tvDescripcio = vistaAlerta.findViewById<TextView>(R.id.tv_alerta_descripcio)

                        tvTitulo.text = "${alerta.titol}:"
                        tvDescripcio.text = alerta.descripcio

                        contenedorAlertas.addView(vistaAlerta)
                    }

                    // Si no hay alertas, mostramos un mensaje amigable
                    if (alertas.isEmpty()) {
                        val tvVacio = TextView(this@InicioAgenteActivity).apply {
                            text = "No hay alertas activas en este momento."
                            setPadding(16, 16, 16, 16)
                        }
                        contenedorAlertas.addView(tvVacio)
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(this@InicioAgenteActivity, "Error conectando con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}