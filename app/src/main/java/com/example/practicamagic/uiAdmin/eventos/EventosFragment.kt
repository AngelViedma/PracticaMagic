package com.example.practicamagic.uiAdmin.eventos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentEventosAdminBinding
import com.example.practicamagic.eventos.CrearEvento
import com.example.practicamagic.entities.Evento
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EventosFragment : Fragment() {

    lateinit var binding: FragmentEventosAdminBinding
    lateinit var viewModel: EventosViewModel
    lateinit var adapter: EventoAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var eventos: ArrayList<Evento> = arrayListOf()
    private var launcherAddEvento= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == -1){
            loadEventos()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventosAdminBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[EventosViewModel::class.java]

        initDatabase()
        initObservers()
        initAdapter()
        initListeners()
        loadEventos()

        return binding.root
    }

    private fun loadEventos() {
        db_ref.child("tienda").child("eventos").get().addOnSuccessListener {
            if (it.exists()) {
                eventos.clear()
                for (evento in it.children) {
                    val evento = evento.getValue(Evento::class.java)
                    if (evento != null) {
                        eventos.add(evento)
                    }
                }
                adapter.submitList(eventos)
            }
        }
    }
    private fun initListeners() {
        binding.btAddEvento.setOnClickListener {
            launcherAddEvento.launch(Intent(requireContext(), CrearEvento::class.java))
        }
    }

    private fun initDatabase() {
        db_ref= FirebaseDatabase.getInstance().reference
        sto_ref= FirebaseStorage.getInstance().reference
    }

    private fun initAdapter() {
        adapter = EventoAdapter(requireContext(), db_ref, sto_ref)
        binding.recyclerEventosAdmin.adapter = adapter
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = EventosFragment()
    }
}