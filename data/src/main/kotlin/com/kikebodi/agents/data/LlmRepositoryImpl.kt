package com.kikebodi.agents.data

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.params.LLMParams
import ai.koog.prompt.structure.executeStructured
import com.kikebodi.agents.data.model.OpportunityList
import com.kikebodi.agents.data.tools.SystemPrompts

class LlmRepositoryImpl : LlmRepository {

    override suspend fun getOpportunitiesFromDogvDocument(documentData: String): OpportunityList {
        return useLocalLlm(
            systemPrompt = SystemPrompts.KIKE_BODI_SYSTEM_PROMPT.trimIndent(),
            userPrompt = documentData
        )
    }

    private suspend fun useLocalLlm(systemPrompt: String, userPrompt: String): OpportunityList {
        val executor = simpleOllamaAIExecutor()
        val llmParams = LLMParams(
            temperature = 1.5
        )
        val result = executor.executeStructured<OpportunityList>(
            prompt = prompt("dogv-opportunities", params = llmParams) {
                system(systemPrompt)
                user(userPrompt)
            },
            model = OllamaModels.Meta.LLAMA_3_2,
        )
        val structured = result.getOrElse { throw it }
        return structured.data
    }

    private suspend fun useRemoteLlm(systemPrompt: String, userPrompt: String): OpportunityList {
        val executor = simpleOpenAIExecutor(BuildConfig.OPENAI_API_KEY)
        val result = executor.executeStructured<OpportunityList>(
            prompt = prompt("dogv-opportunities") {
                system(systemPrompt)
                user(userPrompt)
            },
            model = OpenAIModels.Chat.GPT4_1Mini,
        )
        val structured = result.getOrElse { throw it }
        return structured.data
    }
}
