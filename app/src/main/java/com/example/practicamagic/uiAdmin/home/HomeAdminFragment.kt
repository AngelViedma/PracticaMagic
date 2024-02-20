package com.example.practicamagic.uiAdmin.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.entities.Carta
import com.example.practicamagic.databinding.FragmentHomeAdminBinding
import com.example.practicamagic.uiAdmin.home.addCarta.AddCartaActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeAdminFragment : Fragment() {

    //variables lateinit de vista y viewmodel
    lateinit var binding: FragmentHomeAdminBinding
    lateinit var viewModel: HomeAdminViewModel
    lateinit var adapter: CartasAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var cartas: ArrayList<Carta> = arrayListOf()
    private var launcherAddCarta= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == -1){
            loadCartas()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inicializacion de vista y viewmodel
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this)[HomeAdminViewModel::class.java]

        initDatabase()
        initObservers()
        initAdapter()
        initListeners()
        loadCartas()

        return binding.root
    }

    private fun loadCartas() {
        db_ref.child("tienda").child("cartas").get().addOnSuccessListener {
            if (it.exists()) {
                cartas.clear()
                for (carta in it.children) {
                    val carta = carta.getValue(Carta::class.java)
                    if (carta != null) {
                        cartas.add(carta)
                    }
                }
                adapter.submitList(cartas)
            }
        }
    }

    private fun initListeners() {
        binding.btAddCarta.setOnClickListener {
            launcherAddCarta.launch(Intent(requireContext(), AddCartaActivity::class.java))
        }
    }

    private fun initDatabase() {
        db_ref= FirebaseDatabase.getInstance().reference
        sto_ref=FirebaseStorage.getInstance().reference
    }

    private fun initAdapter() {
        adapter = CartasAdapter(requireContext(), db_ref, sto_ref)
        binding.recyclerCartas.adapter = adapter
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = HomeAdminFragment()
    }
}