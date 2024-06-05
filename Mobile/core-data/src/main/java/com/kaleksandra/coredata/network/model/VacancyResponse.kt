package com.kaleksandra.coredata.network.model

data class VacancyResponse(
    val items: List<VacancyItem>,
    val skills: List<String>,
)