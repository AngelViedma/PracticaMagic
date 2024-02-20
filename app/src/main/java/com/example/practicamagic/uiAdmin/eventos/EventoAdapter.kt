package com.example.practicamagic.uiAdmin.eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicamagic.databinding.DialogEventoBinding
import com.example.practicamagic.databinding.ItemEventoBinding
import com.example.practicamagic.entities.Evento
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class EventoAdapter(
    private  var contexto: Context,
    private  var db_ref: DatabaseReference,
    private  var sto_ref: StorageReference
) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    private var eventos: MutableList<Evento> = mutableListOf()

    class ViewHolder(val binding: ItemEventoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(evento: Evento, context: Context) {
            Glide.with(context)
                .load(evento.imagen)
                .into(binding.imgEvento)

            binding.tvNombreEvento.text = evento.nombre
            binding.tvFechaEvento.text = evento.fecha
            binding.tvPrecioEvento.text = evento.precio.toString()
            binding.tvAforoMaxEvento.text = evento.aforo_max.toString()
            binding.tvAforoOcupadoEvento.text = evento.aforo_ocupado.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemEventoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    fun submitList(items: List<Evento>) {
        eventos.clear()
        eventos.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = eventos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventos[position], contexto)

        holder.binding.btModificarEvento.setOnClickListener {
            showEventoaDetailsDialog(eventos[position])
        }
    }

    private fun showEventoaDetailsDialog(evento: Evento) {
        val dialogBinding = DialogEventoBinding.inflate(LayoutInflater.from(contexto))

        dialogBinding.editNombreEvento.setText(evento.nombre)
        dialogBinding.editFechaEvento.setText(evento.fecha)
        dialogBinding.editPrecioEvento.setText(evento.precio.toString())
        dialogBinding.editAforoMaximoEvento.setText(evento.aforo_max.toString())
        dialogBinding.editAforoOcupadoEvento.setText(evento.aforo_ocupado.toString())


        val builder = AlertDialog.Builder(contexto)
        builder.setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->
                val nombre = dialogBinding.editNombreEvento.text.toString()
                val fecha = dialogBinding.editFechaEvento.text.toString()
                val precio = dialogBinding.editPrecioEvento.text.toString().toDouble()
                val aforo_max = dialogBinding.editAforoMaximoEvento.text.toString().toInt()
                val aforo_ocupado = dialogBinding.editAforoOcupadoEvento.text.toString().toInt()



                val updatedEvento = Evento(evento.id, nombre, fecha, precio, aforo_max,aforo_ocupado,evento.imagen)
                db_ref.child("tienda").child("eventos").child(evento.id.toString()).setValue(updatedEvento)
                    .addOnSuccessListener {
                        val index = eventos.indexOf(evento)
                        if (index != -1) {
                            eventos[index] = updatedEvento
                            notifyItemChanged(index)
                        }
                    }
            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}