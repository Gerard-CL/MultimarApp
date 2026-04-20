package com.example.multimarapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.adapters.NotificacionesAdapter
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class NotificacionesActivity : AppCompatActivity() {

    private lateinit var notificacionesAdapter: NotificacionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        configurarRecyclerView()
        cargarNotificaciones()
    }

    private fun configurarRecyclerView() {
        val rvNotificaciones = findViewById<RecyclerView>(R.id.rvNotificaciones)
        rvNotificaciones.layoutManager = LinearLayoutManager(this)

        notificacionesAdapter = NotificacionesAdapter(emptyList())
        rvNotificaciones.adapter = notificacionesAdapter
    }

    private fun cargarNotificaciones() {

        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = api.getTodasLasPropuestas()

                if (response.isSuccessful && response.body() != null) {
                    val listaNotificaciones = response.body()!!

                    if (listaNotificaciones.isEmpty()) {
                        Toast.makeText(this@NotificacionesActivity, "No tienes nuevas propuestas", Toast.LENGTH_SHORT).show()
                    } else {
                        notificacionesAdapter.actualizarDatos(listaNotificaciones)
                    }
                } else {
                    Toast.makeText(this@NotificacionesActivity, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@NotificacionesActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}