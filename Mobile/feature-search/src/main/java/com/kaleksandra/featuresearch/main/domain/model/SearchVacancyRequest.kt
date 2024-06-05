package com.kaleksandra.featuresearch.main.domain.model

data class SearchVacancyRequest(
    val text: String? = null,
    val area: String? = null,
    val experience: String? = null,
    val employment: String? = null,
    val schedule: String? = null,
    val onlyWithSalary: Boolean? = null,
)