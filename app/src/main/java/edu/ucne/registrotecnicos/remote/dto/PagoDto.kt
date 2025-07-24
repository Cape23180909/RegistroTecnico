package edu.ucne.registrotecnicos.remote.dto

data class PagoDto (
    val pagoId: Int = 0,
    val descripcion: String,
    val monto: Double
)