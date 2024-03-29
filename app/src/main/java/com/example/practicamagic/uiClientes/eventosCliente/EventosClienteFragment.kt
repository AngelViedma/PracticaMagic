package com.example.practicamagic.uiClientes.eventosCliente

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.databinding.FragmentEventosAdminBinding
import com.example.practicamagic.databinding.FragmentEventosClienteBinding
import com.example.practicamagic.entities.Evento
import com.example.practicamagic.entities.Inscripcion
import com.example.practicamagic.eventos.CrearEvento
import com.example.practicamagic.uiAdmin.eventos.EventoAdapter
import com.example.practicamagic.uiAdmin.eventos.EventosFragment
import com.example.practicamagic.uiAdmin.eventos.EventosViewModel
import com.example.practicamagic.uiClientes.ajustesCliente.InscripcionEventoAdapter
import com.example.practicamagic.uiClientes.pedidosCliente.PedidosClienteFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EventosClienteFragment : Fragment() {

    lateinit var binding: FragmentEventosClienteBinding
    lateinit var viewModel: EventosClienteViewModel
    lateinit var adapter: EventoAdapterCliente
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    private var eventos: ArrayList<Evento> = arrayListOf()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventosClienteBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[EventosClienteViewModel::class.java]

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

    }

    private fun initDatabase() {
        db_ref= FirebaseDatabase.getInstance().reference
        sto_ref= FirebaseStorage.getInstance().reference
        auth= FirebaseAuth.getInstance()
    }

    private fun initAdapter() {
        adapter = EventoAdapterCliente(requireContext(), db_ref, auth)
        binding.recyclerEventosCliente.adapter = adapter
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = EventosFragment()
    }
}