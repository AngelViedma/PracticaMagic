package com.example.practicamagic.uiClientes.eventosCliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicamagic.databinding.DialogAprobarPedidoBinding
import com.example.practicamagic.databinding.DialogEventoBinding
import com.example.practicamagic.databinding.ItemEventoClienteBinding
import com.example.practicamagic.entities.Carta
import com.example.practicamagic.entities.Evento
import com.example.practicamagic.entities.Inscripcion
import com.example.practicamagic.entities.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class EventoAdapterCliente(
    private val contexto: Context,
    private val db_ref: DatabaseReference,
    private val auth: FirebaseAuth
) : RecyclerView.Adapter<EventoAdapterCliente.ViewHolder>() {

    private var eventos: MutableList<Evento> = mutableListOf()

    inner class ViewHolder(val binding: ItemEventoClienteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(evento: Evento) {
            Glide.with(contexto)
                .load(evento.imagen)
                .into(binding.imgEvento)

            binding.tvNombreEvento.text = evento.nombre
            binding.tvFechaEvento.text = evento.fecha
            binding.tvPrecioEvento.text = evento.precio.toString()
            binding.tvAforoMaxEvento.text = evento.aforo_max.toString()
            binding.tvAforoOcupadoEvento.text = evento.aforo_ocupado.toString()

            binding.btApuntarseEvento.setOnClickListener {
                showConfirmationDialog(evento)
            }
        }
    }

    private fun showConfirmationDialog(evento: Evento) {
        val dialogBinding = DialogAprobarPedidoBinding.inflate(LayoutInflater.from(contexto))

        if (evento.aforo_max != evento.aforo_ocupado) {
            dialogBinding.textAviso.text =
                "Te vas a unir al evento, desea continuar?"
            val builder = AlertDialog.Builder(contexto)
            builder.setView(dialogBinding.root)
                .setPositiveButton("Guardar") { dialog, which ->
                    val updatedEventos = Evento(
                        evento.id,
                        evento.nombre,
                        evento.fecha,
                        evento.precio,
                        evento.aforo_max,
                        evento.aforo_ocupado?.plus(1),
                        evento.imagen
                    )

                    val id_generado = db_ref.child("tienda").child("reservas_eventos").push().key

                    val newPedido = Pedido(id = id_generado, usuarioId = auth.currentUser?.uid, cartaId =evento.id)

                    db_ref.child("tienda").child("eventos").child(evento.id.toString()).setValue(updatedEventos)
                        .addOnSuccessListener {
                            val id_generado = db_ref.child("tienda").child("reservas_eventos").push().key
                            val newInscripcion = Inscripcion(id = id_generado, id_evento = evento.id, id_persona = auth.currentUser?.uid)

                            db_ref.child("tienda").child("reservas_eventos").child(newPedido.id.toString())
                                .setValue(newPedido)
                                .addOnSuccessListener {
                                    val index = eventos.indexOf(evento)
                                    if (index != -1) {
                                        eventos[index] = updatedEventos
                                        notifyItemChanged(index)
                                    }
                                }
                        }

                }.setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }

            builder.create().show()
        } else {
            dialogBinding.textAviso.text =
                "El evento está completo, no puedes unirte."
            val builder = AlertDialog.Builder(contexto)
            builder.setView(dialogBinding.root)
                .setPositiveButton("Aceptar") { dialog, which ->
                    dialog.dismiss()
                }
            builder.create().show()
        }
    }

    fun submitList(items: List<Evento>) {
        eventos.clear()
        eventos.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventoClienteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = eventos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventos[position])
    }
}