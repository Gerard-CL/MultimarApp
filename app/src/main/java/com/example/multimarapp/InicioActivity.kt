package com.example.multimarapp
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InicioActivity : AppCompatActivity() { // <-- El nombre de tu clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí cargas el XML que diseñamos (asegúrate de que el nombre sea el correcto)
        setContentView(R.layout.activity_inicio)

        findViewById<View>(R.id.indicatorInicio).setBackgroundResource(R.color.btn_blue)

        // 1. Buscamos el RecyclerView en nuestro XML
        val rvEnvios = findViewById<RecyclerView>(R.id.rvEnvios)

        // 2. Le decimos cómo debe organizarse
        rvEnvios.layoutManager = LinearLayoutManager(this)

        // 3. Creamos nuestra lista de datos
        val listaDeEnvios = listOf(
            Envio("080205340", "Juan", "EEUU", "Enviado", "En camino"),
            Envio("080205340", "Juan", "EEUU", "En camino", "En camino"),
            Envio("080205340", "Juan", "EEUU", "Entregado", "En camino")
                                  )

        // 4. Se lo pasamos al Adaptador
        val adapter = EnvioAdapter(listaDeEnvios)
        rvEnvios.adapter = adapter

        // 1. Buscamos la campana por su ID
        val btnCampana = findViewById<ImageView>(R.id.imgNotification)

// 2. Le asignamos la acción del clic
        btnCampana.setOnClickListener {
            val intent = Intent(this, NotificacionesActivity::class.java)
            startActivity(intent)
            // Esto quita la animación de transición
            overridePendingTransition(0, 0)
        }

        // 1. Buscamos el botón de "Envíos" en el menú inferior por su ID
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