package com.example.practicamagic.uiClientes.ajustesCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.databinding.FragmentInscripcionesClienteBinding
import com.example.practicamagic.entities.Evento
import com.example.practicamagic.entities.Inscripcion
import com.example.practicamagic.entities.Pedido
import com.example.practicamagic.uiClientes.pedidosCliente.PedidosClienteFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class InscripcionesClienteEventosFragment : Fragment() {

    // Declaraciones de propiedades
    lateinit var binding: FragmentInscripcionesClienteBinding
    lateinit var viewModel: InscripcionesClienteEventosViewModel
    lateinit var adapter: InscripcionEventoAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    lateinit var auth: FirebaseAuth
    private var inscripciones: ArrayList<Inscripcion> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInscripcionesClienteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[InscripcionesClienteEventosViewModel::class.java]

        initDatabase()
        initAdapters()
        loadEventos()

        return binding.root
    }

    companion object {
        fun newInstance() = InscripcionesClienteEventosFragment()
    }

    private fun initAdapters() {
        adapter = InscripcionEventoAdapter(requireContext(), db_ref, sto_ref, auth)
        binding.recyclerEventosCliente.adapter = adapter
    }

    private fun loadEventos() {
            db_ref.child("tienda").child("reservas_eventos").get().addOnSuccessListener {
                if (it.exists()) {
                    inscripciones.clear()
                    for (inscripcion in it.children) {
                        val newPedido = inscripcion.getValue(Inscripcion::class.java)
                        if (newPedido != null) {
                            inscripciones.add(newPedido)
                        }
                    }
                    val filteredList = inscripciones.filter { it.id_persona == auth.uid  }
                    adapter.submitList(filteredList)
                }
            }
        }


    private fun initDatabase() {
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }
}
