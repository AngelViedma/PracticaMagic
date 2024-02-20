package com.example.practicamagic.uiAdmin.ventas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VentasAdminViewModel : ViewModel() {
    private val _text = MutableLiveData("ajustes admin fragment")
    val text: LiveData<String> = _text
}