package com.example.multimarapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.DetalleEnviosActivity
import com.example.multimarapp.R
import com.example.multimarapp.network.PedidoActivoResponse

class PedidosAdapter(private var pedidos: List<PedidoActivoResponse>) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrigen: TextView = view.findViewById(R.id.tvOrigen)
        val tvDestino: TextView = view.findViewById(R.id.tvDestino)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_envio_detalle, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        holder.tvEstado.text = pedido.pasoActual
        holder.tvOrderId.text = "ID del Pedido: ${pedido.idPedido}"
        holder.tvOrigen.text = pedido.ciudadOrigen
        holder.tvDestino.text = pedido.ciudadDestino

        // ¡NUEVO!: Lógica para ir a la pantalla de detalles al tocar la tarjeta
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleEnviosActivity::class.java)
            // Pasamos el ID del pedido usando el nombre de la variable de tu DTO (idPedido)
            intent.putExtra("EXTRA_ID_PEDIDO", pedido.idPedido)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = pedidos.size

    fun actualizarDatos(nuevaLista: List<PedidoActivoResponse>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}