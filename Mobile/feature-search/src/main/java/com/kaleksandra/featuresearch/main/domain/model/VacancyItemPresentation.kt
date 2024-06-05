package com.kaleksandra.featuresearch.main.domain.model

data class VacancyItemPresentation(
    val id: String,
    val name: String,
    val description: String,
    val procentage: Int,
    val skills: List<Skill>,
)