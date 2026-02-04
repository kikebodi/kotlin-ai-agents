package com.kikebodi.agents.data.dogv

import com.kikebodi.agents.data.observability.MetricsRegistry
import com.kikebodi.agents.data.observability.NoopMetricsRegistry
import com.kikebodi.agents.data.pdf.PdfTextExtractor
import com.kikebodi.agents.domain.ports.DogvRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory

class DogvRepositoryImpl(
    private val httpClient: HttpClient,
    private val pdfTextExtractor: PdfTextExtractor,
    private val metricsRegistry: MetricsRegistry = NoopMetricsRegistry
) : DogvRepository {
    private val logger = LoggerFactory.getLogger(DogvRepositoryImpl::class.java)

    override suspend fun findTodayPdfUrl(language: String): String? {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val year = today.year
        val month = today.monthNumber.toString().padStart(2, '0')
        val day = today.dayOfMonth.toString().padStart(2, '0')

        for (index in 10200..10300) {
            val url = "https://dogv.gva.es/datos/$year/$month/$day/pdf/sumario_${year}_${index}_${language}.pdf"
            val response = httpClient.head(url)
            if (response.status.isSuccess()) {
                logger.info("DOGV PDF found at $url")
                return url
            }
            delay(50)
        }
        logger.warn("No DOGV PDF found for $year-$month-$day")
        return null
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
