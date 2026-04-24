package com.example.multimarapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.DetallesOferta
import com.example.multimarapp.R
import com.example.multimarapp.network.NotificacionResponse
import com.google.android.material.button.MaterialButton

class NotificacionesAdapter(private var notificaciones: List<NotificacionResponse>) :
    RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder>() {

    class NotificacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombrePropuesta: TextView = view.findViewById(R.id.tvNombrePropuesta)
        val tvFechaInicial: TextView = view.findViewById(R.id.tvFechaInicial)
        val tvCaducidad: TextView = view.findViewById(R.id.tvCaducidad)
        val tvTotalValue: TextView = view.findViewById(R.id.tvTotalValue)
        val btnVerDetalles: MaterialButton = view.findViewById(R.id.btnVerDetalles) // Listo por si le quieres dar click luego
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notificacion, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val notificacion = notificaciones[position]

        holder.tvNombrePropuesta.text = "Propuesta de ${notificacion.nombreOperador}"


        val fechaIniCorta = notificacion.fechaInicio.take(10)
        val fechaCadCorta = notificacion.fechaCaducidad.take(10)

        holder.tvFechaInicial.text = "$fechaIniCorta"
        holder.tvCaducidad.text = " Válido hasta: $fechaCadCorta"
        holder.tvTotalValue.text = "${notificacion.precio} €"

        holder.btnVerDetalles.setOnClickListener { view ->
            val intent = android.content.Intent(view.context, DetallesOferta::class.java)

            intent.putExtra("ID_OFERTA", notificacion.id)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount() = notificaciones.size

    fun actualizarDatos(nuevaLista: List<NotificacionResponse>) {
        notificaciones = nuevaLista
        notifyDataSetChanged()
    }
}