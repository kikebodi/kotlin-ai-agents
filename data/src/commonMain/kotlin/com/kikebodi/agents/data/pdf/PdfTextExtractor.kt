package com.kikebodi.agents.data.pdf

interface PdfTextExtractor {
    suspend fun extractText(bytes: ByteArray): String
}
