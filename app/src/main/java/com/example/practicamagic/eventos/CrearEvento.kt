package com.example.practicamagic.eventos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practicamagic.Utilidades
import com.example.practicamagic.databinding.ActivityCrearEventoBinding
import com.example.practicamagic.entities.Evento
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearEvento : AppCompatActivity() {
    private lateinit var binding:ActivityCrearEventoBinding
    private lateinit var st_ref: StorageReference
    private lateinit var db_ref: DatabaseReference
    private var imagenUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        st_ref = FirebaseStorage.getInstance().reference
        db_ref = FirebaseDatabase.getInstance().reference

        val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            if (uri != null) {
                imagenUri = uri
                binding.imageEvento.setImageURI(imagenUri)
            }
        }
        binding.imageEvento.setOnClickListener {
            accesoGaleria.launch("image/*")
        }

        binding.btCrearEvento.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val nombre = binding.textInputEditTextNombreEvento.text.toString()
                val fecha = binding.textInputEditTextFechaEvento.text.toString()
                val precio = binding.textInputEditTextPrecioEvento.text.toString().toDouble()
                val aforo_max = binding.textInputEditTextAforoMaximoEvento.text.toString().toInt()
                val aforo_ocupado = 0
                val id_generado: String? = db_ref.child("tienda").child("eventos").push().key
                val imagen = Utilidades.guardarImagenEvento(st_ref, id_generado!!, imagenUri!!)
                if (nombre.isEmpty() || fecha.isEmpty() || imagen.isEmpty() || precio.isNaN() || aforo_max<0) {
                    Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT)
                        .show()
                    return@launch
                }
                val evento =
                    Evento(id_generado, nombre, fecha, precio, aforo_max, aforo_ocupado, imagen)
                saveEvent(evento)
            }

        }
    }

    private fun saveEvent(evento: Evento) {
        db_ref.child("tienda").child("eventos").child(evento.id.toString()).setValue(evento).addOnSuccessListener {
            Toast.makeText(this, "Evento guardada con exito", Toast.LENGTH_SHORT).show()
            setResult(-1, Intent().putExtra("evento",evento))
            finish()
        }
    }

}