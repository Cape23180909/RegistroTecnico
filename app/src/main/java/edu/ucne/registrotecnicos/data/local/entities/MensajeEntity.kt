package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Mensajes")
data class MensajeEntity (
    @PrimaryKey
    val mensajeId: Int? = null,
    val descripcion: String = "",
    val fecha: String?,
    val rol: String = "",
    val nombre: String = ""
)