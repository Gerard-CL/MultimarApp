package com.example.multimarapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimarapp.R
import com.example.multimarapp.network.PasoTrackingResponse
import java.text.SimpleDateFormat
import java.util.Locale

class TimelineAdapter(private var listaPasos: List<PasoTrackingResponse>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    class TimelineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lineTop: View = view.findViewById(R.id.lineTop)
        val lineBottom: View = view.findViewById(R.id.lineBottom)
        val dotIndicator: View = view.findViewById(R.id.dotIndicator)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val paso = listaPasos[position]

        holder.tvTitle.text = paso.nombrePaso

        // Formatear la fecha o mostrar "Pendiente"
        if (paso.fechaCompletado != null) {
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val formatter = SimpleDateFormat("dd/MM/yyyy | HH:mm a", Locale.getDefault())
                val date = parser.parse(paso.fechaCompletado)
                holder.tvDate.text = formatter.format(date!!)
            } catch (e: Exception) {
                holder.tvDate.text = paso.fechaCompletado
            }
        } else {
            holder.tvDate.text = "Pendiente"
        }

        // Visibilidad de los extremos (ocultar primera línea arriba y última abajo)
        holder.lineTop.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
        holder.lineBottom.visibility = if (position == itemCount - 1) View.INVISIBLE else View.VISIBLE

        // ==========================================
        // LÓGICA DE COLORES (AZUL vs GRIS)
        // ==========================================
        val colorAzul = Color.parseColor("#63B4ED") // Tu color azul claro
        val colorGris = Color.parseColor("#E0E0E0") // Gris clarito para pendientes

        val esCompletado = paso.fechaCompletado != null

        // Verificamos si el paso anterior está completado para colorear la línea que los conecta
        val pasoAnteriorCompletado = if (position > 0) {
            listaPasos[position - 1].estado.equals("Completado", ignoreCase = true)
        } else {
            true // El primero da igual porque su lineTop está oculta
        }

        // 1. Configurar el Punto y el Texto principal
        if (esCompletado) {
            holder.tvTitle.setTextColor(Color.parseColor("#111111")) // Texto oscuro
            holder.dotIndicator.setBackgroundResource(R.drawable.bg_dot_active) // Círculo azul
            holder.lineBottom.setBackgroundColor(colorAzul) // La línea que sale hacia abajo es azul
        } else {
            holder.tvTitle.setTextColor(Color.parseColor("#999999")) // Texto grisáceo
            holder.dotIndicator.setBackgroundResource(R.drawable.bg_dot_inactive) // Círculo gris
            holder.lineBottom.setBackgroundColor(colorGris) // La línea que sale hacia abajo es gris
        }

        // 2. Configurar la Línea Superior (LineTop)
        // La línea que llega a este punto es azul si el paso ANTERIOR ya se completó.
        if (pasoAnteriorCompletado) {
            holder.lineTop.setBackgroundColor(colorAzul)
        } else {
            holder.lineTop.setBackgroundColor(colorGris)
        }
    }

    override fun getItemCount() = listaPasos.size

    fun actualizarDatos(nuevaLista: List<PasoTrackingResponse>) {
        listaPasos = nuevaLista
        notifyDataSetChanged()
    }
}