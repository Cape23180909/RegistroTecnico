package edu.ucne.registrotecnicos.remote.dto

data class LaboratorioDto(
    val laboratorioId: Int = 0,
    val descripcion: String,
    val monto: Double
)