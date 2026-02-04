package com.kikebodi.agents.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Opportunity(
    val category: OpportunityCategory,
    val title: String,
    val entity: String,
    val summary: String,
    val budget: String? = null,
    val deadline: String? = null,
    val reference: String? = null,
    val url: String? = null,
    val score: Int,
    val matchedKeywords: List<String> = emptyList()
)

@Serializable
enum class OpportunityCategory {
    AYUDA_SUBVENCION,
    LICITACION_CONTRATO,
    PROGRAMA_CONVOCATORIA,
    OTROS
}
