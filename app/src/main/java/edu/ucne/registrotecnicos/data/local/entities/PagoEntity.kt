package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pagos")
data class PagoEntity (
    @PrimaryKey(autoGenerate = true)
    val pagoId: Int,
    val descripcion: String,
    val monto: Double
)