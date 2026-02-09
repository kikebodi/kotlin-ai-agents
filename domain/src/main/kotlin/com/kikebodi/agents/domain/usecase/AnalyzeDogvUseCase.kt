package com.kikebodi.agents.domain.usecase

import com.kikebodi.agents.data.model.AgentRequest
import com.kikebodi.agents.data.model.AgentResponse

interface AnalyzeDogvUseCase {
    suspend fun execute(request: AgentRequest): AgentResponse
}