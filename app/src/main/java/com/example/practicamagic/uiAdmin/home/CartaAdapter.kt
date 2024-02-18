package com.example.practicamagic.uiAdmin.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicamagic.Carta
import com.example.practicamagic.databinding.DialogCartaBinding
import com.example.practicamagic.databinding.ItemCartaBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class CartaAdapter(
    private val context: Context,
    private val db_ref: DatabaseReference,
    private val sto_ref: StorageReference
) : RecyclerView.Adapter<CartaAdapter.ViewHolder>() {

    private var cartas: MutableList<Carta> = mutableListOf()

    class ViewHolder(val binding: ItemCartaBinding) : RecyclerView.ViewHolder(binding.root) {

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
                val binding = ItemCartaBinding.inflate(
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

        holder.binding.btBorrarCarta.setOnClickListener {
            val carta = cartas[position]
            db_ref.child("tienda").child("cartas").child(carta.id.toString()).removeValue()
                .addOnSuccessListener {
                    sto_ref.child("tienda").child("cartas").child(carta.id.toString()).delete()
                        .addOnSuccessListener {
                            cartas.removeAt(position)
                            notifyItemRemoved(position)
                            Toast.makeText(context, "Carta eliminada", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        holder.binding.btModificarCarta.setOnClickListener {
            showCartaDetailsDialog(cartas[position])
        }
    }

    private fun showCartaDetailsDialog(carta: Carta) {
        val dialogBinding = DialogCartaBinding.inflate(LayoutInflater.from(context))

        dialogBinding.editNombreCarta.setText(carta.nombre)
        dialogBinding.editCategoriaCarta.setText(carta.categoria)
        dialogBinding.editPrecioCarta.setText(carta.precio.toString())
        dialogBinding.editStockCarta.setText(when (carta.stock.toString()){
            "Disponible" -> "Disponible"
            "Agotado" -> "Agotado"
            "0" -> "Agotado"
            "1" -> "Disponible"
            else -> "Agotado"

        })

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->
                val nombre = dialogBinding.editNombreCarta.text.toString()
                val categoria = dialogBinding.editCategoriaCarta.text.toString()
                val precio = dialogBinding.editPrecioCarta.text.toString().toDouble()
                val stock = when (dialogBinding.editStockCarta.text.toString()){
                    "Disponible" -> "Disponible"
                    "Agotado" -> "Agotado"
                    "0" -> "Agotado"
                    "1" -> "Disponible"
                    else -> "Agotado"

                }

                val updatedCarta = Carta(carta.id, nombre, categoria, precio, stock, carta.imagen)
                db_ref.child("tienda").child("cartas").child(carta.id.toString()).setValue(updatedCarta)
                    .addOnSuccessListener {
                        val index = cartas.indexOf(carta)
                        if (index != -1) {
                            cartas[index] = updatedCarta
                            notifyItemChanged(index)
                        }
                    }
            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}