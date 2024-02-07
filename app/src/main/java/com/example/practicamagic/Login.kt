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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bt_login: Button
    private lateinit var textInputLayoutContrasena: TextInputLayout
    private lateinit var textInputEditTextContrasena: TextInputEditText

    private lateinit var textInputEditTextCorreo: TextInputEditText
    private lateinit var textInputLayoutCorreo: TextInputLayout
    private lateinit var sharedPreferencesPasswords:SharedPreferences
    var esAdmin:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                            if(checkAdmin(correo,contrasena)){
                                esAdmin=true
                                Toast.makeText(this,"Eres Admin",Toast.LENGTH_SHORT).show()
                                val intent_AdminHome=Intent(this,AdminHome::class.java)
                                startActivity(intent_AdminHome)
                            }else{
                                esAdmin=false
                                val intent_ClienteHome=Intent(this,ClienteHome::class.java)
                                Toast.makeText(this,"Eres cliente",Toast.LENGTH_SHORT).show()
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

    private fun checkAdmin(correo:String,pass:String):Boolean{
        val mapPreferences=sharedPreferencesPasswords.all
        var esAdmin=false

        if(mapPreferences.containsKey(correo)){
            if(mapPreferences[correo]==pass){
                esAdmin=true
            }
        }else{
            esAdmin=false
        }
        return esAdmin
    }


}