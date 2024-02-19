package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.R
import com.example.practicamagic.uiClientes.eventosCliente.EventosClienteFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerPersonas : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Persona>
    private lateinit var db_ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_personas)

        db_ref= FirebaseDatabase.getInstance().getReference()

        lista= mutableListOf<Persona>()

        db_ref.child("tienda")
            .child("personas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->
                        val pojo_persona=hijo?.getValue(Persona::class.java)
                        lista.add(pojo_persona!!)
                    }
                    recycler.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        recycler=findViewById(R.id.lista_personas)
        recycler.adapter=AdaptadorPersona(lista)
        recycler.layoutManager= LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)





        volver=findViewById(R.id.volver_ver_personas)
        volver.setOnClickListener {

            val actividad = Intent(applicationContext,EventosClienteFragment::class.java)
            startActivity (actividad)
        }
    }
}