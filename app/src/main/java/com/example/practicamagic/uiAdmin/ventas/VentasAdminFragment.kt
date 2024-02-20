package com.example.practicamagic.uiAdmin.ventas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.databinding.FragmentVentasAdminBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VentasAdminFragment : Fragment() {

    //variables lateinit de vista y viewmodel
    lateinit var binding: FragmentVentasAdminBinding
    lateinit var viewModel: VentasAdminViewModel
    lateinit var adapter: VentasAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var pedidos: ArrayList<Pedido> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVentasAdminBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this)[VentasAdminViewModel::class.java]

        initDatabase()
        initAdapters()
        loadPedidos()

        return binding.root
    }

    private fun initAdapters() {
        adapter = VentasAdapter(requireContext(), db_ref, sto_ref)
        binding.recyclerVentas.adapter = adapter
    }

    private fun loadPedidos() {
        db_ref.child("tienda").child("reservas_carta").get().addOnSuccessListener {
            if (it.exists()) {
                pedidos.clear()
                for (pedido in it.children) {
                    val newPedido = pedido.getValue(Pedido::class.java)
                    if (newPedido != null) {
                        pedidos.add(newPedido)
                    }
                }
                adapter.submitList(pedidos)
            }
        }
    }

    private fun initDatabase() {
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
    }

    companion object {
        fun newInstance() = VentasAdminFragment()
    }

}