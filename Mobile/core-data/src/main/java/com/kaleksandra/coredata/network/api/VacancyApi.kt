package com.kaleksandra.coredata.network.api

import com.kaleksandra.coredata.network.model.VacancyResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VacancyApi {
    @GET("Vacancies/GetSkillCheck")
    suspend fun searchVacancy(@QueryMap query: Map<String, String>): Response<VacancyResponse>
}