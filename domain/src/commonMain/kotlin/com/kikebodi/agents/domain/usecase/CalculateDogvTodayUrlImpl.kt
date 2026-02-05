package com.kikebodi.agents.domain.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalculateDogvTodayUrlImpl: CalculateDogvTodayUrl {

    private val getDogvIndexUseCase: GetDogvIndexUseCase = GetDogvIndexUseCaseImpl()

    override fun getDogvUrl(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val year = today.year
        val month = today.monthNumber.toString().padStart(2, '0')
        val day = today.dayOfMonth.toString().padStart(2, '0')
        val index = getDogvIndexUseCase.getIndex(year, month.toInt(), day.toInt())
        val url = "https://dogv.gva.es/datos/$year/$month/$day/pdf/sumario_${year}_${index}_${LANGUAGE}.pdf"
        return url
    }

    companion object{
        const val LANGUAGE = "es"
    }
}