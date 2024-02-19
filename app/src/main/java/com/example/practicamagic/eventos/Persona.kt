package com.example.practicamagic.eventos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Persona (var id:String?="",
                    var nombre: String? = "",
                    var dni:String?="",
                    var id_inscripcion:String?=""
): Parcelable {
    override fun toString(): String =nombre!!
}