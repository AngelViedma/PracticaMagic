package com.example.practicamagic.uiAdmin.eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.databinding.ItemEventoApuntadoClienteBinding
import com.example.practicamagic.entities.Inscripcion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class VerTodasInscripcionesAdminAdapter(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference,
    auth: FirebaseAuth
) : RecyclerView.Adapter<VerTodasInscripcionesAdminAdapter.ViewHolder>() {

    private var inscripcion: MutableList<Inscripcion> = mutableListOf()

    class ViewHolder(val binding: ItemEventoApuntadoClienteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inscripcion: Inscripcion, context: Context) {
            binding.tvIdInscripcion.text = inscripcion.id
            binding.tvNumeroIdEvento.text = inscripcion.id_evento
            binding.tvNumeroIdCliente.text = inscripcion.id_persona
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemEventoApuntadoClienteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    fun submitList(items: List<Inscripcion>) {
        inscripcion.clear()
        inscripcion.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = inscripcion.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(inscripcion[position], context)
    }
}