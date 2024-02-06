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

class CrearCuenta : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bt_crear_usuario:Button
    private lateinit var textInputLayoutContrasena:TextInputLayout
    private lateinit var textInputEditTextContrasena:TextInputEditText

    private lateinit var textInputLayoutRepetirContrasena: TextInputLayout
    private lateinit var textInputEditTextRepetirContrasena:TextInputEditText

    private lateinit var textInputEditTextCorreo:TextInputEditText
    private lateinit var textInputLayoutCorreo:TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        bt_crear_usuario=findViewById(R.id.bt_registrar_datos_usuario)

        textInputEditTextCorreo=findViewById(R.id.textInputEditTextCorreo)
        textInputLayoutCorreo=findViewById(R.id.textInputLayoutCorreo)

        textInputEditTextContrasena=findViewById(R.id.textInputEditTextContrasena)
        textInputLayoutContrasena=findViewById(R.id.textInputLayoutContrasena)

        textInputLayoutRepetirContrasena=findViewById(R.id.textInputLayoutRepetirContrasena)
        textInputEditTextRepetirContrasena=findViewById(R.id.textInputEditTextRepetirContrasena)




        bt_crear_usuario.setOnClickListener{
            if(validar()){
                auth= Firebase.auth
                auth.createUserWithEmailAndPassword(textInputEditTextCorreo.text.toString(), textInputEditTextContrasena.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(this,"Usuario creado correctamente",Toast.LENGTH_SHORT).show()
                            val intent_ir_login= Intent(this,Login::class.java)
                            startActivity(intent_ir_login)
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Log.e("ERRORXD","${task.exception?.message}")
                            Toast.makeText(
                                baseContext,
                                "Ese correo ya existe",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }
            }
        }



    }

    private fun validar():Boolean{
        var validado=true

        val correo=textInputEditTextCorreo.text.toString().trim()
        val contrasena=textInputEditTextContrasena.text.toString().trim()
        val contrasenaRepetir=textInputEditTextRepetirContrasena.text.toString().trim()

        if(correo.isNullOrEmpty()){
            textInputLayoutCorreo.error="El correo no puede estar vacio"
            validado=false
        }else{
            textInputLayoutCorreo.error=null
        }

        if(!contrasena.equals(contrasenaRepetir)){
            textInputLayoutRepetirContrasena.error="Las contraseñas no coinciden"
            validado=false
        }else{
            textInputLayoutRepetirContrasena.error=null
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