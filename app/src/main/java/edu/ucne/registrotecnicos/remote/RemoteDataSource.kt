package edu.ucne.registrotecnicos.data.remote

import edu.ucne.composedemo.data.remote.LaboratorioApi
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: LaboratorioApi
) {
    suspend fun getLaboratorios(): List<LaboratorioDto> = api.getLaboratorios()

    suspend fun createLaboratorio(laboratorio: LaboratorioDto): LaboratorioDto =
        api.createLaboratorio(laboratorio)

    suspend fun getLaboratorio(id: Int): LaboratorioDto = api.getLaboratorio(id)

    suspend fun updateLaboratorio(id: Int, laboratorio: LaboratorioDto): LaboratorioDto =
        api.updateLaboratorio(id, laboratorio)

    suspend fun deleteLaboratorio(id: Int) = api.deleteLaboratorio(id)
}