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
        db_ref.child("tienda").child("reservas_eventos").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                inscripciones.clear()
                for (inscripcionSnapshot in dataSnapshot.children) {
                    val inscripcion = inscripcionSnapshot.getValue(Inscripcion::class.java)
                    if (inscripcion != null && inscripcion.id_persona == auth.currentUser?.uid) {
                        inscripciones.add(inscripcion)
                    }
                }
                val eventosIds = inscripciones.map { it.id_evento }
                loadEventosDetails(eventosIds)
            }
        }
    }

    private fun loadEventosDetails(eventosIds: List<String?>) {
        db_ref.child("tienda").child("eventos").get().addOnSuccessListener { dataSnapshot ->
            val inscripciones: MutableList<Inscripcion> = mutableListOf()
            if (dataSnapshot.exists()) {
                for (eventoSnapshot in dataSnapshot.children) {
                    val evento = eventoSnapshot.getValue(Evento::class.java)
                    val eventoId = evento?.id
                    Log.d("Inscripciones", "Evento ID: $eventoId")
                    Log.d("Inscripciones", "Lista de IDs de eventos: $eventosIds")
                    if (eventoId != null && eventosIds.contains(eventoId)) {
                        inscripciones.add(Inscripcion(id_evento = eventoId, id_persona = auth.currentUser?.uid))
                        Log.d("Inscripciones", "Evento agregado a inscripciones: $evento")
                    }
                }
                Log.d("Inscripciones", "Lista de inscripciones: $inscripciones")
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
