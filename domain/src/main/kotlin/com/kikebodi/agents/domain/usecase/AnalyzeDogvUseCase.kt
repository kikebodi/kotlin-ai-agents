package com.kikebodi.agents.domain.usecase

import com.kikebodi.agents.domain.model.AgentRequest
import com.kikebodi.agents.domain.model.AgentResponse

interface AnalyzeDogvUseCase {
    suspend fun execute(request: AgentRequest): AgentResponse
}