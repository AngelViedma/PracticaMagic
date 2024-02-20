package com.example.practicamagic.uiClientes.homeCliente

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicamagic.databinding.DialogAprobarPedidoBinding
import com.example.practicamagic.entities.Carta
import com.example.practicamagic.databinding.ItemCartaClienteBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class CartaAdapterCliente(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference,
    private val auth: FirebaseAuth,
) : RecyclerView.Adapter<CartaAdapterCliente.ViewHolder>() {

    private var cartas: MutableList<Carta> = mutableListOf()

    class ViewHolder(val binding: ItemCartaClienteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(carta: Carta, context: Context) {
            Glide.with(context)
                .load(carta.imagen)
                .into(binding.imgCarta)

            binding.tvNombreCarta.text = carta.nombre
            binding.tvCategoriaCarta.text = carta.categoria
            binding.tvPrecioCarta.text = carta.precio.toString()
            binding.tvStockCarta.text =
                if (carta.stock == "1" || carta.stock == "Disponible") "Disponible" else "Agotado"

            carta.stock.takeIf { it == "Agotado" }?.let {
                binding.btComprarCartaCliente.isEnabled = false
                binding.container.setBackgroundColor(Color.LTGRAY)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemCartaClienteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    private fun showConfirmationDialog(carta: Carta) {
        val dialogBinding = DialogAprobarPedidoBinding.inflate(LayoutInflater.from(context))

        dialogBinding.textAviso.text =
            "Se va a confirmar el pedido, desea continuar? \nSe realizará el cobro tras la confirmación."
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->
                val updatedCarta = Carta(
                    carta.id,
                    carta.nombre,
                    carta.categoria,
                    carta.precio,
                    "Agotado",
                    carta.imagen
                )

                val id_generado = db_ref.child("tienda").child("reservas_carta").push().key

                val newPedido = Pedido(id = id_generado, usuarioId = auth.currentUser?.uid, cartaId =carta.id)

                db_ref.child("tienda").child("cartas").child(carta.id.toString()).setValue(updatedCarta)
                    .addOnSuccessListener {
                        val id_generado = db_ref.child("tienda").child("reservas_carta").push().key
                        val newPedido = Pedido(id = id_generado, usuarioId = auth.currentUser?.uid, cartaId = carta.id)

                        db_ref.child("tienda").child("reservas_carta").child(newPedido.id.toString())
                            .setValue(newPedido)
                            .addOnSuccessListener {
                                val index = cartas.indexOf(carta)
                                if (index != -1) {
                                    cartas[index] = updatedCarta
                                    notifyItemChanged(index)
                                }
                            }
                    }

            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    fun submitList(items: List<Carta>) {
        cartas.clear()
        cartas.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = cartas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartas[position], context)

        holder.binding.btComprarCartaCliente.setOnClickListener {
            showConfirmationDialog(cartas[position])
        }
    }
}
