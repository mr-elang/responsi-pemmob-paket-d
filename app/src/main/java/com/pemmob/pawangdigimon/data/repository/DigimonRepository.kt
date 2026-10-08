package com.pemmob.pawangdigimon.data.repository

import com.pemmob.pawangdigimon.data.model.DigimonDetail
import com.pemmob.pawangdigimon.data.model.DigimonListResponse
import com.pemmob.pawangdigimon.data.network.ApiService
import com.pemmob.pawangdigimon.data.network.RetrofitInstance

class DigimonRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun getDigimons(
        name: String? = null,
        page: Int = 0,
        pageSize: Int = 30
    ): Result<DigimonListResponse> {
        return runCatching {
            api.getDigimons(name = name, page = page, pageSize = pageSize)
        }
    }

    suspend fun getDigimonDetail(id: Int): Result<DigimonDetail> {
        return runCatching {
            api.getDigimonDetail(id)
        }
    }
}
