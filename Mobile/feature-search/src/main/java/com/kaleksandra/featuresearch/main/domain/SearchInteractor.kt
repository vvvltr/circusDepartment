package com.kaleksandra.featuresearch.main.domain

import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.model.Area
import com.kaleksandra.coredata.network.model.VacancyResponse
import com.kaleksandra.coredata.network.repository.SearchRepository
import javax.inject.Inject

interface SearchInteractor {
    suspend fun searchCity(query: String): Effect<List<Area>>
    suspend fun searchVacancy(query: Map<String, String>): Effect<VacancyResponse>
}

class SearchInteractorImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : SearchInteractor {
    override suspend fun searchCity(query: String): Effect<List<Area>> {
        return searchRepository.searchCity(query)
    }

    override suspend fun searchVacancy(query: Map<String, String>): Effect<VacancyResponse> {
        return searchRepository.searchVacancy(query)
    }
}