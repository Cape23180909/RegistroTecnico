package edu.ucne.registrotecnicos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey
    val TecnicoId: Int?= null,
    val Nombre: String = "",
    val Sueldo: Double = 0.0
)