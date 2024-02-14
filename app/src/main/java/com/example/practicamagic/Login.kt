package com.example.practicamagic

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import com.example.practicamagic.clientes.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bt_login: Button
    private lateinit var textInputLayoutContrasena: TextInputLayout
    private lateinit var textInputEditTextContrasena: TextInputEditText

    private lateinit var textInputEditTextCorreo: TextInputEditText
    private lateinit var textInputLayoutCorreo: TextInputLayout
    private lateinit var sharedPreferencesPasswords:SharedPreferences
    var esAdmin:String = "cliente"
    private lateinit var lista_usuarios: MutableList<Usuario>

    private lateinit var db_ref: DatabaseReference
    private lateinit var this_activity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        this_activity = this

        db_ref = FirebaseDatabase.getInstance().reference

        db_ref.child("tienda").child("usuarios")
        lista_usuarios = Utilidades.obtenerListaUsuarios(db_ref)
        sharedPreferencesPasswords=PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferencesPasswords.edit {
            putString("admin1234@gmail.com","admin1234")
            putString("admin12345@gmail.com","admin12345")
            apply()
        }

        bt_login=findViewById(R.id.bt_login)

        textInputEditTextCorreo=findViewById(R.id.textInputEditTextCorreo)
        textInputLayoutCorreo=findViewById(R.id.textInputLayoutCorreo)

        textInputEditTextContrasena=findViewById(R.id.textInputEditTextContrasena)
        textInputLayoutContrasena=findViewById(R.id.textInputLayoutContrasena)

        auth = Firebase.auth

        bt_login.setOnClickListener{
            if(validar()){
                val correo=textInputEditTextCorreo.text.toString()
                val contrasena=textInputEditTextContrasena.text.toString()
                auth.signInWithEmailAndPassword(correo,contrasena )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            val id=user?.uid
                            if(checkAdmin(correo,contrasena)== "admin"){
                                esAdmin="admin"
                                Toast.makeText(this,"Eres Admin",Toast.LENGTH_SHORT).show()
                                val intent_AdminHome=Intent(this,AdminHome::class.java)

                                existeUsuario(id!!,correo,contrasena,esAdmin)
                                startActivity(intent_AdminHome)
                            }else{
                                esAdmin="cliente"
                                val intent_ClienteHome=Intent(this,ClienteHome::class.java)
                                Toast.makeText(this,"Eres cliente",Toast.LENGTH_SHORT).show()
                                existeUsuario(id!!,correo,contrasena,esAdmin)
                                startActivity(intent_ClienteHome)
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Correo o contraseña incorrectas.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }


    }
    private fun validar():Boolean{
        var validado=true

        val correo=textInputEditTextCorreo.text.toString().trim()
        val contrasena=textInputEditTextContrasena.text.toString().trim()

        if(correo.isNullOrEmpty()){
            textInputLayoutCorreo.error="El correo no puede estar vacio"
            validado=false
        }else{
            textInputLayoutCorreo.error=null
        }

        if(contrasena.isNullOrEmpty()){
            textInputLayoutContrasena.error="La contraseña no puede estar vacia"
            validado=false
        }else{
            textInputLayoutContrasena.error=null

        }

        return validado
    }

    private fun checkAdmin(correo:String,pass:String):String{
        val mapPreferences=sharedPreferencesPasswords.all
        var esAdmin="cliente"

        if(mapPreferences.containsKey(correo)){
            if(mapPreferences[correo]==pass){
                esAdmin="admin"
            }
        }else{
            esAdmin="cliente"
        }
        return esAdmin
    }

    private fun existeUsuario(id:String,login:String,password:String,esAdmin:String){
        if (Utilidades.existeUsuario(lista_usuarios, login.trim())) {

            Toast.makeText(applicationContext, "El Usuario ya existe en la base de datos", Toast.LENGTH_SHORT)
                .show()
        }else {
                Utilidades.crearUsuario(
                    id!!,
                    login.trim(),
                    password.trim(),
                    esAdmin.trim()
                )
                Utilidades.tostadaCorrutina(
                    this_activity,
                    applicationContext,
                    "Usuario creado con exito en la base de datos"
                )
        }
    }
}