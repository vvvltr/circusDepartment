package com.kaleksandra.coredata.network.repository

import com.kaleksandra.coredata.network.Completable
import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.api.AuthApi
import com.kaleksandra.coredata.network.call
import com.kaleksandra.coredata.network.database.DataStoreProvider
import com.kaleksandra.coredata.network.database.TOKEN_KEY
import com.kaleksandra.coredata.network.di.IoDispatcher
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.model.AuthDto
import com.kaleksandra.coredata.network.toCompletable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(email: String, password: String): Effect<Completable>
    suspend fun register(
        email: String,
        password: String,
    ): Effect<Completable>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val dataStoreProvider: DataStoreProvider,
) : AuthRepository {
    override suspend fun auth(email: String, password: String): Effect<Completable> {
        return call(dispatcher) {
            api.auth(AuthDto(email, password))
        }.doOnSuccess {
            withContext(dispatcher) {
                dataStoreProvider.update(
                    TOKEN_KEY,
                    it.token
                )
            }
        }.toCompletable()
    }

    override suspend fun register(
        email: String,
        password: String,
    ): Effect<Completable> {
        return call(dispatcher) {
            api.register(AuthDto(email, password))
        }.toCompletable()
    }
}