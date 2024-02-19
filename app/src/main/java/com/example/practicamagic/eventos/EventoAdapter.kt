package com.example.practicamagic.eventos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.R
import com.google.firebase.database.FirebaseDatabase

class EventoAdapter(private val lista_eventos:List<Evento>) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {
    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.item_evento,parent, false)
        //Para poder hacer referencia al contexto de la aplicacion
        contexto=parent.context

        return EventoViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val item_actual=lista_eventos[position]

        holder.nombre.text=item_actual.nombre
        holder.fecha.text=item_actual.precio
        holder.desapuntarse.visibility= View.INVISIBLE

        holder.ver_personas.setOnClickListener {
            val intent= Intent(contexto,PersonasEvento::class.java)
            intent.putExtra("evento",item_actual)
            contexto.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = lista_eventos.size

    inner class EventoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.item_nombre_evento)
        val fecha: TextView = itemView.findViewById(R.id.item_fecha_evento)
        val ver_personas: ImageView =itemView.findViewById(R.id.item_ver_personas)
        val desapuntarse: ImageView =itemView.findViewById(R.id.item_desapuntarme)
    }
}