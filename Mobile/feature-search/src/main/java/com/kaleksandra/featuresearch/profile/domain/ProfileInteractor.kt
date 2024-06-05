package com.kaleksandra.featuresearch.profile.domain

import com.kaleksandra.coredata.network.Completable
import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.model.Profile
import com.kaleksandra.coredata.network.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProfileInteractor {
    suspend fun getProfile(): Effect<Profile>
    suspend fun logOut(): Effect<Completable>
    suspend fun updateSkills(skills: List<String>): Effect<Completable>
    suspend fun getSkills(): Flow<List<String>>
}

class ProfileInteractorImpl @Inject constructor(
    private val repository: ProfileRepository
) : ProfileInteractor {
    override suspend fun getProfile(): Effect<Profile> {
        return repository.getProfile()
    }

    override suspend fun logOut(): Effect<Completable> {
        return repository.logOut()
    }

    override suspend fun updateSkills(skills: List<String>): Effect<Completable> {
        return repository.updateSkills(skills)
    }

    override suspend fun getSkills(): Flow<List<String>> {
        return repository.getSkills()
    }
}