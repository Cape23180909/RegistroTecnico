package edu.ucne.registrotecnicos.remote

import edu.ucne.registrotecnicos.remote.dto.PagoDto
import retrofit2.http.*

interface PagoApi {

    @GET("api/Pagos")
    suspend fun getPagos(): List<PagoDto>

    @GET("api/Pagos/{id}")
    suspend fun getPago(@Path("id") id: Int): PagoDto

    @POST("api/Pagos")
    suspend fun createPago(@Body pagoDto: PagoDto): PagoDto

    @PUT("api/Pagos/{id}")
    suspend fun updatePago(@Path("id") id: Int, @Body pagoDto: PagoDto): PagoDto

    @DELETE("api/Pagos/{id}")
    suspend fun deletePago(@Path("id") id: Int)
}