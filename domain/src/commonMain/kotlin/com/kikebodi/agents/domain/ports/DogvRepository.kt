package com.kikebodi.agents.domain.ports

interface DogvRepository {
    suspend fun findTodayPdfUrl(language: String): String?
    suspend fun fetchPdfText(url: String): String
}
