package com.example.practicamagic


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carta(
    var id: String? = null,
    var nombre : String? = null,
    var categoria: String? = null,
    var precio: Double? = null,
    var stock: Boolean? = null,
    var imagen: String? = null,
): Parcelable