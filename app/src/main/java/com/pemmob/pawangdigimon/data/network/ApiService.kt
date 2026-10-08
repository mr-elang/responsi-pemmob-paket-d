package com.pemmob.pawangdigimon.data.network

import com.pemmob.pawangdigimon.data.model.DigimonDetail
import com.pemmob.pawangdigimon.data.model.DigimonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("digimon")
    suspend fun getDigimons(
        @Query("name") name: String? = null,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 30
    ): DigimonListResponse

    @GET("digimon/{id}")
    suspend fun getDigimonDetail(
        @Path("id") id: Int
    ): DigimonDetail
}
