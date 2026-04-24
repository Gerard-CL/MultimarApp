package com.example.multimarapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.R
import com.example.multimarapp.network.HistorialPedidoResponse

class HistorialAdapter(private var listaOriginal: List<HistorialPedidoResponse>) :
    RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    private var listaMostrada = listaOriginal.toList()

    class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutHeader: View = view.findViewById(R.id.layoutHeader)
        val layoutExpandable: LinearLayout = view.findViewById(R.id.layoutExpandable)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrigen: TextView = view.findViewById(R.id.tvOrigen)
        val tvDestino: TextView = view.findViewById(R.id.tvDestino)
        val ivArrow: ImageView = view.findViewById(R.id.ivArrow)

        val ivDownloadDoc1: ImageView = view.findViewById(R.id.ivDownloadDoc1)
        val ivDownloadDoc2: ImageView = view.findViewById(R.id.ivDownloadDoc2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val pedido = listaMostrada[position]

        holder.tvOrderId.text = "ID del pedido: ${pedido.idPedido}"
        holder.tvOrigen.text = pedido.ciudadOrigen
        holder.tvDestino.text = pedido.ciudadDestino

        holder.layoutHeader.setOnClickListener {
            val estaVisible = holder.layoutExpandable.visibility == View.VISIBLE

            if (estaVisible) {
                holder.layoutExpandable.visibility = View.GONE
                holder.ivArrow.rotation = 0f
            } else {
                holder.layoutExpandable.visibility = View.VISIBLE
                holder.ivArrow.rotation = 90f
            }
        }

        holder.ivDownloadDoc1.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Descargando Documento 1 del pedido ${pedido.idPedido}", Toast.LENGTH_SHORT).show()
        }

        holder.ivDownloadDoc2.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Descargando Documento 2 del pedido ${pedido.idPedido}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = listaMostrada.size

    fun actualizarDatos(nuevaLista: List<HistorialPedidoResponse>) {
        listaOriginal = nuevaLista
        listaMostrada = nuevaLista
        notifyDataSetChanged()
    }

    fun filtrar(texto: String) {
        listaMostrada = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.idPedido.toString().contains(texto, ignoreCase = true) ||
                        it.ciudadOrigen.contains(texto, ignoreCase = true) ||
                        it.ciudadDestino.contains(texto, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}