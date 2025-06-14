package edu.ucne.composedemo.data.remote

import edu.ucne.registrotecnicos.remote.dto.LaboratorioDto
import retrofit2.http.*

interface LaboratorioApi {
    @GET("api/Laboratorios")
    suspend fun getLaboratorios(): List<LaboratorioDto>

    @GET("api/Laboratorios/{id}")
    suspend fun getLaboratorio(@Path("id") id: Int): LaboratorioDto

    @POST("api/Laboratorios")
    suspend fun createLaboratorio(@Body laboratorioDto: LaboratorioDto): LaboratorioDto

    @PUT("api/Laboratorios/{id}")
    suspend fun updateLaboratorio(@Path("id") id: Int, @Body laboratorioDto: LaboratorioDto): LaboratorioDto

    @DELETE("api/Laboratorios/{id}")
    suspend fun deleteLaboratorio(@Path("id") id: Int)
}