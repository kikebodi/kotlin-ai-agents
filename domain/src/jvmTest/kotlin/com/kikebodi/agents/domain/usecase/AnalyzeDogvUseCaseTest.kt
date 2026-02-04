package com.kikebodi.agents.domain.usecase

import com.kikebodi.agents.domain.model.AgentRequest
import com.kikebodi.agents.domain.model.Opportunity
import com.kikebodi.agents.domain.model.OpportunityCategory
import com.kikebodi.agents.domain.ports.DogvRepository
import com.kikebodi.agents.domain.ports.LlmRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AnalyzeDogvUseCaseTest : StringSpec({
    "returns empty response when no pdf is available" {
        val useCase = AnalyzeDogvUseCaseImpl(
            dogvRepository = object : DogvRepository {
                override suspend fun findTodayPdfUrl(language: String) = null
                override suspend fun fetchPdfText(url: String) = ""
            },
            llmRepository = object : LlmRepository {
                override suspend fun analyze(text: String, maxItems: Int) = emptyList<Opportunity>()
            }
        )

        val response = useCase.execute(AgentRequest())

        response.sourceUrl shouldBe null
        response.items shouldBe emptyList()
    }

    "returns opportunities when pdf exists" {
        val useCase = AnalyzeDogvUseCaseImpl(
            dogvRepository = object : DogvRepository {
                override suspend fun findTodayPdfUrl(language: String) = "https://example.com/dogv.pdf"
                override suspend fun fetchPdfText(url: String) = "some text"
            },
            llmRepository = object : LlmRepository {
                override suspend fun analyze(text: String, maxItems: Int) = listOf(
                    Opportunity(
                        category = OpportunityCategory.PROGRAMA_CONVOCATORIA,
                        title = "Convocatoria IA",
                        entity = "Generalitat",
                        summary = "Resumen",
                        score = 90
                    )
                )
            }
        )

        val response = useCase.execute(AgentRequest())

        response.sourceUrl shouldBe "https://example.com/dogv.pdf"
        response.items.size shouldBe 1
    }
})
