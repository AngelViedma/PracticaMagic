package com.example.practicamagic.uiClientes.homeCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practicamagic.entities.Carta
import com.example.practicamagic.databinding.FragmentHomeClienteBinding
import com.example.practicamagic.uiAdmin.home.HomeAdminFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeClienteFragment : Fragment() {


    lateinit var binding: FragmentHomeClienteBinding
    lateinit var viewModel: HomeClienteViewModel
    lateinit var adapter: CartaAdapterCliente
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private lateinit var auth: FirebaseAuth
    private var cartas: ArrayList<Carta> = arrayListOf()
    private var launcherAddCarta =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == -1) {
                loadCartas()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeClienteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeClienteViewModel::class.java]

        initDatabase()
        initObservers()
        initAdapter()
        loadCartas()

        return binding.root
    }

    private fun loadCartas() {
        var filteredList: ArrayList<Carta> = arrayListOf()
        db_ref.child("tienda").child("cartas").get().addOnSuccessListener {
            if (it.exists()) {
                cartas.clear()
                for (carta in it.children) {
                    val carta = carta.getValue(Carta::class.java)
                    if (carta != null) {
                        filteredList.add(carta)
                    }
                }
                cartas.addAll(filteredList.filter { it.stock == "Disponible" || it.stock == "1" })
                adapter.submitList(cartas)
            }
        }
    }

    private fun initDatabase() {
        auth = FirebaseAuth.getInstance()
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
    }

    private fun initAdapter() {
        adapter = CartaAdapterCliente(requireContext(), db_ref, sto_ref,auth)
        binding.recyclerCartasCliente.adapter = adapter
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }

    companion object {
        fun newInstance() = HomeAdminFragment()
    }
}