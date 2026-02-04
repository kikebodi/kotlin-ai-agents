package com.kikebodi.agents.data

import com.kikebodi.agents.domain.model.Opportunity

interface LlmRepository {
    suspend fun analyze(text: String, maxItems: Int): List<Opportunity>
}
