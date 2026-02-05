package com.kikebodi.agents.data.dogv

interface DogvRepository {
    suspend fun pingUrl(url: String): Boolean
    suspend fun fetchPdfText(url: String): String
}