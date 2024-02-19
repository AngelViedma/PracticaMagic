package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.practicamagic.R
import com.example.practicamagic.uiAdmin.home.HomeAdminFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearEvento : AppCompatActivity() {

    private lateinit var volver: Button
    private lateinit var crear_evento: Button
    private lateinit var nombre_evento: EditText
    private lateinit var fecha_evento: EditText
    private lateinit var db_ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        db_ref= FirebaseDatabase.getInstance().getReference()

        nombre_evento=findViewById(R.id.nombre_evento)
        fecha_evento=findViewById(R.id.fecha)


        crear_evento=findViewById(R.id.nuevo_evento)

        crear_evento.setOnClickListener {

            val id_generado=db_ref.child("aplicacion")
                .child("eventos")
                .push().key

            val nuevo_evento=Evento(id_generado,
                nombre_evento.text.toString(),
                fecha_evento.text.toString())

            db_ref.child("aplicacion")
                .child("eventos")
                .child(id_generado!!)
                .setValue(nuevo_evento)


            Toast.makeText(applicationContext, "Evento creado con éxito", Toast.LENGTH_SHORT).show()
            val actividad = Intent(applicationContext,HomeAdminFragment::class.java)
            startActivity (actividad)
        }

        volver=findViewById(R.id.volver_crear_evento)

        volver.setOnClickListener {

            val actividad = Intent(applicationContext,HomeAdminFragment::class.java)
            startActivity (actividad)
        }

    }
}