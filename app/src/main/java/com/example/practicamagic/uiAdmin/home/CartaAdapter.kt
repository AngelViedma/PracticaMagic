package com.example.practicamagic.uiAdmin.home

import android.annotation.SuppressLint
import android.content.Context
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

    private var cartas: List<Carta> = listOf()

    class ViewHolder(val binding: ItemCartaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(carta: Carta, context: Context, db_ref: DatabaseReference, sto_ref: StorageReference) {
            Glide.with(context)
                .load(carta.imagen)
                .into(binding.imgCarta)

            binding.tvNombreCarta.text = carta.nombre
            binding.tvCategoriaCarta.text = carta.categoria
            binding.tvPrecioCarta.text = carta.precio.toString()
            binding.tvStockCarta.text=return when(carta.stock){
                false -> binding.tvStockCarta.text = "Agotado"
                else -> binding.tvStockCarta.text = "Disponible"
            }
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

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<Carta>){
        cartas = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun getItemCount() = cartas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartas[position], context, db_ref, sto_ref)
        holder.binding.btBorrarCarta.setOnClickListener {
            var success1=false
            var success2=false
            db_ref.child("tienda").child("cartas").child(cartas[position].id.toString()).removeValue().addOnSuccessListener {
                success1=true

            }
            sto_ref.child("tienda").child("cartas").child(cartas[position].id.toString()).delete().addOnSuccessListener {
                success2=true
            }
            if(success1 && success2){
                Toast.makeText(context, "Carta borrada con exito", Toast.LENGTH_SHORT).show()
                submitList(cartas)
            }
        }
        holder.binding.btModificarCarta.setOnClickListener {
            showCartaDetailsDialog(cartas[position]).show()
            submitList(cartas)
        }
    }

    fun showCartaDetailsDialog(carta: Carta): AlertDialog {
        val dialogBinding = DialogCartaBinding.inflate(LayoutInflater.from(context))

        dialogBinding.editNombreCarta.setText(carta.nombre)
        dialogBinding.editCategoriaCarta.setText(carta.categoria)
        dialogBinding.editPrecioCarta.setText(carta.precio.toString())
        dialogBinding.editStockCarta.setText(carta.stock.toString())

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, which ->
                val nombre = dialogBinding.editNombreCarta.text.toString()
                val categoria = dialogBinding.editCategoriaCarta.text.toString()
                val precio = dialogBinding.editPrecioCarta.text.toString().toDouble()
                val stock = dialogBinding.editStockCarta.text.toString().toBoolean()

                val carta = Carta(carta.id, nombre, categoria, precio, stock, carta.imagen)
                db_ref.child("tienda").child("cartas").child(carta.id.toString()).setValue(carta)
            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        return dialog
    }
}