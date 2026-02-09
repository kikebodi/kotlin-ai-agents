package com.kikebodi.agents.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OpportunityList")
data class OpportunityList(
    val items: List<Opportunity>
)

@Serializable
@SerialName("Opportunity")
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
@SerialName("OpportunityCategory")
enum class OpportunityCategory {
    AYUDA_SUBVENCION,
    LICITACION_CONTRATO,
    PROGRAMA_CONVOCATORIA,
    OTROS
}
