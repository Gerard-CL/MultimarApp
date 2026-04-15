package com.example.multimarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// PropuestaAdapter.kt
class PropuestaAdapter(private var lista: List<Propuesta>) :
    RecyclerView.Adapter<PropuestaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombrePropuesta)
        val tvFechaInicial: TextView = view.findViewById(R.id.tvFechaInicial)
        val tvCaducidad: TextView = view.findViewById(R.id.tvCaducidad)
        val tvTotal: TextView = view.findViewById(R.id.tvTotalValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propuesta = lista[position]
        holder.tvNombre.text = "Propuesta numero ${propuesta.id}"
        holder.tvFechaInicial.text = "Fecha Inicial: ${propuesta.dataValidessaInicial}"
        holder.tvCaducidad.text = "Fecha Caducidad: ${propuesta.dataValidessaFinal}"
        holder.tvTotal.text = "${propuesta.preu} €"
    }

    override fun getItemCount() = lista.size
}