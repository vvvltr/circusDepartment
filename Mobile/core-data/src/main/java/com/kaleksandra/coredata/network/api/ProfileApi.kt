package com.kaleksandra.coredata.network.api

import com.kaleksandra.coredata.network.model.Profile
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("Profile")
    fun getProfile() : Response<Profile>
}