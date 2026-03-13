package com.example.multimarapp


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. CONEXIÓN DEL ARCHIVO COMPLETO:
        // "R.layout.activity_login" debe ser el nombre exacto de tu archivo XML
        setContentView(R.layout.activity_login)

        // 2. CONEXIÓN DE LOS ELEMENTOS (Vistas):
        // Buscamos los elementos por el ID que les dimos en el XML
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        // 3. DARLE FUNCIONALIDAD AL BOTÓN:
        btnLogin.setOnClickListener {
            // Obtenemos el texto que escribió el usuario
            val usernameStr = etUsername.text.toString()
            val passwordStr = etPassword.text.toString()

            // Pequeña validación de ejemplo
            if (usernameStr.isNotEmpty() && passwordStr.isNotEmpty()) {
                // Si hay texto, mostramos un mensaje emergente
                Toast.makeText(this, "Iniciando sesión como: $usernameStr", Toast.LENGTH_SHORT).show()

                // AQUÍ IRÍA TU LÓGICA REAL: verificar en base de datos, ir a otra pantalla, etc.

            } else {
                // Si está vacío, le avisamos al usuario
                Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}