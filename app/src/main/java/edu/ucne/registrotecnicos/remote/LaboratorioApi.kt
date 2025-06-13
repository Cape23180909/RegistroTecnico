package edu.ucne.registrotecnicos.remote

import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import retrofit2.http.*

interface LaboratorioApi{

    @GET("api/laboratorios")
    suspend fun getLaboratorios(): List<LaboratorioDto>

    @GET("api/laboratorios/{id}")
    suspend fun getLaboratorio(@Path("id") id: Int): LaboratorioDto

    @POST("api/laboratorios")
    suspend fun createLaboratorio(@Body laboratorio: LaboratorioDto): LaboratorioDto

    @PUT("api/laboratorios/{id}")
    suspend fun updateLaboratorio(@Path("id") id: Int, @Body laboratorio: LaboratorioDto): LaboratorioDto

    @DELETE("api/laboratorios/{id}")
    suspend fun deleteLaboratorio(@Path("id") id: Int)
}