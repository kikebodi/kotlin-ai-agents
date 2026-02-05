package com.kikebodi.agents.data.config

import kotlinx.serialization.Serializable

@Serializable
data class AgentConfig(
    val openAiApiKey: String,
    val openAiBaseUrl: String = "https://api.openai.com/v1",
    val model: String = "gpt-4o-mini",
    val maxTokens: Int = 2048,
    val temperature: Double = 0.2
)
