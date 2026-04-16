package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class InicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // ---------------------------------------------------------
        // 1. CONFIGURACIÓN DE NAVEGACIÓN Y BOTONES
        // ---------------------------------------------------------
        findViewById<View>(R.id.indicatorInicio).setBackgroundResource(R.color.btn_blue)

        val btnCampana = findViewById<ImageView>(R.id.imgNotification)
        btnCampana.setOnClickListener {
            val intent = Intent(this, NotificacionesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val navEnvios = findViewById<LinearLayout>(R.id.navEnvios)
        navEnvios.setOnClickListener {
            val intent = Intent(this, EnviosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)
        navPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        // ---------------------------------------------------------
        // 2. CONFIGURACIÓN DEL RECYCLERVIEW (Datos falsos temporales)
        // ---------------------------------------------------------
        val rvEnvios = findViewById<RecyclerView>(R.id.rvEnvios)
        rvEnvios.layoutManager = LinearLayoutManager(this)

        val listaDeEnvios = listOf(
            Envio("080205340", "Juan", "EEUU", "Enviado"),
            Envio("080205340", "Juan", "EEUU", "En camino"),
            Envio("080205340", "Juan", "EEUU", "Entregado")
        )

        val adapter = EnvioAdapter(listaDeEnvios)
        rvEnvios.adapter = adapter

        // ---------------------------------------------------------
        // 3. LLAMADA A LA API OPTIMIZADA
        // ---------------------------------------------------------
        val tvTotalValue = findViewById<TextView>(R.id.tvTotalValue)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)

        // Usamos la corrutina directamente llamando a nuestro Singleton
        lifecycleScope.launch {
            try {
                // Fíjate cómo ahora simplemente llamamos a RetrofitClient.apiService
                val propuesta = RetrofitClient.apiService.getPropuestaReciente()

                // Si todo va bien, pintamos los datos
                tvTotalValue.text = "${propuesta.preu} €"
                tvFecha.text = "Fecha: ${propuesta.dataValidessaInicial}"

            } catch (e: Exception) {
                // Si hay error (API apagada, sin internet, etc), mostramos un mensaje
                Toast.makeText(this@InicioActivity, "Error al cargar la propuesta", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}