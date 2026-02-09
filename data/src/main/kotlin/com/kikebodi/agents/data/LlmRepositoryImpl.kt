package com.kikebodi.agents.data

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.structure.executeStructured
import com.kikebodi.agents.data.model.OpportunityList
import com.kikebodi.agents.data.tools.SystemPrompts

class LlmRepositoryImpl : LlmRepository {

    private val executor = simpleOllamaAIExecutor()

    override suspend fun getOpportunitiesFromDogvDocument(documentData: String): OpportunityList {
        val result = executor.executeStructured<OpportunityList>(
            prompt = prompt("dogv-opportunities") {
                system(SystemPrompts.KIKE_BODI_SYSTEM_PROMPT.trimIndent())
                user(documentData)
            },
            model = OllamaModels.Meta.LLAMA_3_2
        )

        val structured = result.getOrElse { throw it }
        return structured.data
    }
}
