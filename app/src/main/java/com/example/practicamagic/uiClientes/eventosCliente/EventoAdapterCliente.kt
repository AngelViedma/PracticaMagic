package com.example.practicamagic.uiClientes.eventosCliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
                val userId = auth.currentUser?.uid
                val eventId = evento.id // Supongamos que evento es el objeto Evento actual

                if (userId != null) {
                    db_ref.child("tienda").child("reservas_eventos")
                        .orderByChild("id_evento")
                        .equalTo(eventId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var userAlreadyRegistered = false
                                for (reservaSnapshot in snapshot.children) {
                                    val idUsuario = reservaSnapshot.child("id_persona").getValue(String::class.java)
                                    if (idUsuario == userId) {
                                        userAlreadyRegistered = true
                                        break
                                    }
                                }

                                if (userAlreadyRegistered) {
                                    Toast.makeText(contexto, "Ya estás registrado en este evento", Toast.LENGTH_SHORT).show()
                                } else {
                                    // El usuario no está registrado en este evento, muestra el diálogo de confirmación
                                    binding.btApuntarseEvento.setOnClickListener {
                                        showConfirmationDialog(evento)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Manejar el error en la consulta
                            }
                        })
                }
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

                            db_ref.child("tienda").child("reservas_eventos").child(newInscripcion.id.toString())
                                .setValue(newInscripcion)
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