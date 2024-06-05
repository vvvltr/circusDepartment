package com.kaleksandra.coredata.network.api

import com.kaleksandra.coredata.network.model.AuthDto
import com.kaleksandra.coredata.network.model.TokenDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("User/Login")
    suspend fun auth(@Body authBody: AuthDto): Response<TokenDto>

    @POST("User/Register")
    suspend fun register(@Body authBody: AuthDto): Response<ResponseBody>
}