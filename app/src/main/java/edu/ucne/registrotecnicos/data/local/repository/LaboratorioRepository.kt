package edu.ucne.registrotecnicos.data.local.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import edu.ucne.registrotecnicos.remote.RemoteDataSource
import edu.ucne.registrotecnicos.remote.Resource
import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LaboratorioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getLaboratorios(): Flow<Resource<List<LaboratorioDto>>> = flow {
        try {
            emit(Resource.Loading())
            val laboratorios = remoteDataSource.getLaboratorios()
            emit(Resource.Success(laboratorios))
        } catch (e: HttpException) {
            Log.e("Retrofit No connection", "Error de conexión ${e.message}", e)
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            Log.e("Retrofit Unknown", "Error desconocido ${e.message}", e)
            emit(Resource.Error("Unknown error ${e.message}"))
        }
    }

    suspend fun getLaboratorio(id: Int) = remoteDataSource.getLaboratorio(id)

    suspend fun createLaboratorio(laboratorio: LaboratorioDto) = remoteDataSource.createLaboratorio(laboratorio)

    suspend fun updateLaboratorio(laboratorio: LaboratorioDto) = remoteDataSource.updateLaboratorio(laboratorio.laboratorioId, laboratorio)

    suspend fun deleteLaboratorio(id: Int) = remoteDataSource.deleteLaboratorio(id)
}
