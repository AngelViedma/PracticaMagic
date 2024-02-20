package com.example.practicamagic.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento (var id:String? = null,
                   var nombre: String? = null,
                   var fecha:String? = null,
                   var precio:Double? = null,
                   var aforo_max:Int? = null,
                   var aforo_ocupado:Int? = null,
                   var imagen:String? = null
): Parcelable {
}