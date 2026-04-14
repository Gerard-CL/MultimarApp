package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PedidosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pedidos)

//Navegació

        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        navInicio.setOnClickListener {
            val intent = Intent(this, IniioActivityAgente::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)
        navPerfil.setOnClickListener {
            val intent = Intent(this, PerfilAgenteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }    }
}