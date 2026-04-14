package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        // ---------------------------------------------------------
        // 1. NAVEGACIÓN - Botón Inicio
        // ---------------------------------------------------------
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        navInicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        // ---------------------------------------------------------
        // 2. RECYCLERVIEW
        // ---------------------------------------------------------
        val rvNotificaciones = findViewById<RecyclerView>(R.id.rvNotificaciones)
        rvNotificaciones.layoutManager = LinearLayoutManager(this)

        // ---------------------------------------------------------
        // 3. RETROFIT + LLAMADA A LA API
        // ---------------------------------------------------------
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        lifecycleScope.launch {
            try {
                val propuestas = api.getPropuestas()
                val adapter = PropuestaAdapter(propuestas)
                rvNotificaciones.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(
                    this@NotificacionesActivity,
                    "Error al cargar las notificaciones",
                    Toast.LENGTH_LONG
                              ).show()
                e.printStackTrace()
            }
        }
    }
}