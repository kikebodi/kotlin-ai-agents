package com.kikebodi.agents.domain.usecase

import com.kikebodi.agents.domain.model.AgentRequest
import com.kikebodi.agents.domain.model.AgentResponse
import com.kikebodi.agents.domain.ports.DogvRepository
import com.kikebodi.agents.domain.ports.LlmRepository

internal class AnalyzeDogvUseCaseImpl(
    private val dogvRepository: DogvRepository,
    private val llmRepository: LlmRepository
): AnalyzeDogvUseCase {
    override suspend fun execute(request: AgentRequest): AgentResponse {
        val url = dogvRepository.findTodayPdfUrl(request.language)
        if (url == null) {
            return AgentResponse(sourceUrl = null, items = emptyList())
        }
        val text = dogvRepository.fetchPdfText(url)
        val items = llmRepository.analyze(text, request.maxItems)
            return AgentResponse(sourceUrl = url, items = items)
        }
}
