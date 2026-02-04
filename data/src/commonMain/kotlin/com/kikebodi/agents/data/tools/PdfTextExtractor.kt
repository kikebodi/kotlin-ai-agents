package com.kikebodi.agents.data.tools

interface PdfTextExtractor {
    suspend fun extractText(bytes: ByteArray): String
}
