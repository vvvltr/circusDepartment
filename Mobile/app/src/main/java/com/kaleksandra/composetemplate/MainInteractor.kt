package com.taekwondo.taekwondoapp

import com.kaleksandra.coredata.network.database.DataStoreProvider
import com.kaleksandra.coredata.network.database.TOKEN_KEY
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val dataStore: DataStoreProvider
) {
    fun getToken(): Flow<Boolean> {
        return dataStore.contains(TOKEN_KEY)
    }
}