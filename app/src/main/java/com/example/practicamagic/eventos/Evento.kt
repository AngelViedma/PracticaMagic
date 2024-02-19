package com.example.practicamagic.eventos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento (var id:String?="",
                   var nombre: String? = "",
                   var fecha:String?="",
                   var precio:String?="gratis",
                   var aforo_max:String?="",
                   var aforo_ocupado:String?="0",
                   var imagen:String?=""
): Parcelable {
    override fun toString(): String =nombre!!
}