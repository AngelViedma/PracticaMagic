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

class AdaptadorPersona(private val lista_personas:List<Persona>) : RecyclerView.Adapter<AdaptadorPersona.PersonaViewHolder>() {
    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.item_persona,parent, false)
        //Para poder hacer referencia al contexto de la aplicacion
        contexto=parent.context

        return PersonaViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val item_actual=lista_personas[position]

        holder.nombre.text=item_actual.nombre
        holder.dni.text=item_actual.dni

        if(item_actual.id_inscripcion=="") {
            holder.echar_evento.visibility = View.INVISIBLE
            holder.ver_eventos.visibility = View.VISIBLE
        }else{
            holder.echar_evento.visibility = View.VISIBLE
            holder.ver_eventos.visibility = View.INVISIBLE
        }
        holder.echar_evento.setOnClickListener {
            val db_ref= FirebaseDatabase.getInstance().getReference()
            db_ref.child("aplicacion").child("inscripciones").child(item_actual.id_inscripcion!!).removeValue()
        }

        holder.ver_eventos.setOnClickListener{
            val intent= Intent(contexto,EventosPersona::class.java)
            intent.putExtra("persona",item_actual)
            contexto.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista_personas.size

    inner class PersonaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.item_nombre_persona)
        val dni: TextView = itemView.findViewById(R.id.item_persona_dni)
        val ver_eventos: ImageView = itemView.findViewById(R.id.item_ver_eventos)
        val echar_evento: ImageView =itemView.findViewById(R.id.item_echar_evento)
    }
}