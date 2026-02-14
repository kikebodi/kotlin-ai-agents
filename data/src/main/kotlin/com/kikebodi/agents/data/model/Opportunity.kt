package com.kikebodi.agents.data.model

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OpportunityList")
data class OpportunityList(
    @property:LLMDescription(
        "Lista de oportunidades detectadas. "
                + "Debe ser un array JSON. "
                + "Si no hay oportunidades relevantes, debe ser una lista vacía."
    )
    val items: List<Opportunity>
)

@Serializable
@SerialName("Opportunity")
data class Opportunity(

    @property:LLMDescription(
        "Categoría de la oportunidad. "
                + "Debe ser una de: AYUDA_SUBVENCION, LICITACION_CONTRATO, PROGRAMA_CONVOCATORIA u OTROS."
    )
    val category: OpportunityCategory,

    @property:LLMDescription(
        "Título oficial o denominación más representativa de la oportunidad, "
                + "extraído directamente del boletín."
    )
    val title: String,

    @property:LLMDescription(
        "Entidad pública convocante o responsable "
                + "(conselleria, universidad, ayuntamiento, organismo público, etc.)."
    )
    val entity: String,

    @property:LLMDescription(
        "Resumen breve y factual (1–2 frases) describiendo la oportunidad. "
                + "No especular ni añadir información no presente en el texto."
    )
    val summary: String,

    @property:LLMDescription(
        "Puntuación de relevancia entre 0 y 100. "
                + "Debe reflejar el encaje con servicios de TI, software, IA o digitalización."
    )
    val score: Int,

    @property:LLMDescription(
        "URL directa a la publicación oficial. "
                + "OBLIGATORIA. "
                + "Debe derivarse del identificador oficial (DOGV, BOE, BDNS, etc.) si no aparece explícitamente. "
                + "Nunca puede ser una cadena vacía."
    )
    val url: String,

    @property:LLMDescription(
        "Presupuesto o importe económico asociado a la oportunidad, "
                + "si aparece explícitamente en el texto. "
                + "Usar null si no se indica."
    )
    val budget: String? = null,

    @property:LLMDescription(
        "Fecha límite de presentación o plazo relevante. "
                + "Usar formato ISO YYYY-MM-DD cuando sea posible. "
                + "Usar null si no se especifica."
    )
    val deadline: String? = null,

    @property:LLMDescription(
        "Referencia oficial o identificador del boletín "
                + "(por ejemplo DOGV-C-YYYY-NNNNN, YYYY/NNNNN, BOE-A-YYYY-NNNNN, BDNS). "
                + "Usar null si no existe."
    )
    val reference: String? = null,

    @property:LLMDescription(
        "Lista de palabras o expresiones clave encontradas literalmente en el texto "
                + "que justifican la relevancia de la oportunidad."
    )
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
