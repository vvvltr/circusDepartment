package com.kaleksandra.featuresearch.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.corecommon.ext.debug
import com.kaleksandra.coredata.network.doOnError
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.model.Area
import com.kaleksandra.featuresearch.main.domain.SearchInteractor
import com.kaleksandra.featuresearch.main.domain.model.SearchVacancyRequest
import com.kaleksandra.featuresearch.main.domain.model.Skill
import com.kaleksandra.featuresearch.main.domain.model.VacancyItemPresentation
import com.kaleksandra.featuresearch.profile.domain.ProfileInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_DEBOUNCE_DELAY: Long = 300L

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val profileInteractor: ProfileInteractor,
) : ViewModel() {

    private val _cityInput: MutableStateFlow<String> = MutableStateFlow("")
    val cities: MutableStateFlow<List<Area>> = MutableStateFlow(emptyList())
    val vacancies: MutableStateFlow<List<VacancyItemPresentation>> = MutableStateFlow(emptyList())
    val findedSkills: MutableStateFlow<List<Skill>> = MutableStateFlow(emptyList())
    val mySkills: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    init {
        _cityInput.debounce(DEFAULT_DEBOUNCE_DELAY)
            .distinctUntilChanged()
            .onEach {
                searchInteractor.searchCity(it).doOnSuccess { cities ->
                    this.cities.emit(cities)
                }.doOnError {
                    debug(it)
                }
            }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            profileInteractor.getSkills().distinctUntilChanged()
                .onEach { mySkills.emit(it.map { it.lowercase() }) }
                .launchIn(viewModelScope)
        }
    }

    fun searchCity(city: String) {
        viewModelScope.launch {
            _cityInput.emit(city)
        }
    }

    fun searchVacancy(value: SearchVacancyRequest) {
        viewModelScope.launch {
            searchInteractor.searchVacancy(value.toQueryMap())
                .doOnSuccess {
                    vacancies.emit(
                        it.items.map {
                            VacancyItemPresentation(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                procentage = getProcentage(
                                    mySkills.value,
                                    it.skills
                                ),
                                skills = it.skills.map { name ->
                                    Skill(
                                        name,
                                        name.lowercase() in mySkills.value
                                    )
                                }
                            )
                        }.sortedByDescending { it.procentage }
                    )
                    findedSkills.emit(it.skills.map { name ->
                        Skill(
                            name,
                            name in mySkills.value
                        )
                    })
                }.doOnError {
                    debug("error")
                }
        }
    }

    private fun getProcentage(mySkills: List<String>, skills: List<String>): Int {
        val count = skills.count { mySkills.contains(it.lowercase()) }
        return (count.toDouble() / skills.size * 100).toInt()
    }

    private fun SearchVacancyRequest.toQueryMap(): Map<String, String> {
        return mapOf(
            "area" to area,
            "text" to text,
            "experience" to experience,
            "employment" to employment,
            "schedule" to schedule,
            "only_with_salary" to onlyWithSalary.toString(),
        ).filterValues { it != null }.mapValues { it.value!! }
    }
}