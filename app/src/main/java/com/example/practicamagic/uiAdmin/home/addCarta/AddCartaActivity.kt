package com.example.practicamagic.uiAdmin.home.addCarta

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practicamagic.entities.Carta
import com.example.practicamagic.Utilidades
import com.example.practicamagic.databinding.ActivityAddCartaBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddCartaActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCartaBinding
    lateinit var st_ref: StorageReference
    lateinit var db_ref: DatabaseReference
    private var imagenUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCartaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        st_ref= FirebaseStorage.getInstance().reference
        db_ref= FirebaseDatabase.getInstance().reference

        binding.imageCarta.setOnClickListener {
            accesoGaleria.launch("image/*")
        }

        binding.btAddCarta.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val nombre = binding.textInputEditTextNombreCarta.text.toString()
                val categoria = binding.textInputEditTextCategoriaCarta.text.toString()
                val precio = binding.textInputEditTextPrecioCarta.text.toString().toDouble()
                val stock = binding.textInputEditTextStockCarta.text.toString()

                var id_generado: String? = db_ref.child("tienda").child("cartas").push().key
                    val imagen = Utilidades.guardarImagenCarta(st_ref, id_generado!!, imagenUri!!)

                val finalStock=stockFinal(stock)

                if (nombre.isEmpty() || categoria.isEmpty() || precio.isNaN() || imagen.isEmpty()) {
                    Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val carta = Carta(id_generado,nombre, categoria, precio, finalStock, imagen)
                saveCard(carta)
            }

        }
    }
    private fun saveCard(carta: Carta) {
        db_ref.child("tienda").child("cartas").child(carta.id.toString()).setValue(carta).addOnSuccessListener {
            Toast.makeText(this, "Carta guardada con exito", Toast.LENGTH_SHORT).show()
            setResult(-1, Intent().putExtra("carta",carta))
            finish()
        }
    }

    private val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri: Uri? ->
        if (uri != null) {
            imagenUri = uri
            binding.imageCarta.setImageURI(imagenUri)
        }
    }
    fun stockFinal(stock:String):String{
        return when(stock){
            "0"->"Agotado"
            "1"->"Disponible"
            "Disponible"->"Disponible"
            "Agotado"->"Agotado"
            else->"Agotado"
        }
    }
}