package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multimarapp.network.RechazoRequest
import com.example.multimarapp.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DetallesOferta : AppCompatActivity() {

    private var ofertaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_oferta)

        // 1. Recogemos el ID que nos ha enviado el Adapter
        ofertaId = intent.getIntExtra("ID_OFERTA", -1)

        // 2. Botón de volver atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Cierra esta pantalla y vuelve a la anterior
        }

        if (ofertaId != -1) {
            cargarDetalles(ofertaId)
            configurarBotones()
        } else {
            Toast.makeText(this, "Error al cargar la oferta", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarDetalles(id: Int) {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = api.getDetallePropuesta(id)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!

                    // Inyectamos los datos en el diseño
                    findViewById<TextView>(R.id.tvIdDetalle).text = datos.id.toString()
                    findViewById<TextView>(R.id.tvOperador).text = datos.operadorLogistico
                    findViewById<TextView>(R.id.tvFechaValida).text = datos.fechaCaducidad.take(10)

                    findViewById<TextView>(R.id.tvOrigen).text = "Origen: ${datos.origen}"
                    findViewById<TextView>(R.id.tvDestino).text = "Destino: ${datos.destino}"

                    findViewById<TextView>(R.id.tvModalidad).text = "Modo: ${datos.tipoTransporte} | Incoterm: ${datos.incoterm}"

                    val peso = datos.peso?.toString() ?: "0"
                    val volumen = datos.volumen?.toString() ?: "0"
                    findViewById<TextView>(R.id.tvCarga).text = "Peso: $peso Kg | Volumen: $volumen m³"

                    findViewById<TextView>(R.id.tvFlujo).text = datos.flujo
                    findViewById<TextView>(R.id.tvPrecioTotal).text = "${datos.precio} €"
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetallesOferta, "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarBotones() {
        val btnAceptar = findViewById<MaterialButton>(R.id.btnAceptar)
        val btnRechazar = findViewById<MaterialButton>(R.id.btnRechazar)

        btnAceptar.setOnClickListener {
            gestionarPropuesta(aceptar = true, razon = "")
        }

        btnRechazar.setOnClickListener {
            mostrarDialogoRechazo()
        }
    }

    private fun mostrarDialogoRechazo() {
        // Creamos la "ventana flotante" para pedir la razón
        val input = EditText(this)
        input.hint = "Escribe el motivo del rechazo..."

        AlertDialog.Builder(this)
            .setTitle("Rechazar Propuesta")
            .setMessage("¿Por qué deseas rechazar esta propuesta?")
            .setView(input) // Metemos la caja de texto en la ventana
            .setPositiveButton("Enviar") { _, _ ->
                val razon = input.text.toString()
                if (razon.isNotEmpty()) {
                    gestionarPropuesta(aceptar = false, razon = razon)
                } else {
                    Toast.makeText(this, "Debes escribir un motivo", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun gestionarPropuesta(aceptar: Boolean, razon: String) {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = if (aceptar) {
                    api.aceptarPropuesta(ofertaId)
                } else {
                    api.rechazarPropuesta(ofertaId, RechazoRequest(razon))
                }

                if (response.isSuccessful) {
                    val mensaje = if (aceptar) "Propuesta Aceptada" else "Propuesta Rechazada"
                    Toast.makeText(this@DetallesOferta, mensaje, Toast.LENGTH_SHORT).show()

                    // Como la oferta ya no debe salir en Notificaciones, volvemos a Inicio
                    val intent = Intent(this@DetallesOferta, InicioActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Limpia el historial de pantallas
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetallesOferta, "Error al procesar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}