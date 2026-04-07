package com.example.multimarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class EnvioAdapter(private val listaEnvios: List<Envio>) : RecyclerView.Adapter<EnvioAdapter.EnvioViewHolder>() {

    class EnvioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvClient: TextView = itemView.findViewById(R.id.tvClient)
        val tvDest: TextView = itemView.findViewById(R.id.tvDest)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val cvStatus: MaterialCardView = itemView.findViewById(R.id.cvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_envio, parent, false)
        return EnvioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnvioViewHolder, position: Int) {
        val envio = listaEnvios[position]

        holder.tvOrderId.text = "Order ID: ${envio.orderId}"
        holder.tvClient.text = "Cliente: ${envio.cliente}"
        holder.tvDest.text = "Destino: ${envio.destino}"
        holder.tvStatus.text = envio.estado

        // Lógica para cambiar los colores del "badge" según el estado
        val context = holder.itemView.context
        when (envio.estado) {
            "Enviado" -> {
                holder.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_shipped_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_shipped_text))
            }
            "En camino" -> {
                holder.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_transit_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_transit_text))
            }
            "Entregado" -> {
                holder.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_delivered_bg))
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_delivered_text))
            }
        }
    }

    override fun getItemCount(): Int {
        return listaEnvios.size
    }
}