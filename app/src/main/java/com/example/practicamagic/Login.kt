package com.example.practicamagic

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bt_login=findViewById(R.id.bt_login)

        textInputEditTextCorreo=findViewById(R.id.textInputEditTextCorreo)
        textInputLayoutCorreo=findViewById(R.id.textInputLayoutCorreo)

        textInputEditTextContrasena=findViewById(R.id.textInputEditTextContrasena)
        textInputLayoutContrasena=findViewById(R.id.textInputLayoutContrasena)

        auth = Firebase.auth

        bt_login.setOnClickListener{
            if(validar()){
                auth.signInWithEmailAndPassword(textInputEditTextCorreo.text.toString(), textInputEditTextContrasena.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            val intent_main=Intent(this,MainActivity::class.java)
                            startActivity(intent_main)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
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


}