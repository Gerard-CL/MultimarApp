package com.example.multimarapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.adapters.TimelineAdapter
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class DetalleEnviosActivity : AppCompatActivity() {

    private lateinit var timelineAdapter: TimelineAdapter
    private var idPedido: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_envios)

        idPedido = intent.getIntExtra("EXTRA_ID_PEDIDO", -1)

        configurarBotones()
        configurarRecyclerView()

        if (idPedido != -1) {
            cargarDetallePedido()
        } else {
            Toast.makeText(this, "Error: No se recibió el ID del pedido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun configurarBotones() {
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun configurarRecyclerView() {
        val rvTimeline = findViewById<RecyclerView>(R.id.rvTimeline)

        rvTimeline.layoutManager = LinearLayoutManager(this)
        rvTimeline.isNestedScrollingEnabled = false

        timelineAdapter = TimelineAdapter(emptyList())
        rvTimeline.adapter = timelineAdapter
    }

    private fun cargarDetallePedido() {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = api.getSeguimientoOperacion(idPedido)

                if (response.isSuccessful && response.body() != null) {
                    val detalle = response.body()!!
                    pintarDatosEnPantalla(detalle)
                } else {
                    Toast.makeText(this@DetalleEnviosActivity, "Error al cargar los detalles", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalleEnviosActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pintarDatosEnPantalla(detalle: com.example.multimarapp.network.SeguimientoOperacionResponse) {
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvPuertoOrigen = findViewById<TextView>(R.id.tvPuertoOrigen)
        val tvPuertoDestino = findViewById<TextView>(R.id.tvPuertoDestino)
        val tvIcoterm = findViewById<TextView>(R.id.tvIcoterm)

        tvTitle.text = "PEDIDO #${detalle.idOperacion}"
        tvPuertoOrigen.text = detalle.ciudadOrigen
        tvPuertoDestino.text = detalle.ciudadDestino
        tvIcoterm.text = detalle.incoterm

        timelineAdapter.actualizarDatos(detalle.tracking)
    }
}