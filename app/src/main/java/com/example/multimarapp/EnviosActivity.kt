package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class EnviosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envios)

        // ---------------------------------------------------------
        // 1. CONFIGURACIÓN DE LAS BARRITAS DE NAVEGACIÓN
        // ---------------------------------------------------------
        // Apagamos todas las barritas
        findViewById<View>(R.id.indicatorInicio).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorJuego).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorPerfil).setBackgroundResource(android.R.color.transparent)

        // Encendemos SOLO la de Envíos
        findViewById<View>(R.id.indicatorEnvios).setBackgroundResource(R.color.btn_blue)

        // ---------------------------------------------------------
        // 2. CONFIGURACIÓN DE LOS CLICS DE NAVEGACIÓN
        // ---------------------------------------------------------
        val btnVerHistorial = findViewById<TextView>(R.id.tvHistorial)
        btnVerHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        navInicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
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
        // 3. CONFIGURACIÓN DEL RECYCLERVIEW Y LA API (NUEVO)
        // ---------------------------------------------------------

        // Buscamos la lista en el XML y le decimos que se pinte en vertical
        val rvEnvios = findViewById<RecyclerView>(R.id.rvEnvios) // OJO: Comprueba que tu ID en el XML es rvEnvios
        rvEnvios.layoutManager = LinearLayoutManager(this)

        // Creamos el Adapter arrancando con una lista vacía
        val adapterEnvios = EnvioAdapter(emptyList())
        rvEnvios.adapter = adapterEnvios

        // Lanzamos la llamada a Visual Studio en segundo plano
        lifecycleScope.launch {
            try {
                // Hacemos la petición a la ruta configurada en tu ApiService
                val enviosReales = RetrofitClient.apiService.getListaEnvios()

                // Si la API responde correctamente, inyectamos los datos en la pantalla
                adapterEnvios.actualizarLista(enviosReales)

            } catch (e: Exception) {
                // Si algo falla, avisamos al usuario y mostramos el error en el Logcat
                Toast.makeText(this@EnviosActivity, "Error al cargar la lista de envíos", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}