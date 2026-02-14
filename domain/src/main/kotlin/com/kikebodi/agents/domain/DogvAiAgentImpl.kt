package com.kikebodi.agents.domain

import com.kikebodi.agents.data.model.AgentResponse
import com.kikebodi.agents.data.DogvRepository
import com.kikebodi.agents.data.DogvRepositoryImpl
import com.kikebodi.agents.data.LlmRepository
import com.kikebodi.agents.data.LlmRepositoryImpl
import com.kikebodi.agents.data.model.Opportunity
import com.kikebodi.agents.data.model.OpportunityList
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrl
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrlImpl
import kotlinx.serialization.json.Json

class DogvAiAgentImpl: DogvAIAgent {

    private val calculateDogvTodayUrl: CalculateDogvTodayUrl = CalculateDogvTodayUrlImpl()
    private val dogvRepository: DogvRepository = DogvRepositoryImpl()
    private val llmRepository: LlmRepository = LlmRepositoryImpl()


    // Send email
    override suspend fun run(): String {
        // Get URL
        val url = calculateDogvTodayUrl.getDogvUrl()
        // Retrieve data from URL
        val pdfContent = dogvRepository.fetchPdfText(url)
        // Agent work
        val opportunityList = llmRepository.getOpportunitiesFromDogvDocument(pdfContent)
        // Print response - for testing
        val outputJson = Json {
            explicitNulls = true
            encodeDefaults = true
        }.encodeToString(
            OpportunityList.serializer(),
            opportunityList
        )
        return outputJson
    }
}