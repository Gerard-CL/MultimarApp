package com.example.multimarapp

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

// 1. Cambiamos 'val' por 'var' para poder actualizar la lista cuando responda la API
class EnvioAdapter(private var listaEnvios: List<Envio>) : RecyclerView.Adapter<EnvioAdapter.EnvioViewHolder>() {

    class EnvioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvOrigen: TextView = itemView.findViewById(R.id.tvOrigen)
        val tvDest: TextView = itemView.findViewById(R.id.tvDestino)
        val tvStatus: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvioViewHolder {
        // Asegúrate de que tu XML realmente se llama 'item_envio_detalle' en la carpeta res/layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_envio_detalle, parent, false)
        return EnvioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnvioViewHolder, position: Int) {
        val envio = listaEnvios[position]

        // 2. Corregido el mapeo de datos exacto según tu data class 'Envio'
        holder.tvOrderId.text = "ID del Pedido: ${envio.orderId}"
        holder.tvOrigen.text = envio.origen
        holder.tvDest.text = envio.destino
        holder.tvStatus.text = envio.estado

        // 3. Lógica corregida para cambiar los colores del TextView (BackgroundTint y TextColor)
        val context = holder.itemView.context
        when (envio.estado) {
            "Enviado" -> {
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_shipped_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_shipped_text))
            }
            "En camino" -> {
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_transit_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_transit_text))
            }
            "Entregado" -> {
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_delivered_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_delivered_text))
            }
            else -> {
                // Siempre es bueno poner un color por defecto por si la API manda un estado raro
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_transit_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_transit_text))
            }
        }
    }

    override fun getItemCount(): Int {
        return listaEnvios.size
    }

    // 4. Función esencial para inyectar los datos de la API cuando lleguen
    fun actualizarLista(nuevaLista: List<Envio>) {
        this.listaEnvios = nuevaLista
        notifyDataSetChanged()
    }
}