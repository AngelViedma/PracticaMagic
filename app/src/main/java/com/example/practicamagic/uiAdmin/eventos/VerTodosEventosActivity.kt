package com.example.practicamagic.uiAdmin.eventos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practicamagic.R
import com.example.practicamagic.databinding.ActivityVerTodosEventosBinding

class VerTodosEventosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerTodosEventosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerTodosEventosBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}