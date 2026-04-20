package com.example.multimarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.adapters.PedidosAdapter
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class EnviosActivity : AppCompatActivity() {
    private lateinit var pedidosAdapter: PedidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envios)

        configurarRecyclerView()
        configurarBotonHistorial()
        configurarBuscador()

        configurarNavegacionInferior()

        cargarPedidos(null)
    }

    private fun configurarRecyclerView() {
        val rvTodosEnvios = findViewById<RecyclerView>(R.id.rvTodosEnvios)
        rvTodosEnvios.layoutManager = LinearLayoutManager(this)

        pedidosAdapter = PedidosAdapter(emptyList())
        rvTodosEnvios.adapter = pedidosAdapter
    }

    private fun configurarBotonHistorial() {
        val tvHistorial = findViewById<TextView>(R.id.tvHistorial)
        tvHistorial.setOnClickListener {
            val intent = Intent(this@EnviosActivity, HistorialActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarBuscador() {
        val etSearch = findViewById<EditText>(R.id.etSearch)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString()

                val filtro = if (textoBusqueda.isNotEmpty()) textoBusqueda else null
                cargarPedidos(filtro)
            }
        })
    }

    private fun cargarPedidos(busqueda: String?) {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {

                val response = api.getPedidosActivos(busqueda)

                if (response.isSuccessful && response.body() != null) {
                    val listaPedidos = response.body()!!
                    pedidosAdapter.actualizarDatos(listaPedidos)
                } else {
                    Toast.makeText(this@EnviosActivity, "Error al cargar pedidos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EnviosActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarNavegacionInferior() {
        val navInicio = findViewById<View>(R.id.navInicio)
        val navPerfil = findViewById<View>(R.id.navPerfil)


        navInicio.setOnClickListener {
            val intent = Intent(this@EnviosActivity, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        navPerfil.setOnClickListener {
            val intent = Intent(this@EnviosActivity, PerfilActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}