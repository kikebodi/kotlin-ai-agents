package com.kikebodi.agents.data.llm

import ai.koog.agents.llm.ChatMessage
import ai.koog.agents.llm.ChatRole
import ai.koog.agents.llm.openai.OpenAiChatModel
import ai.koog.agents.llm.openai.OpenAiClient
import com.kikebodi.agents.data.config.AgentConfig
import com.kikebodi.agents.data.observability.MetricsRegistry
import com.kikebodi.agents.data.observability.NoopMetricsRegistry
import com.kikebodi.agents.data.tools.VectorSearchTool
import com.kikebodi.agents.data.tools.WebSearchTool
import com.kikebodi.agents.domain.model.Opportunity
import com.kikebodi.agents.domain.ports.LlmRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory

class KoogLlmRepository(
    private val config: AgentConfig,
    private val systemPrompt: String,
    private val vectorSearchTool: VectorSearchTool = VectorSearchTool(),
    private val webSearchTool: WebSearchTool = WebSearchTool(),
    private val metricsRegistry: MetricsRegistry = NoopMetricsRegistry
) : LlmRepository {
    private val logger = LoggerFactory.getLogger(KoogLlmRepository::class.java)
    private val json = Json { ignoreUnknownKeys = true }
    private val client = OpenAiClient(
        apiKey = config.openAiApiKey,
        baseUrl = config.openAiBaseUrl
    )
    private val model = OpenAiChatModel(
        client = client,
        model = config.model,
        temperature = config.temperature,
        maxTokens = config.maxTokens
    )

    override suspend fun analyze(text: String, maxItems: Int): List<Opportunity> {
        val seedQuery = text.lineSequence().joinToString(" ").take(300)
        var hints = vectorSearchTool.search(seedQuery, limit = 3)
        if (hints.isEmpty()) {
            webSearchTool.search("DOGV oportunidades IA $seedQuery").forEachIndexed { index, result ->
                vectorSearchTool.upsert("web-$index", result)
            }
            hints = vectorSearchTool.search(seedQuery, limit = 3)
        }
        val contextSnippet = hints.joinToString("\n") { "- ${it.payload}" }
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = systemPrompt),
            ChatMessage(
                role = ChatRole.User,
                content = "\nContexto adicional:\n$contextSnippet\n\nEstructura la siguiente información:\n\n$text"
            )
        )

        val response = model.complete(messages, responseFormat = "json_object")
        val content = response.content ?: "{}"
        val parsed = runCatching {
            val root = json.parseToJsonElement(content)
            val array = root.jsonObject["items"]?.jsonArray ?: root.jsonArray
            array.map { json.decodeFromJsonElement<Opportunity>(it) }
        }.getOrElse {
            logger.warn("Failed to parse LLM response: ${it.message}")
            emptyList()
        }

        val sorted = parsed.sortedByDescending { it.score }.take(maxItems)
        metricsRegistry.incrementCounter("llm.analysis.completed")
        return sorted
    }
}
