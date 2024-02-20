package com.example.practicamagic.uiAdmin.eventos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerTodasInscripcionesAdminViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ver inscripciones Fragment"
    }
    val text: LiveData<String> = _text
}