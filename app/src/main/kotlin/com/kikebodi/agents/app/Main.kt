package com.kikebodi.agents.app

import com.kikebodi.agents.data.agent.DogvAgent
import com.kikebodi.agents.data.config.AgentConfig
import com.kikebodi.agents.data.dogv.DogvRepositoryImpl
import com.kikebodi.agents.data.llm.KoogLlmRepository
import com.kikebodi.agents.data.tools.PdfTextExtractorImpl
import com.kikebodi.agents.domain.model.AgentRequest
import com.kikebodi.agents.domain.usecase.AnalyzeDogvUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.InputStream

fun main(args: Array<String>) {
    val parser = ArgParser("dogv-agent")
    val language by parser.option(ArgType.String, shortName = "l", description = "Language code").default("es")
    val maxItems by parser.option(ArgType.Int, shortName = "m", description = "Max items").default(15)
    parser.parse(args)

    val apiKey = System.getenv("OPENAI_API_KEY") ?: error("OPENAI_API_KEY not set")
    val baseUrl = System.getenv("OPENAI_BASE_URL") ?: "https://api.openai.com/v1"
    val model = System.getenv("OPENAI_MODEL") ?: "gpt-4o-mini"
    val config = AgentConfig(openAiApiKey = apiKey, openAiBaseUrl = baseUrl, model = model)

    val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    val systemPromptStream: InputStream = requireNotNull(
        Thread.currentThread().contextClassLoader.getResourceAsStream("system_prompt.txt")
    ) { "system_prompt.txt resource not found" }
    val systemPrompt = systemPromptStream.bufferedReader().use { it.readText() }
    val dogvRepository = DogvRepositoryImpl(httpClient, PdfTextExtractorImpl())
    val llmRepository = KoogLlmRepository(config, systemPrompt)
    val useCase = AnalyzeDogvUseCaseImpl(dogvRepository, llmRepository)
    val agent = DogvAgent(useCase)

    runBlocking {
        val response = agent.run(AgentRequest(language = language, maxItems = maxItems))
        val outputJson = Json { prettyPrint = true }.encodeToString(
            com.kikebodi.agents.domain.model.AgentResponse.serializer(),
            response
        )
        println(outputJson)
    }
}
