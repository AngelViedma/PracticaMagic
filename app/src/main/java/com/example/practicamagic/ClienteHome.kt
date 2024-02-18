package com.example.practicamagic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practicamagic.databinding.ActivityClienteHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClienteHome : AppCompatActivity() {

    private lateinit var binding: ActivityClienteHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClienteHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navViewCliente

        val navController = findNavController(R.id.nav_host_fragment_activity_cliente)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_cliente, R.id.navigation_eventos_cliente, R.id.navigation_pedidos_cliente, R.id.navigation_ajustes_cliente
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}