package com.example.practicamagic.uiClientes.pedidosCliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.databinding.DialogAprobarPedidoBinding
import com.example.practicamagic.databinding.ItemPedidoAdminBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class ComprasAdapter(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference,
    auth: FirebaseAuth
) : RecyclerView.Adapter<ComprasAdapter.ViewHolder>() {

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
    }
}