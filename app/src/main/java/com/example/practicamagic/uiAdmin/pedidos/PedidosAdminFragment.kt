package com.example.practicamagic.uiAdmin.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentPedidosAdminBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PedidosAdminFragment : Fragment() {

    lateinit var binding: FragmentPedidosAdminBinding
    lateinit var viewModel: PedidosAdminViewModel
    lateinit var adapter: OrdenPendienteAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var pedidos: ArrayList<Pedido> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPedidosAdminBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PedidosAdminViewModel::class.java]

        initDatabase()
        initAdapters()
        loadPedidos()

        return binding.root
    }

    private fun initDatabase() {
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
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

    private fun initAdapters() {
        adapter = OrdenPendienteAdapter(requireContext(), db_ref, sto_ref)
        binding.recyclerPedidos.adapter = adapter
    }

    companion object {
        fun newInstance() = PedidosAdminFragment()
    }
}