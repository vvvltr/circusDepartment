package com.kaleksandra.coredata.network.repository

import com.kaleksandra.coredata.network.Completable
import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.Error
import com.kaleksandra.coredata.network.Success
import com.kaleksandra.coredata.network.api.ProfileApi
import com.kaleksandra.coredata.network.database.DataStoreProvider
import com.kaleksandra.coredata.network.database.SKILLS_KEY
import com.kaleksandra.coredata.network.database.TOKEN_KEY
import com.kaleksandra.coredata.network.di.IoDispatcher
import com.kaleksandra.coredata.network.model.Profile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ProfileRepository {
    suspend fun getProfile(): Effect<Profile>
    suspend fun logOut(): Effect<Completable>
    suspend fun updateSkills(skills: List<String>): Effect<Completable>
    suspend fun getSkills(): Flow<List<String>>
}

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val dataStoreProvider: DataStoreProvider
) : ProfileRepository {
    override suspend fun getProfile(): Effect<Profile> {
        return Success(Profile("Александра"))
        /*return call(ioDispatcher) {
            api.getProfile()
        }*/
    }

    override suspend fun logOut(): Effect<Completable> {
        try {
            dataStoreProvider.remove(TOKEN_KEY)
            return Success(Completable)
        } catch (e: Exception) {
            return Error(e)
        }
    }

    override suspend fun updateSkills(skills: List<String>): Effect<Completable> {
        try {
            withContext(dispatcher) {
                dataStoreProvider.update(
                    SKILLS_KEY,
                    skills.joinToString(",")
                )
            }
            return Success(Completable)
        } catch (e: Exception) {
            return Error(e)
        }
    }

    override suspend fun getSkills(): Flow<List<String>> {
        return dataStoreProvider.get(SKILLS_KEY).map { it?.split(",") ?: emptyList() }
    }
}