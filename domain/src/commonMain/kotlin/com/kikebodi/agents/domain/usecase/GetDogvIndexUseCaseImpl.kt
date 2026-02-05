package com.kikebodi.agents.domain.usecase

import kotlinx.datetime.*

internal class GetDogvIndexUseCaseImpl : GetDogvIndexUseCase {

    override fun getIndex(year: Int, month: Int, day: Int): Int {
        val targetDate = LocalDate(year, month, day)

        require(targetDate >= REFERENCE_DATE) {
            "Target date must be >= reference date"
        }

        val delta = countWorkingDays(
            fromExclusive = REFERENCE_DATE,
            toInclusive = targetDate
        )

        return REFERENCE_INDEX + delta
    }

    private fun countWorkingDays(
        fromExclusive: LocalDate,
        toInclusive: LocalDate
    ): Int {
        var count = 0
        var date = fromExclusive.plus(1, DateTimeUnit.DAY)

        while (date <= toInclusive) {
            if (isWorkingDay(date)) count++
            date = date.plus(1, DateTimeUnit.DAY)
        }
        return count
    }

    private fun isWorkingDay(date: LocalDate): Boolean {
        val dow = date.dayOfWeek
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) return false
        if (date in HOLIDAYS_2026) return false
        return true
    }

    companion object{
        private val HOLIDAYS_2026 = setOf(
            LocalDate(2026, 1, 1),   // Año Nuevo
            LocalDate(2026, 1, 6),   // Epifanía del Señor (Reyes)
            LocalDate(2026, 3, 19),  // San José
            LocalDate(2026, 4, 3),   // Viernes Santo
            LocalDate(2026, 4, 6),   // Lunes de Pascua
            LocalDate(2026, 5, 1),   // Fiesta del Trabajo
            LocalDate(2026, 6, 24),  // San Juan
            LocalDate(2026, 8, 15),  // Asunción de la Virgen
            LocalDate(2026, 10, 9),  // Día de la Comunitat Valenciana
            LocalDate(2026, 10, 12), // Fiesta Nacional de España
            LocalDate(2026, 12, 8),  // Inmaculada Concepción
            LocalDate(2026, 12, 25)  // Natividad del Señor
        )
        private val REFERENCE_DATE = LocalDate(2026, 2, 5)
        private const val REFERENCE_INDEX = 10296
    }
}
