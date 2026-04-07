package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)

        // Apagamos todas las barritas
        findViewById<View>(R.id.indicatorInicio).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorJuego).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorPerfil).setBackgroundResource(android.R.color.transparent)

        // Encendemos SOLO la de Envíos
        findViewById<View>(R.id.indicatorEnvios).setBackgroundResource(R.color.btn_blue)


        // Menu de navegación

        val btnAtras = findViewById<ImageView>(R.id.ivBackHistorial)

        btnAtras.setOnClickListener {
            onBackPressed() // Método clásico para volver atrás
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

    }
}