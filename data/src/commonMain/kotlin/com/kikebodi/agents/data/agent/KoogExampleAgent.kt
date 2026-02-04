package com.kikebodi.agents.data.agent

import ai.koog.agents.llm.ChatMessage
import ai.koog.agents.llm.ChatRole
import ai.koog.agents.llm.openai.OpenAiChatModel
import ai.koog.agents.llm.openai.OpenAiClient
import com.kikebodi.agents.data.config.AgentConfig

class KoogExampleAgent(
    private val config: AgentConfig
) {
    private val client = OpenAiClient(
        apiKey = config.openAiApiKey,
        baseUrl = config.openAiBaseUrl
    )
    private val model = OpenAiChatModel(
        client = client,
        model = config.model,
        temperature = config.temperature,
        maxTokens = config.maxTokens
    )

    suspend fun run(prompt: String): String {
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = "Eres un asistente útil."),
            ChatMessage(role = ChatRole.User, content = prompt)
        )
        return model.complete(messages).content.orEmpty()
    }
}
