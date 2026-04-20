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
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnEntrar = findViewById<Button>(R.id.btn_login)

        btnEntrar.setOnClickListener {

            val emailEscrito = etEmail.text.toString()
            val passwordEscrito = etPassword.text.toString()

            if (emailEscrito.isEmpty() || passwordEscrito.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Hacemos la llamada a la API en segundo plano
            lifecycleScope.launch {
                try {

                    val request = LoginRequest(email = emailEscrito, password = passwordEscrito)

                    val response = RetrofitClient.getApiService(this@LoginActivity).login(request)

                    if (response.isSuccessful && response.body() != null) {

                        val token = response.body()!!.token

                        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        sharedPreferences.edit().putString("TOKEN", token).apply()

                        val intent = Intent(this@LoginActivity, InicioActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    println("ERROR DE RED: ${e.message}")
                }
            }
        }
    }
}