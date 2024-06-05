package com.kaleksandra.coredata.network.model

data class VacancyItem(
    val id: String,
    val name: String,
    val description: String,
    val skills: List<String>,
)