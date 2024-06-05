package com.kaleksandra.coredata.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Area(
    val areas: List<Area>,
    val id: String,
    val name: String,
    @SerialName("parent_id")
    val parentId: String?
)