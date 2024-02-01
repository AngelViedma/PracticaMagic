package com.example.practicamagic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var bt_login:Button
    lateinit var bt_crear_cuenta:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_login=findViewById(R.id.bt_registrar_datos_usuario)
        bt_crear_cuenta=findViewById(R.id.bt_crear_cuenta)

        val intent_login=Intent(this,Login::class.java)
        val intent_crear_cuenta=Intent(this,CrearCuenta::class.java)

        bt_login.setOnClickListener{startActivity(intent_login)}
        bt_crear_cuenta.setOnClickListener{startActivity(intent_crear_cuenta)}



    }
}