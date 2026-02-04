package com.kikebodi.agents.data.agent

import com.kikebodi.agents.domain.model.AgentRequest
import com.kikebodi.agents.domain.model.AgentResponse
import com.kikebodi.agents.domain.usecase.AnalyzeDogvUseCase

class DogvAgent(
    private val useCase: AnalyzeDogvUseCase
) {
    suspend fun run(request: AgentRequest): AgentResponse {
        return useCase.execute(request)
    }
}
