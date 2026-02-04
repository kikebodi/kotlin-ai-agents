package com.kikebodi.agents.data.pdf

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper

class JvmPdfTextExtractor : PdfTextExtractor {
    override suspend fun extractText(bytes: ByteArray): String {
        Loader.loadPDF(bytes).use { document ->
            val stripper = PDFTextStripper()
            return stripper.getText(document)
        }
    }
}
