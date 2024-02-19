package com.example.practicamagic.eventos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento (var id:String?="",
                   var nombre: String? = "",
                   var fecha:String?="",
                   var id_inscripcion:String?=""
): Parcelable {
    override fun toString(): String =nombre!!
}