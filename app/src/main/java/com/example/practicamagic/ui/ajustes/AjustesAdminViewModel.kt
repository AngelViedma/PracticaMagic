package com.example.practicamagic.ui.ajustes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AjustesAdminViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Admin Fragment"
    }
    val text: LiveData<String> = _text


}