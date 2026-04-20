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
import android.view.View
import android.widget.ImageView

class InicioActivity : AppCompatActivity() {

    private lateinit var enviosAdapter: EnviosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val imgNotification = findViewById<ImageView>(R.id.imgNotification)

        imgNotification.setOnClickListener {
            val intent = Intent(this@InicioActivity, NotificacionesActivity::class.java)
            startActivity(intent)
        }

        configurarNavegacionInferior()

        configurarRecyclerView()
        cargarDatosDelDashboard()
    }

    private fun configurarRecyclerView() {
        val rvEnvios = findViewById<RecyclerView>(R.id.rvEnvios)
        rvEnvios.layoutManager = LinearLayoutManager(this)

        enviosAdapter = EnviosAdapter(emptyList())
        rvEnvios.adapter = enviosAdapter
    }

    private fun cargarDatosDelDashboard() {
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvTotalValue = findViewById<TextView>(R.id.tvTotalValue)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)

        val btnVerDetalles = findViewById<View>(R.id.btnVerDetalles)

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

                        btnVerDetalles.visibility = View.GONE
                    } else if (propuestaRes.body() != null) {
                        val propuesta = propuestaRes.body()!!
                        tvTotalValue.text = "${propuesta.precio} €"
                        tvFecha.text = "Fecha: ${propuesta.fechaInicio.take(10)}"

                        btnVerDetalles.visibility = View.VISIBLE
                        btnVerDetalles.setOnClickListener {
                            val intent = Intent(this@InicioActivity, DetallesOferta::class.java)
                            intent.putExtra("ID_OFERTA", propuesta.id)
                            startActivity(intent)
                        }
                    }
                }

                // 3. Cargar Últimos Envíos
                val enviosRes = api.getUltimosEnvios()
                if (enviosRes.isSuccessful && enviosRes.body() != null) {
                    val listaEnvios = enviosRes.body()!!
                    enviosAdapter.actualizarDatos(listaEnvios)
                }

            } catch (e: Exception) {
                Toast.makeText(this@InicioActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarNavegacionInferior() {
        // Botón de Envíos
        val navEnvios = findViewById<View>(R.id.navEnvios)
        navEnvios.setOnClickListener {
            val intent = Intent(this@InicioActivity, EnviosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Botón de Perfil
        val navPerfil = findViewById<View>(R.id.navPerfil)
        navPerfil.setOnClickListener {
            val intent = Intent(this@InicioActivity, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}