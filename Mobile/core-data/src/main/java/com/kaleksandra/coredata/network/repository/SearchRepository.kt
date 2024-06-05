package com.kaleksandra.coredata.network.repository

import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.api.SearchApi
import com.kaleksandra.coredata.network.api.VacancyApi
import com.kaleksandra.coredata.network.call
import com.kaleksandra.coredata.network.di.IoDispatcher
import com.kaleksandra.coredata.network.map
import com.kaleksandra.coredata.network.model.Area
import com.kaleksandra.coredata.network.model.VacancyResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface SearchRepository {
    suspend fun searchCity(query: String): Effect<List<Area>>
    suspend fun searchVacancy(query: Map<String, String>): Effect<VacancyResponse>
}

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
    private val vacancyApi: VacancyApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : SearchRepository {
    override suspend fun searchCity(query: String): Effect<List<Area>> {
        return call(dispatcher) {
            api.getAllCitiesRussia()
        }.map { area -> area?.let { _area -> getAllAreas(query, _area) } ?: emptyList() }
    }

    override suspend fun searchVacancy(query: Map<String, String>): Effect<VacancyResponse> {
        return call(dispatcher) {
            vacancyApi.searchVacancy(query)
        }
    }

    private fun getAllAreas(query: String, area: Area): List<Area> {
        val allAreas = mutableListOf<Area>()
        if (area.name.contains(query, ignoreCase = true)) {
            allAreas.add(area)
        }
        area.areas.forEach { subArea ->
            val areas = getAllAreas(query, subArea)
            areas.forEach {
                if (it.name.contains(query, ignoreCase = true)) {
                    allAreas.add(it)
                }
            }
        }
        return allAreas
    }
}