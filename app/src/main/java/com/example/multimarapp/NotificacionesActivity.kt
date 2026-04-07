package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notificaciones)

        // 1. Buscamos el contenedor del botón Inicio en el menú inferior
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)

// 2. Le asignamos el clic para volver
        navInicio.setOnClickListener {
            // Creamos el Intent para ir a la pantalla de Inicio
            val intent = Intent(this, InicioActivity::class.java)

            // TRUCO: Esta bandera hace que si la pantalla de Inicio ya estaba abierta,
            // simplemente "vuelva" a ella en lugar de crear una nueva desde cero.
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

            startActivity(intent)

            // Opcional: Quitar animación para que se sienta instantáneo
            overridePendingTransition(0, 0)
        }

    }
}