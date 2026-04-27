package com.example.multimarapp // Asegúrate de que coincida con tu paquete

import android.content.Context
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object AESUtils {
    private const val PREFS_NAME = "AppPreferenciasDNI"
    private const val KEY_ALIAS = "ClaveSecretaAES"

    // Obtiene la clave existente o genera una nueva si no existe
    private fun getOrCreateKey(context: Context): SecretKey {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val keyString = prefs.getString(KEY_ALIAS, null)

        return if (keyString != null) {
            // Si ya existe, la decodificamos y la usamos
            val decodedKey = Base64.decode(keyString, Base64.DEFAULT)
            SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        } else {
            // Si no existe, creamos una clave AES de 256 bits y la guardamos
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            val secretKey = keyGen.generateKey()

            val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
            prefs.edit().putString(KEY_ALIAS, encodedKey).apply()

            secretKey
        }
    }

    fun encriptar(context: Context, datosLimpios: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey(context))
        return cipher.doFinal(datosLimpios)
    }

    fun desencriptar(context: Context, datosEncriptados: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(context))
        return cipher.doFinal(datosEncriptados)
    }
}