package com.kikebodi.agents.data.dogv

import com.kikebodi.agents.data.observability.MetricsRegistry
import com.kikebodi.agents.data.observability.NoopMetricsRegistry
import com.kikebodi.agents.data.tools.PdfTextExtractor
import com.kikebodi.agents.data.DogvRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Clock

class DogvRepositoryImpl(
    private val httpClient: HttpClient,
    private val pdfTextExtractor: PdfTextExtractor,
    private val metricsRegistry: MetricsRegistry = NoopMetricsRegistry
) : DogvRepository {
    private val logger = KotlinLogging.logger {}

    override suspend fun pingUrl(url: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPdfText(url: String): String {
        val start = Clock.System.now().toEpochMilliseconds()
        val response: HttpResponse = httpClient.get(url)
        if (response.status != HttpStatusCode.OK) {
            error("Failed to fetch DOGV PDF: ${response.status}")
        }
        val bytes = response.body<ByteArray>()
        val text = pdfTextExtractor.extractText(bytes)
        val duration = Clock.System.now().toEpochMilliseconds() - start
        metricsRegistry.recordTimer("dogv.pdf.extract", duration)
        return text
    }
}
