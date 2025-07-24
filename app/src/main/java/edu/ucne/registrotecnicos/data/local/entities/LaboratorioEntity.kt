package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Laboratorios")
data class LaboratorioEntity (
    @PrimaryKey(autoGenerate = true)
    val laboratorioId: Int,
    val descripcion: String,
    val monto: Double
)