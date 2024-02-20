package com.example.practicamagic.uiClientes.pedidosCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.practicamagic.databinding.FragmentPedidosClienteBinding
import com.example.practicamagic.entities.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PedidosClienteFragment : Fragment() {

    lateinit var binding: FragmentPedidosClienteBinding
    lateinit var viewModel: PedidosClienteViewModel
    lateinit var adapter: ComprasAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var pedidos: ArrayList<Pedido> = arrayListOf()
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPedidosClienteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PedidosClienteViewModel::class.java]

        initDatabase()
        initAdapters()
        loadPedidos()

        return binding.root
    }

    companion object {
        fun newInstance() = PedidosClienteFragment()
    }

    private fun initAdapters() {
        adapter = ComprasAdapter(requireContext(), db_ref, sto_ref,auth)
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
                val filteredList = pedidos.filter { it.usuarioId == auth.uid  }
                adapter.submitList(filteredList)
            }
        }
    }

    private fun initDatabase() {
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}