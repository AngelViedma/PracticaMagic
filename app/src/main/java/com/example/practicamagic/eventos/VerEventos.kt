package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.R
import com.example.practicamagic.uiClientes.homeCliente.HomeClienteFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerEventos : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Evento>
    private lateinit var db_ref: DatabaseReference


    private lateinit var volver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_eventos)

        db_ref= FirebaseDatabase.getInstance().getReference()

        lista= mutableListOf<Evento>()

        db_ref.child("tienda")
            .child("eventos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->
                        val objeto_evento=hijo?.getValue(Evento::class.java)
                        lista.add(objeto_evento!!)
                    }
                    recycler.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        recycler=findViewById(R.id.rv_lista_eventos)
        recycler.adapter=EventoAdapter(lista)
        recycler.layoutManager= LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)

        volver=findViewById(R.id.volver_ver_eventos)
        volver.setOnClickListener {

            val actividad = Intent(applicationContext,HomeClienteFragment::class.java)
            startActivity (actividad)
        }
    }
}