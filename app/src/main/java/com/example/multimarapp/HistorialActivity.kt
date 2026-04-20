package com.example.multimarapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.adapters.HistorialAdapter
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class HistorialActivity : AppCompatActivity() {

    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        configurarRecyclerView()
        configurarBotonAtras()
        configurarBuscador()

        cargarHistorial()
    }

    private fun configurarRecyclerView() {
        val rvHistorial = findViewById<RecyclerView>(R.id.rvHistorial)
        rvHistorial.layoutManager = LinearLayoutManager(this)

        historialAdapter = HistorialAdapter(emptyList())
        rvHistorial.adapter = historialAdapter
    }

    private fun configurarBotonAtras() {
        val ivBackHistorial = findViewById<ImageView>(R.id.ivBackHistorial)
        ivBackHistorial.setOnClickListener {
            finish() // Destruye esta pantalla y vuelve a EnviosActivity
        }
    }

    private fun configurarBuscador() {
        val etSearch = findViewById<EditText>(R.id.etSearch)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                historialAdapter.filtrar(s.toString())
            }
        })
    }

    private fun cargarHistorial() {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = api.getHistorialPedidos()

                if (response.isSuccessful && response.body() != null) {
                    historialAdapter.actualizarDatos(response.body()!!)
                } else {
                    Toast.makeText(this@HistorialActivity, "Error al cargar el historial", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HistorialActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}