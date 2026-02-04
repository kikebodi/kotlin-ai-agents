package com.kikebodi.agents.data.tools

data class VectorSearchResult(
    val id: String,
    val score: Double,
    val payload: String
)

class VectorSearchTool {
    private val store = mutableMapOf<String, String>()

    fun upsert(id: String, text: String) {
        store[id] = text
    }

    fun search(query: String, limit: Int = 5): List<VectorSearchResult> {
        if (store.isEmpty()) return emptyList()
        return store.entries
            .map { entry ->
                val score = if (entry.value.contains(query, ignoreCase = true)) 0.9 else 0.2
                VectorSearchResult(entry.key, score, entry.value)
            }
            .sortedByDescending { it.score }
            .take(limit)
    }
}
