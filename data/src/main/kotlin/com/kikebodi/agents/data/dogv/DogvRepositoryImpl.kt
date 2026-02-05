package com.kikebodi.agents.data.dogv

import com.kikebodi.agents.data.observability.MetricsRegistry
import com.kikebodi.agents.data.observability.NoopMetricsRegistry
import com.kikebodi.agents.data.tools.PdfTextExtractor
import com.kikebodi.agents.data.tools.PdfTextExtractorImpl
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

class DogvRepositoryImpl(
    private val pdfTextExtractor: PdfTextExtractor = PdfTextExtractorImpl(),
    private val metricsRegistry: MetricsRegistry = NoopMetricsRegistry
) : DogvRepository {
    private val logger = KotlinLogging.logger {}

    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                // TODO: Remove insecure SSL mode once proper truststore/proxy setup is in place.
                val trustManager = trustAllTrustManager()
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf(trustManager), SecureRandom())
                sslSocketFactory(sslContext.socketFactory, trustManager)
                hostnameVerifier { _, _ -> true }
                logger.warn { "DOGV_INSECURE_SSL enabled. TLS verification is disabled." }
            }
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
    }

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

    private fun loadTrustManager(trustStorePath: String, trustStorePassword: String): X509TrustManager {
        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
        FileInputStream(trustStorePath).use { stream ->
            trustStore.load(stream, trustStorePassword.toCharArray())
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(trustStore)
        return tmf.trustManagers.filterIsInstance<X509TrustManager>().firstOrNull()
            ?: error("No X509TrustManager found in truststore: $trustStorePath")
    }

    private fun trustAllTrustManager(): X509TrustManager =
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        }
}
