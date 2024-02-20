package com.example.practicamagic.uiAdmin.ventas

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.databinding.DialogAprobarPedidoBinding
import com.example.practicamagic.databinding.ItemPedidoAdminBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class VentasAdapter(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference
) : RecyclerView.Adapter<VentasAdapter.ViewHolder>() {

    private var pedidos: MutableList<Pedido> = mutableListOf()

    class ViewHolder(val binding: ItemPedidoAdminBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: Pedido, context: Context) {
            binding.tvNumeroIdPedido.text = pedido.id
            binding.tvNumeroIdCarta.text = pedido.cartaId
            binding.tvNumeroIdCliente.text = pedido.usuarioId
            binding.btAprobarPedido.isVisible = false
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemPedidoAdminBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    fun submitList(items: List<Pedido>) {
        pedidos.clear()
        pedidos.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = pedidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pedidos[position], context)

        holder.binding.btAprobarPedido.setOnClickListener {
            showConfirmationDialog(pedidos[position])
        }
    }

    private fun showConfirmationDialog(pedido: Pedido) {
        val dialogBinding = DialogAprobarPedidoBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->

                val updatePedido = Pedido(pedido.id, pedido.usuarioId, pedido.cartaId, "Preparado")
                db_ref.child("tienda").child("reservas_carta").child(pedido.id.toString())
                    .setValue(updatePedido)
                    .addOnSuccessListener {
                        val index = pedidos.indexOf(updatePedido)
                        if (index != -1) {
                            pedidos[index] = updatePedido
                            notifyItemChanged(index)
                        }
                    }
            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}