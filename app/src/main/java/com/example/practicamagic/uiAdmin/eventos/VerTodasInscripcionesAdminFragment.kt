package com.example.practicamagic.uiAdmin.eventos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.R
import com.example.practicamagic.databinding.FragmentInscripcionesClienteBinding
import com.example.practicamagic.databinding.FragmentVerTodasInscripcionesAdminBinding
import com.example.practicamagic.entities.Evento
import com.example.practicamagic.entities.Inscripcion
import com.example.practicamagic.uiClientes.ajustesCliente.InscripcionEventoAdapter
import com.example.practicamagic.uiClientes.ajustesCliente.InscripcionesClienteEventosFragment
import com.example.practicamagic.uiClientes.ajustesCliente.InscripcionesClienteEventosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VerTodasInscripcionesAdminFragment : Fragment() {

    lateinit var binding: FragmentVerTodasInscripcionesAdminBinding
    lateinit var viewModel: VerTodasInscripcionesAdminViewModel
    lateinit var adapter: InscripcionEventoAdapter
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    lateinit var auth: FirebaseAuth
    private var inscripciones: ArrayList<Inscripcion> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerTodasInscripcionesAdminBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[VerTodasInscripcionesAdminViewModel::class.java]

        initDatabase()
        initAdapters()
        loadEventos()

        return binding.root
    }

    companion object {
        fun newInstance() = VerTodasInscripcionesAdminFragment()
    }

    private fun initAdapters() {
        adapter = InscripcionEventoAdapter(requireContext(), db_ref, sto_ref, auth)
        binding.recyclerVerInscripcionesAdmin.adapter = adapter
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

                adapter.submitList(inscripciones)
            }
        }
    }

    private fun loadEventosDetails(eventosIds: List<String?>) {
        db_ref.child("tienda").child("eventos").get().addOnSuccessListener { dataSnapshot ->
            val inscripciones: MutableList<Inscripcion> = mutableListOf()
            if (dataSnapshot.exists()) {
                for (eventoSnapshot in dataSnapshot.children) {
                    val evento = eventoSnapshot.getValue(Evento::class.java)
                    if (evento != null && evento.id in eventosIds.filterNotNull()) {
                        inscripciones.add(Inscripcion(id_evento = evento.id, id_persona = auth.currentUser?.uid))
                    }
                }
                adapter.submitList(inscripciones)
            }
        }
    }






    private fun initDatabase() {
        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }
}
