package com.example.practicamagic.eventos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.practicamagic.R
import com.example.practicamagic.uiClientes.homeCliente.HomeClienteFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearPersona : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var crear_persona: Button
    private lateinit var nombre_persona: EditText
    private lateinit var dni_persona: EditText
    private lateinit var db_ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_persona)

        db_ref= FirebaseDatabase.getInstance().getReference()

        nombre_persona=findViewById(R.id.nombre_persona)
        dni_persona=findViewById(R.id.dni)
        crear_persona=findViewById(R.id.nueva_persona)

        crear_persona.setOnClickListener {
            val id_generado=db_ref.child("tienda")
                .child("personas")
                .push().key

            val nuevo_evento=Persona(id_generado,
                nombre_persona.text.toString(),
                dni_persona.text.toString())

            db_ref.child("tienda")
                .child("personas")
                .child(id_generado!!)
                .setValue(nuevo_evento)


            Toast.makeText(applicationContext, "Persona introducida con éxito", Toast.LENGTH_SHORT).show()
            val actividad = Intent(applicationContext,HomeClienteFragment::class.java)
            startActivity (actividad)
        }

        volver=findViewById(R.id.volver_crear_persona)
        volver.setOnClickListener {

            val actividad = Intent(applicationContext,HomeClienteFragment::class.java)
            startActivity (actividad)
        }
    }
}