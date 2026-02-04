package com.kikebodi.agents.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AgentRequest(
    val language: String = "es",
    val targetDateIso: String? = null,
    val maxItems: Int = 20
)

@Serializable
data class AgentResponse(
    val sourceUrl: String?,
    val items: List<Opportunity>
)
