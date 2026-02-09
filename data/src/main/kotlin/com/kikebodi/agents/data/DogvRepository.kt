package com.kikebodi.agents.data

interface DogvRepository {
    suspend fun pingUrl(url: String): Boolean
    suspend fun fetchPdfText(url: String): String
}