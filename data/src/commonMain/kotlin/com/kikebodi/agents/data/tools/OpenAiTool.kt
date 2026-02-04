package com.kikebodi.agents.data.tools

import com.kikebodi.agents.domain.model.Opportunity
import com.kikebodi.agents.domain.ports.LlmRepository

class OpenAiTool(
    private val llmRepository: LlmRepository
) {
    suspend fun analyze(text: String, maxItems: Int): List<Opportunity> {
        return llmRepository.analyze(text, maxItems)
    }
}
