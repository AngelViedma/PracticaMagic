package com.example.practicamagic.entities


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pedido(
    var id: String? = null,
    var usuarioId : String? = null,
    var cartaId: String? = null,
    var estado: String? = "En preparacion"
): Parcelable