package com.example.multimarapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.DetalleEnviosActivity
import com.example.multimarapp.R
import com.example.multimarapp.network.EnvioResponse

class EnviosAdapter(private var envios: List<EnvioResponse>) : RecyclerView.Adapter<EnviosAdapter.EnvioViewHolder>() {

    // 1. Enlazamos los IDs
    class EnvioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrigen: TextView = view.findViewById(R.id.tvOrigen)
        val tvDestino: TextView = view.findViewById(R.id.tvDestino)
    }

    // 2. Le decimos qué diseño XML usar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_envio_detalle, parent, false)
        return EnvioViewHolder(view)
    }

    // 3. Rellenamos los datos de cada envío en su vista correspondiente
    override fun onBindViewHolder(holder: EnvioViewHolder, position: Int) {
        val envio = envios[position]
        holder.tvEstado.text = envio.pasoActual
        holder.tvOrderId.text = "ID del Pedido: ${envio.id}"
        holder.tvOrigen.text = envio.ciudadOrigen
        holder.tvDestino.text = envio.ciudadDestino

        // ¡NUEVO!: Al hacer clic en cualquier parte de la tarjeta del pedido
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleEnviosActivity::class.java)
            // Pasamos el ID exacto del pedido (envio.id) a la siguiente pantalla
            intent.putExtra("EXTRA_ID_PEDIDO", envio.id)
            context.startActivity(intent)
        }
    }

    // 4. Le decimos cuántos elementos hay en total
    override fun getItemCount() = envios.size

    fun actualizarDatos(nuevaLista: List<EnvioResponse>) {
        envios = nuevaLista
        notifyDataSetChanged()
    }
}