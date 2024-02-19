package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamagic.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class EventosPersona : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Evento>
    private lateinit var db_ref: DatabaseReference
    private lateinit var volver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_eventos)

        val pojo_persona:Persona=intent.getParcelableExtra<Persona>("persona")!!

        db_ref= FirebaseDatabase.getInstance().getReference()

        lista= mutableListOf<Evento>()

        db_ref.child("tienda")
            .child("inscripciones")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    GlobalScope.launch ( Dispatchers.IO){
                        snapshot.children.forEach{ hijo: DataSnapshot?->
                            val pojo_inscripcion=hijo?.getValue(Inscripcion::class.java)
                            if(pojo_inscripcion!!.id_persona==pojo_persona.id){
                                var pojo_evento:Evento=Evento()
                                //USAMOS EL SEMAFORO PARA SINCRONIZAR: LINEALIZAMOS EL CODIGO
                                var semaforo = CountDownLatch(1)

                                db_ref.child("tienda")
                                    .child("eventos")
                                    .child(pojo_inscripcion!!.id_evento!!)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            pojo_evento = snapshot!!.getValue(Evento::class.java)!!
                                            //RELLENAR LOS DATOS PARA EL ADAPTADOR
                                            //PARA PODER BORRAR LA INSCRIPCION
                                            pojo_evento.id_inscripcion=pojo_inscripcion.id
                                            lista.add(pojo_evento)
                                            semaforo.countDown()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            println(error.message)
                                        }
                                    })
                                semaforo.await()

                            }
                        }

                        runOnUiThread {
                            recycler.adapter?.notifyDataSetChanged()

                        }
                    }
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

            val actividad = Intent(applicationContext,VerPersonas::class.java)
            startActivity (actividad)
        }
    }
}