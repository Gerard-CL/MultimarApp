package com.example.multimarapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.multimarapp.adapters.EnviosAdapter
import android.content.Intent
import android.widget.ImageView

class InicioActivity : AppCompatActivity() {

    // Declaramos el adapter arriba para poder usarlo en toda la clase
    private lateinit var enviosAdapter: EnviosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // --- NUEVO CÓDIGO PARA EL BOTÓN DE LA CAMPANA ---
        val imgNotification = findViewById<ImageView>(R.id.imgNotification)

        imgNotification.setOnClickListener {
            // Creamos la intención de ir de esta pantalla a NotificacionesActivity
            val intent = Intent(this@InicioActivity, NotificacionesActivity::class.java)
            startActivity(intent)
        }
        // -------------------------------------------------

        configurarRecyclerView()
        cargarDatosDelDashboard()
    }

    private fun configurarRecyclerView() {
        val rvEnvios = findViewById<RecyclerView>(R.id.rvEnvios)
        // Le decimos que se muestre como una lista vertical
        rvEnvios.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adapter con una lista vacía de momento
        enviosAdapter = EnviosAdapter(emptyList())
        rvEnvios.adapter = enviosAdapter
    }

    private fun cargarDatosDelDashboard() {
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvTotalValue = findViewById<TextView>(R.id.tvTotalValue)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)

        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                // 1. Cargar Perfil
                val perfilRes = api.getPerfil()
                if (perfilRes.isSuccessful && perfilRes.body() != null) {
                    tvGreeting.text = "Hola, ${perfilRes.body()!!.nom}"
                }

                // 2. Cargar Última Propuesta
                val propuestaRes = api.getUltimaPropuesta()
                if (propuestaRes.isSuccessful) {
                    if (propuestaRes.code() == 204) {
                        tvTotalValue.text = "0.00 €"
                        tvFecha.text = "Sin propuestas"
                    } else if (propuestaRes.body() != null) {
                        val propuesta = propuestaRes.body()!!
                        tvTotalValue.text = "${propuesta.precio} €"
                        tvFecha.text = "Fecha: ${propuesta.fechaInicio.take(10)}"
                    }
                }

                // 3. Cargar Últimos Envíos
                val enviosRes = api.getUltimosEnvios()
                if (enviosRes.isSuccessful && enviosRes.body() != null) {
                    val listaEnvios = enviosRes.body()!!
                    // AQUÍ ESTÁ LA MAGIA: Le pasamos la lista de internet a nuestro Adapter
                    enviosAdapter.actualizarDatos(listaEnvios)
                }

            } catch (e: Exception) {
                Toast.makeText(this@InicioActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}