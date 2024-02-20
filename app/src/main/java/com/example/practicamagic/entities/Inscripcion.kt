package com.example.practicamagic.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Inscripcion(var id:String?="",
                       var id_evento: String? = "",
                       var id_persona:String?=""
): Parcelable
