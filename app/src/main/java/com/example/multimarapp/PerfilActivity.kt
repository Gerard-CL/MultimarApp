package com.example.multimarapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multimarapp.network.PerfilClienteDTO
import com.example.multimarapp.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PerfilActivity : AppCompatActivity() {

    // --- Variables de UI ---
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellidos: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var layoutNombre: TextInputLayout
    private lateinit var layoutApellidos: TextInputLayout
    private lateinit var layoutCorreo: TextInputLayout
    private lateinit var layoutTelefono: TextInputLayout

    private lateinit var tvIdiomaSelector: TextView
    private lateinit var btnEditPersonalData: MaterialButton
    private lateinit var btnSavePersonalData: MaterialButton
    private lateinit var btnEditIdioma: ImageView
    private lateinit var btnSaveDniData: MaterialButton
    private lateinit var btnReplaceDni: MaterialButton

    // --- Variables de Datos y Sockets ---
    private var isEditingMode = false
    private var idiomaSeleccionado = "Español"
    private var idUsuarioActual = 5 // TODO: Cambiar por el ID real del usuario logueado
    private val SERVER_IP = "192.168.1.X" // TODO: Pon la IP de tu PC
    private val SERVER_PORT = 11000

    // Selector de archivos para subir el DNI
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            subirDniPorSocket(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        inicializarVistas()
        configurarBotones()
        cargarDatosUsuario()
        configurarNavegacionInferior()
    }

    private fun inicializarVistas() {
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)

        layoutNombre = findViewById(R.id.layoutNombre)
        layoutApellidos = findViewById(R.id.layoutApellidos)
        layoutCorreo = findViewById(R.id.layoutCorreo)
        layoutTelefono = findViewById(R.id.layoutTelefono)

        tvIdiomaSelector = findViewById(R.id.tvIdiomaSelector)
        btnEditPersonalData = findViewById(R.id.btnEditPersonalData)
        btnSavePersonalData = findViewById(R.id.btnSavePersonalData)
        btnEditIdioma = findViewById(R.id.btnEditIdioma)

        btnSaveDniData = findViewById(R.id.btnSaveDniData)
        btnReplaceDni = findViewById(R.id.btnReplaceDni)

        // Estado inicial
        btnSavePersonalData.visibility = View.GONE
    }

    private fun configurarBotones() {
        // --- SECCIÓN: DATOS PERSONALES ---
        btnEditPersonalData.setOnClickListener {
            isEditingMode = !isEditingMode
            cambiarModoEdicion(isEditingMode)
        }

        btnSavePersonalData.setOnClickListener {
            guardarDatosPersonales()
        }

        // --- SECCIÓN: PREFERENCIAS (IDIOMA) ---
        btnEditIdioma.setOnClickListener {
            // Cambiamos el fondo a blanco para dar el efecto de desbloqueado
            tvIdiomaSelector.setBackgroundColor(Color.WHITE)
            mostrarMenuIdiomas()
        }

        // --- SECCIÓN: DNI (SOCKETS) ---
        btnSaveDniData.setOnClickListener {
            // Abrir galería o explorador para seleccionar imagen
            pickImageLauncher.launch("image/*")
        }

        btnReplaceDni.setOnClickListener {
            descargarYVerDni()
        }
    }

    // ==========================================
    // LÓGICA VISUAL Y HTTP (DATOS Y PREFERENCIAS)
    // ==========================================

    private fun cambiarModoEdicion(activar: Boolean) {
        val colorFondo = if (activar) Color.parseColor("#FFFFFF") else Color.parseColor("#F2F2F2")

        val campos = listOf(etNombre, etApellidos, etCorreo, etTelefono)
        val layouts = listOf(layoutNombre, layoutApellidos, layoutCorreo, layoutTelefono)

        for (i in campos.indices) {
            campos[i].isEnabled = activar
            campos[i].isFocusable = activar
            campos[i].isFocusableInTouchMode = activar
            layouts[i].boxBackgroundColor = colorFondo
        }

        btnSavePersonalData.visibility = if (activar) View.VISIBLE else View.GONE
        btnEditPersonalData.text = if (activar) "Cancelar" else "Editar"
    }

    private fun mostrarMenuIdiomas() {
        val popupMenu = PopupMenu(this, tvIdiomaSelector)
        popupMenu.menu.add("Español")
        popupMenu.menu.add("Català")
        popupMenu.menu.add("English")

        popupMenu.setOnMenuItemClickListener { item ->
            idiomaSeleccionado = item.title.toString()
            tvIdiomaSelector.text = idiomaSeleccionado
            // Volvemos a poner el fondo gris de bloqueado
            tvIdiomaSelector.setBackgroundResource(R.drawable.bg_pill_selector)

            // Opcional: Guardar automáticamente al cambiar
            guardarDatosPersonales()
            true
        }
        popupMenu.show()
    }

    private fun cargarDatosUsuario() {
        val api = RetrofitClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = api.getPerfilCliente()

                if (response.isSuccessful && response.body() != null) {
                    val perfil = response.body()!!

                    // 1. Rellenar campos de texto (ya lo tenías)
                    etNombre.setText(perfil.nombre)
                    etApellidos.setText(perfil.apellidos)
                    etCorreo.setText(perfil.correo)
                    etTelefono.setText(perfil.telefono)

                    idiomaSeleccionado = if (perfil.idioma.isNotEmpty()) perfil.idioma else "Español"
                    tvIdiomaSelector.text = idiomaSeleccionado

                    // 2. NUEVO: Lógica del DNI
                    val ivEstadoDni = findViewById<ImageView>(R.id.ivEstadoDni)
                    val tvEstadoDni = findViewById<TextView>(R.id.tvEstadoDni)

                    if (perfil.dniSubido) {
                        ivEstadoDni.setImageResource(R.drawable.ic_check_verde)
                        tvEstadoDni.text = "DNI VERIFICADO"
                        tvEstadoDni.setTextColor(android.graphics.Color.parseColor("#4CAF50")) // Verde
                    } else {
                        ivEstadoDni.setImageResource(R.drawable.ic_cruz_roja)
                        tvEstadoDni.text = "DNI NO SUBIDO"
                        tvEstadoDni.setTextColor(android.graphics.Color.parseColor("#D32F2F")) // Rojo
                    }

                } else {
                    val error = response.code()
                    Toast.makeText(this@PerfilActivity, "Error al cargar datos: $error", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun guardarDatosPersonales() {
        val api = RetrofitClient.getApiService(this)
        val datos = PerfilClienteDTO(
            nombre = etNombre.text.toString(),
            apellidos = etApellidos.text.toString(),
            correo = etCorreo.text.toString(),
            telefono = etTelefono.text.toString(),
            idioma = idiomaSeleccionado,
            dniSubido = false
        )

        lifecycleScope.launch {
            try {
                val response = api.actualizarPerfil(datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@PerfilActivity, "Datos guardados", Toast.LENGTH_SHORT).show()
                    cambiarModoEdicion(false)
                    isEditingMode = false
                } else {
                    // AQUÍ ESTÁ EL TRUCO: Le pedimos el código de error exacto
                    val codigoError = response.code()
                    val cuerpoError = response.errorBody()?.string()
                    Toast.makeText(this@PerfilActivity, "Error $codigoError: $cuerpoError", Toast.LENGTH_LONG).show()
                    println("ERROR API: Código $codigoError - Detalles: $cuerpoError")
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Fallo la conexión: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    // ==========================================
    // LÓGICA DE SOCKETS (DNI)
    // ==========================================

    private fun subirDniPorSocket(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 1. Convertir la imagen a un array de bytes
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val bytesArchivo = inputStream?.readBytes() ?: return@launch
                inputStream.close()

                // 2. Conectar al Socket de C#
                val socket = Socket(SERVER_IP, SERVER_PORT)
                val outputStream = socket.getOutputStream()

                // 3. Enviar el comando inicial como String (con salto de línea para StreamReader.ReadLine())
                val comando = "UPLOAD|$idUsuarioActual\r\n"
                outputStream.write(comando.toByteArray(Charsets.UTF_8))

                // 4. Enviar el tamaño del archivo.
                // MUY IMPORTANTE: C# (BinaryReader) lee los enteros en Little Endian. Java/Kotlin los envía en Big Endian por defecto.
                val sizeBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(bytesArchivo.size).array()
                outputStream.write(sizeBuffer)

                // 5. Enviar el archivo
                outputStream.write(bytesArchivo)
                outputStream.flush()
                socket.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "DNI subido correctamente", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "Error Sockets: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun descargarYVerDni() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val socket = Socket(SERVER_IP, SERVER_PORT)
                val outputStream = socket.getOutputStream()
                val inputStream = socket.getInputStream()

                // 1. Enviar el comando de descarga
                val comando = "DOWNLOAD|$idUsuarioActual\r\n"
                outputStream.write(comando.toByteArray(Charsets.UTF_8))
                outputStream.flush()

                // 2. Leer el tamaño del archivo (4 bytes, Little Endian)
                val sizeBytes = ByteArray(4)
                var bytesLeidos = inputStream.read(sizeBytes)
                if (bytesLeidos < 4) return@launch // Error de lectura

                val tamanoArchivo = ByteBuffer.wrap(sizeBytes).order(ByteOrder.LITTLE_ENDIAN).int

                // 3. Leer el archivo completo
                val bufferArchivo = ByteArray(tamanoArchivo)
                var totalLeido = 0
                while (totalLeido < tamanoArchivo) {
                    val leido = inputStream.read(bufferArchivo, totalLeido, tamanoArchivo - totalLeido)
                    if (leido == -1) break
                    totalLeido += leido
                }
                socket.close()

                // 4. Convertir los bytes a una Imagen (Bitmap) y mostrarla en pantalla
                val bitmap = BitmapFactory.decodeByteArray(bufferArchivo, 0, bufferArchivo.size)

                withContext(Dispatchers.Main) {
                    val imageView = ImageView(this@PerfilActivity)
                    imageView.setImageBitmap(bitmap)

                    AlertDialog.Builder(this@PerfilActivity)
                        .setTitle("DNI Descargado y Desencriptado")
                        .setView(imageView)
                        .setPositiveButton("Cerrar", null)
                        .show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilActivity, "Error al descargar DNI: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun configurarNavegacionInferior() {
        // Asegúrate de que "navInicio" y "navEnvios" son los IDs exactos de tu XML
        val navInicio = findViewById<View>(R.id.navInicio)
        val navEnvios = findViewById<View>(R.id.navEnvios)

        // Lógica para el botón Inicio
        navInicio.setOnClickListener {
            val intent = Intent(this@PerfilActivity, InicioActivity::class.java)
            // Banderas para no acumular pantallas
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Cerramos PerfilActivity
        }

        // Lógica para el botón Envíos
        navEnvios.setOnClickListener {
            val intent = Intent(this@PerfilActivity, EnviosActivity::class.java)
            // Banderas para no acumular pantallas
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Cerramos PerfilActivity
        }
    }
}