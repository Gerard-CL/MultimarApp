package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// Importaciones necesarias para los campos de texto y corrutinas
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {

    // Declaramos las variables para los campos de texto
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellidos: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etTelefono: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        // 1. Vincular los componentes de la interfaz
        inicializarVistas()

        // 2. Configurar el menú inferior (Tu código original)
        configurarNavegacion()

        // 3. Llamar a la API para obtener los datos del usuario
        cargarDatosDeUsuario()
    }

    private fun inicializarVistas() {
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
    }

    private fun configurarNavegacion() {
        // Apagamos todas las barritas
        findViewById<View>(R.id.indicatorInicio).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorJuego).setBackgroundResource(android.R.color.transparent)
        findViewById<View>(R.id.indicatorEnvios).setBackgroundResource(android.R.color.transparent)

        // Encendemos SOLO la de Perfil
        findViewById<View>(R.id.indicatorPerfil).setBackgroundResource(R.color.btn_blue)

        // Navegacion
        findViewById<LinearLayout>(R.id.navInicio).setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.navEnvios).setOnClickListener {
            val intent = Intent(this, EnviosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
            // Ya estamos en Perfil, normalmente no hace falta abrir un intent nuevo aquí
        }
    }

    private fun cargarDatosDeUsuario() {
        // Hacemos la petición en un hilo secundario para no bloquear la app
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // AQUI VA TU LLAMADA A RETROFIT
                // val response = api.obtenerPerfilUsuario()

                // SIMULACIÓN: Imaginemos que la API nos devuelve este objeto con éxito
                val nombreApi = "Juan"
                val apellidosApi = "Peréz Garcia"
                val correoApi = "juanperez@gmail.com"
                val telefonoApi = "+34 654 489 321"

                // IMPORTANTE: Para modificar la pantalla, debemos volver al hilo principal (Main)
                withContext(Dispatchers.Main) {
                    etNombre.setText(nombreApi)
                    etApellidos.setText(apellidosApi)
                    etCorreo.setText(correoApi)
                    etTelefono.setText(telefonoApi)
                }

            } catch (e: Exception) {
                // Si la API falla o no hay internet
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}