package com.kikebodi.agents.app

import com.kikebodi.agents.domain.model.AgentResponse
import com.kikebodi.agents.domain.usecase.AnalyzeDogvUseCase
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrl
import com.kikebodi.agents.domain.usecase.CalculateDogvTodayUrlImpl
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {

    val calculateDogvTodayUrl: CalculateDogvTodayUrl = CalculateDogvTodayUrlImpl()

    // Get URL
    val url = calculateDogvTodayUrl.getDogvUrl()
    // Retrieve data from URL
    // Agent work
    // Print response - for testing
    val response = AgentResponse(items = emptyList())
    val outputJson = Json { prettyPrint = true }.encodeToString(
        com.kikebodi.agents.domain.model.AgentResponse.serializer(),
        response
    )
    println(outputJson)
    // Send email
}
