package com.kaleksandra.coredata.network.api

import com.kaleksandra.coredata.network.model.Area
import retrofit2.Response
import retrofit2.http.GET

interface SearchApi {
    @GET("areas/113")
    suspend fun getAllCitiesRussia(): Response<Area>
}