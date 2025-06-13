package edu.ucne.registrotecnicos.remote

import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val laboratoriosApi: LaboratorioApi
) {
    suspend fun getLaboratorios(): List<LaboratorioDto> = laboratoriosApi.getLaboratorios()

    suspend fun getLaboratorio(id: Int): LaboratorioDto = laboratoriosApi.getLaboratorio(id)

    suspend fun createLaboratorio(laboratorio: LaboratorioDto): LaboratorioDto = laboratoriosApi.createLaboratorio(laboratorio)

    suspend fun updateLaboratorio(id: Int, laboratorio: LaboratorioDto): LaboratorioDto = laboratoriosApi.updateLaboratorio(id, laboratorio)

    suspend fun deleteLaboratorio(id: Int) = laboratoriosApi.deleteLaboratorio(id)
}