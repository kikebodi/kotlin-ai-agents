package com.kikebodi.agents.domain.ports

import com.kikebodi.agents.domain.model.Opportunity

interface LlmRepository {
    suspend fun analyze(text: String, maxItems: Int): List<Opportunity>
}
