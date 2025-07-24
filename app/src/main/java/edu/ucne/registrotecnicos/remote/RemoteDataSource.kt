package edu.ucne.registrotecnicos.data.remote

import edu.ucne.composedemo.data.remote.LaboratorioApi
import edu.ucne.registrotecnicos.remote.PagoApi
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import edu.ucne.registrotecnicos.remote.dto.PagoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: LaboratorioApi,
    private val pagoApi: PagoApi
) {
    suspend fun getLaboratorios(): List<LaboratorioDto> = api.getLaboratorios()

    suspend fun createLaboratorio(laboratorio: LaboratorioDto): LaboratorioDto =
        api.createLaboratorio(laboratorio)

    suspend fun getLaboratorio(id: Int): LaboratorioDto = api.getLaboratorio(id)

    suspend fun updateLaboratorio(id: Int, laboratorio: LaboratorioDto): LaboratorioDto =
        api.updateLaboratorio(id, laboratorio)

    suspend fun deleteLaboratorio(id: Int) = api.deleteLaboratorio(id)

    //Pagos
    suspend fun getPagos(): List<PagoDto> = pagoApi.getPagos()

    suspend fun createPago(pago: PagoDto): PagoDto =
        pagoApi.createPago(pago)

    suspend fun getPago(id: Int): PagoDto = pagoApi.getPago(id)

    suspend fun updatePago(id: Int, pago: PagoDto): PagoDto =
        pagoApi.updatePago(id, pago)

    suspend fun deletePago(id: Int) = pagoApi.deletePago(id)

}