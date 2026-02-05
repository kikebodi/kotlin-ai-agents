package com.kikebodi.agents.data.tools

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper

/**
 * Extract text from a PDF document.
 * @param bytes The bytes of the PDF document.
 * @return The text extracted from the PDF document.
 *
 * TODO: Find a KMP compliant solution to extract the PDF text
 */

internal class PdfTextExtractorImpl : PdfTextExtractor {
    override suspend fun extractText(bytes: ByteArray): String {
        Loader.loadPDF(bytes).use { document ->
            val stripper = PDFTextStripper()
            return stripper.getText(document)
        }
    }
}
