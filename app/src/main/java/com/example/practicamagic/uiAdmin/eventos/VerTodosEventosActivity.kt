package com.example.practicamagic.uiAdmin.eventos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.practicamagic.R
import com.example.practicamagic.databinding.ActivityVerTodosEventosBinding

class VerTodosEventosActivity : AppCompatActivity() {
    var frameLayout:FrameLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_todos_eventos)
        supportActionBar?.setTitle("Ver Inscripciones a Eventos")

        frameLayout= findViewById(R.id.fragment_container)

        val fragment = VerTodasInscripcionesAdminFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}