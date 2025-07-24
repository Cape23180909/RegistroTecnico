package edu.ucne.registrotecnicos.data.local.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.dao.PagoDao
import edu.ucne.registrotecnicos.data.local.entities.PagoEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.PagoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PagoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val pagoDao: PagoDao
) {
    fun getPagos(): Flow<Resource<List<PagoDto>>> = flow {
        emit(Resource.Loading())

        try {
            val pagosRemotos = remoteDataSource.getPagos()
            emit(Resource.Success(pagosRemotos))

            val listaEntity = pagosRemotos.map { it.toEntity() }
            pagoDao.save(listaEntity)

        } catch (e: Exception) {
            Log.e("PagoRepository", "Fallo en conexión remota: ${e.message}", e)

            pagoDao.getAll().collect { pagosLocales ->
                val dtoList = pagosLocales.map { it.toDto() }

                if (dtoList.isNotEmpty()) {
                    emit(Resource.Success(dtoList))
                } else {
                    emit(Resource.Error("No hay conexión a internet y no hay datos locales"))
                }
            }
        }
    }

    suspend fun getPago(id: Int) = remoteDataSource.getPago(id)

    suspend fun createPago(pago: PagoDto) =
        remoteDataSource.createPago(pago)

    suspend fun updatePago(pago1: Int, pago: PagoDto) =
        remoteDataSource.updatePago(pago.pagoId, pago)

    suspend fun deleteLaboratorio(id: Int) = remoteDataSource.deleteLaboratorio(id)

    // ✅ Función de extensión para convertir PagoDto a PagoEntity
    private fun PagoDto.toEntity(): PagoEntity {
        return PagoEntity(
            pagoId = this.pagoId,
            descripcion = this.descripcion,
            monto = this.monto
        )
    }

    // ✅ Función de extensión para convertir PagoEntity a PagoDto
    private fun PagoEntity.toDto(): PagoDto {
        return PagoDto(
            pagoId = this.pagoId,
            descripcion = this.descripcion,
            monto = this.monto
        )
    }
}