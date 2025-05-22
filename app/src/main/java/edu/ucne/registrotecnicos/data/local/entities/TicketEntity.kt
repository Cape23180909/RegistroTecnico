package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    val ticketId: Int?=null,
    val fecha: String= "",
    val cliente: String= "",
    val asunto: String= "",
    val descripcion: String= "",
    val tecnicoId: Int? = null,
    val prioridad: String
)