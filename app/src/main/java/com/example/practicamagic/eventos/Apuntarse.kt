package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.practicamagic.ClienteHome
import com.example.practicamagic.R
import com.example.practicamagic.uiClientes.homeCliente.HomeClienteFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Apuntarse : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var apuntarse: Button
    private lateinit var persona_spinner: Spinner
    private lateinit var evento_spinner: Spinner
    private lateinit var db_ref: DatabaseReference
    private lateinit var lista_inscripciones:MutableList<Inscripcion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apuntarse)

        db_ref= FirebaseDatabase.getInstance().getReference()

        val lista_persona_spinner:MutableList<Persona>
        val adaptador_persona_spinner: ArrayAdapter<Persona>

        persona_spinner=findViewById(R.id.spinner_persona)
        lista_persona_spinner= mutableListOf<Persona>()
        adaptador_persona_spinner=ArrayAdapter<Persona>(applicationContext,
            android.R.layout.simple_spinner_dropdown_item,lista_persona_spinner)
        persona_spinner.adapter=adaptador_persona_spinner

        db_ref.child("aplicacion")
            .child("personas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista_persona_spinner.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->
                        val pojo_persona=hijo?.getValue(Persona::class.java)
                        lista_persona_spinner.add(pojo_persona!!)
                    }
                    adaptador_persona_spinner.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        val lista_eventos_spinner:MutableList<Evento>
        val adaptador_eventos_spinner: ArrayAdapter<Evento>

        evento_spinner=findViewById(R.id.spinner_evento)
        lista_eventos_spinner= mutableListOf<Evento>()
        adaptador_eventos_spinner=ArrayAdapter<Evento>(applicationContext,
            android.R.layout.simple_spinner_dropdown_item,lista_eventos_spinner)
        evento_spinner.adapter=adaptador_eventos_spinner

        db_ref.child("aplicacion")
            .child("eventos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista_eventos_spinner.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->
                        val pojo_evento=hijo?.getValue(Evento::class.java)
                        lista_eventos_spinner.add(pojo_evento!!)
                    }
                    adaptador_eventos_spinner.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        lista_inscripciones= mutableListOf<Inscripcion>()
        db_ref.child("aplicacion")
            .child("inscripciones")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista_inscripciones.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->
                        val objeto_inscripcion=hijo?.getValue(Inscripcion::class.java)
                        lista_inscripciones.add(objeto_inscripcion!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })


        apuntarse=findViewById(R.id.apuntarse)
        apuntarse.setOnClickListener {
            val id_persona=(persona_spinner.selectedItem as Persona).id
            val id_evento=(evento_spinner.selectedItem as Evento).id

            if(existeInscripcion(lista_inscripciones,id_evento!!,id_persona!!)){
                Toast.makeText(applicationContext, "No puedes inscribir mas de una vez a la misma persona", Toast.LENGTH_SHORT).show()
            }else {

                val id_generado = db_ref.child("aplicacion").child("inscripciones").push().key

                val nueva_inscripcion = Inscripcion(
                    id_generado,
                    id_evento,
                    id_persona
                )

                db_ref.child("aplicacion")
                    .child("inscripciones")
                    .child(id_generado!!)
                    .setValue(nueva_inscripcion)
                Toast.makeText(
                    applicationContext,
                    "Inscripcion realizada con éxito",
                    Toast.LENGTH_SHORT
                ).show()
                val actividad = Intent(applicationContext, HomeClienteFragment::class.java)
                startActivity(actividad)
            }
        }

        volver=findViewById(R.id.volver_apuntarse)
        volver.setOnClickListener {

            val actividad = Intent(applicationContext,HomeClienteFragment::class.java)
            startActivity (actividad)
        }
    }

    fun existeInscripcion(lista:MutableList<Inscripcion>,evento:String,persona:String):Boolean{
        return lista.any { it.id_persona==persona && it.id_evento==evento }
    }
}