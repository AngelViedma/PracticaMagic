package com.example.practicamagic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class AdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Configuración"

        supportFragmentManager.beginTransaction().replace(R.id.container_AdminHome,SettingsFragmentAdmin()).commit()


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}