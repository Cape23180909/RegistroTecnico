package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    val TicketId: Int?=null,
    val Fecha: String= "",
    val Cliente: String= "",
    val Asunto: String= "",
    val Descripcion: String= "",
    val TecnicoId: Int? = null,
    val Prioridad: String
)