package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.dao.LaboratorioDao
import edu.ucne.registrotecnicos.data.local.entities.LaboratorioEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LaboratorioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val laboratorioDao: LaboratorioDao
) {
    fun getLaboratorios(): Flow<Resource<List<LaboratorioDto>>> = flow {
        try {
            emit(Resource.Loading())

            // ✅ Intentar obtener desde servidor
            val laboratorios = remoteDataSource.getLaboratorios()
            emit(Resource.Success(laboratorios))

            // ✅ Guardar en base de datos local
            val listaEntity = laboratorios.map { it.toEntity() }
            laboratorioDao.save(listaEntity)

        } catch (e: HttpException) {
            Log.e("Retrofit No connection", "Error de conexión ${e.message}", e)

            // ✅ Obtener desde base de datos local si falla
            laboratorioDao.getAll().collect { locales ->
                val dtoList = locales.map { it.toDto() }

                if (dtoList.isNotEmpty()) {
                    emit(Resource.Success(dtoList))
                } else {
                    emit(Resource.Error("No hay conexión a internet y no hay datos locales"))
                }
            }

        } catch (e: Exception) {
            Log.e("Retrofit Unknown", "Error desconocido ${e.message}", e)

            // ✅ También intentar desde local en cualquier otro error
            laboratorioDao.getAll().collect { locales ->
                val dtoList = locales.map { it.toDto() }

                if (dtoList.isNotEmpty()) {
                    emit(Resource.Success(dtoList))
                } else {
                    emit(Resource.Error("Error desconocido y sin datos locales"))
                }
            }
        }
    }

    private fun LaboratorioEntity.toDto(): LaboratorioDto {
        return LaboratorioDto(
            laboratorioId = this.laboratorioId,
            descripcion = this.descripcion,
            monto = this.monto
        )
    }

    suspend fun getLaboratorio(id: Int) = remoteDataSource.getLaboratorio(id)

    suspend fun createLaboratorio(laboratorio: LaboratorioDto) =
        remoteDataSource.createLaboratorio(laboratorio)

    suspend fun updateLaboratorio(laboratorio: LaboratorioDto) =
        remoteDataSource.updateLaboratorio(laboratorio.laboratorioId, laboratorio)

    suspend fun deleteLaboratorio(id: Int) = remoteDataSource.deleteLaboratorio(id)

    fun LaboratorioDto.toEntity(): LaboratorioEntity {
        return LaboratorioEntity(
            laboratorioId = this.laboratorioId,
            descripcion = this.descripcion,
            monto = this.monto
        )
    }
}