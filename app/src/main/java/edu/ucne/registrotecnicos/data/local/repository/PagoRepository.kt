package edu.ucne.registrotecnicos.data.local.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.dao.PagoDao
import edu.ucne.registrotecnicos.data.local.entities.PagoEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.PagoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PagoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val pagoDao: PagoDao
) {
    fun getPagos(): Flow<Resource<List<PagoDto>>> = flow {
        try {
            emit(Resource.Loading())
            // Obtener datos del servidor
            val pagos = remoteDataSource.getPagos()
            emit(Resource.Success(pagos))

            // Guardar en base de datos local
            val listaEntity = pagos.map { it.toEntity() }
            pagoDao.save(listaEntity)

        } catch (e: HttpException) {
            Log.e("Retrofit No connection", "Error de conexión ${e.message}", e)
            emit(Resource.Error("Error de internet: ${e.message}"))
        } catch (e: Exception) {
            Log.e("Retrofit Unknown", "Error desconocido ${e.message}", e)
            emit(Resource.Error("Unknown error: ${e.message}"))
        }
    }

    suspend fun getPago(id: Int) = remoteDataSource.getPago(id)

    suspend fun createPago(pago: PagoDto) =
        remoteDataSource.createPago(pago)

    suspend fun updatePago(pago1: Int, pago: PagoDto) =
        remoteDataSource.updatePago(pago.pagoId, pago)

    suspend fun deleteLaboratorio(id: Int) = remoteDataSource.deleteLaboratorio(id)

    fun PagoDto.toEntity(): PagoEntity {
        return PagoEntity(
            pagoId = this.pagoId,
            descripcion = this.descripcion,
            monto = this.monto
        )
    }
}