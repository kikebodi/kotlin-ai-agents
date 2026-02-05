package com.kikebodi.agents.domain

import com.kikebodi.agents.data.dogv.DogvRepositoryImpl
import com.kikebodi.agents.domain.model.AgentResponse
import com.kikebodi.agents.data.dogv.DogvRepository
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrl
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrlImpl
import kotlinx.serialization.json.Json

class DogvAiAgentImpl: DogvAIAgent {

    private val calculateDogvTodayUrl: CalculateDogvTodayUrl = CalculateDogvTodayUrlImpl()
    private val dogvRepository: DogvRepository = DogvRepositoryImpl()


    // Send email
    override suspend fun run(): String {
        // Get URL
        val url = calculateDogvTodayUrl.getDogvUrl()
        // Retrieve data from URL
        val pdfContent = dogvRepository.fetchPdfText(url)
        // Agent work
        // Print response - for testing
        val response = AgentResponse(items = emptyList())
        val outputJson = Json { prettyPrint = true }.encodeToString(
            AgentResponse.serializer(),
            response
        )
        println(outputJson)
        return outputJson
    }
}