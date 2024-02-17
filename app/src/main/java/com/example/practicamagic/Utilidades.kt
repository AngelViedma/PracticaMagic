package com.example.practicamagic

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practicamagic.clientes.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class Utilidades {
    companion object {

        fun existeUsuario(usuarios: List<Usuario>, login: String): Boolean {
            return usuarios.any { it.login!!.lowercase() == login.lowercase() }
        }

        //Obtener lista datos pero no para usar de manera sincrona o sea que algo dependa directamente de el
        fun obtenerListaUsuarios(db_ref: DatabaseReference): MutableList<Usuario> {
            var lista = mutableListOf<Usuario>()
            //Consulta a la bd
            db_ref.child("tienda")
                .child("usuarios")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //lista.clear()
                        snapshot.children.forEach { hijo: DataSnapshot? ->
                            val pojo_usuarios = hijo?.getValue(Usuario::class.java)

                            lista.add(pojo_usuarios!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }
                })

            return lista
        }


        fun crearUsuario(id: String, login: String, password: String, tipo: String){
            var db_ref = FirebaseDatabase.getInstance().reference
            val usuario= Usuario(id, login, password, tipo)
            db_ref.child("tienda").child("usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(usuario)
        }



        fun tostadaCorrutina(activity: AppCompatActivity, contexto: Context, texto: String) {
            activity.runOnUiThread {
                Toast.makeText(
                    contexto,
                    texto,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        suspend fun guardarImagen(sto_ref: StorageReference, id: String, imagen: Uri): String {
            lateinit var url_carta_firebase: Uri

            url_carta_firebase = sto_ref.child("tienda").child("cartas").child(id)
                .putFile(imagen).await().storage.downloadUrl.await()

            return url_carta_firebase.toString()
        }

    }
}