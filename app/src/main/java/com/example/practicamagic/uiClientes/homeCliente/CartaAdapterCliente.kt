package com.example.practicamagic.uiClientes.homeCliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicamagic.Carta
import com.example.practicamagic.databinding.ItemCartaBinding
import com.example.practicamagic.databinding.ItemCartaClienteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class CartaAdapterCliente(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference
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
            binding.tvStockCarta.text = if (carta.stock=="1" || carta.stock=="Disponible") "Disponible" else "Agotado"
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

    fun submitList(items: List<Carta>) {
        cartas.clear()
        cartas.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = cartas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartas[position], context)

    }
}