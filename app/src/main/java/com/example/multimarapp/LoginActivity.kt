package com.example.multimarapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multimarapp.network.LoginRequest
import com.example.multimarapp.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Asegúrate de que este es tu XML de login

        // 1. Enlazamos las vistas de tu XML con el código Kotlin
        val etEmail = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnEntrar = findViewById<Button>(R.id.btn_login)

        // 2. Le decimos qué hacer cuando se hace clic en el botón
        btnEntrar.setOnClickListener {
            // Recogemos el texto que ha escrito el usuario
            val emailEscrito = etEmail.text.toString()
            val passwordEscrito = etPassword.text.toString()

            // Comprobamos que no estén vacíos
            if (emailEscrito.isEmpty() || passwordEscrito.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Paramos la ejecución aquí
            }

            // 3. Hacemos la llamada a la API en segundo plano (Corrutina)
            // Usamos lifecycleScope para que no se bloquee la pantalla
            lifecycleScope.launch {
                try {
                    // Preparamos el "sobre" de datos
                    val request = LoginRequest(email = emailEscrito, password = passwordEscrito)

                    // ¡Enviamos los datos a C#!
                    val response = RetrofitClient.getApiService(this@LoginActivity).login(request)

                    // 4. Evaluamos la respuesta de C#
                    if (response.isSuccessful && response.body() != null) {
                        // ¡Ha ido bien! Sacamos el token de la respuesta
                        val token = response.body()!!.token

                        // 1. Guardamos el token en la memoria del móvil
                        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        sharedPreferences.edit().putString("TOKEN", token).apply()

                        // 2. Saltamos a la pantalla de Inicio
                        val intent = Intent(this@LoginActivity, InicioActivity::class.java)
                        startActivity(intent)
                        finish() // Cerramos el Login para que no pueda volver atrás con el botón de retroceso

                    } else {
                        // C# nos ha dicho que las credenciales están mal (Error 401)
                        Toast.makeText(this@LoginActivity, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Error crítico (ej: Servidor apagado, puerto incorrecto, sin internet)
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    println("ERROR DE RED: ${e.message}")
                }
            }
        }
    }
}