package com.kikebodi.agents.domain.usecase

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class GetDogvIndexUseCaseTest {

    private val SUT: GetDogvIndexUseCase = GetDogvIndexUseCaseImpl()

    @Test
    fun `reference date returns reference index`() {
        val index = SUT.getIndex(2026, 2, 5)

        assertEquals(10296, index)
    }

    @Test
    fun `next working day increases index by one`() {
        // 2026-02-06 is Friday
        val index = SUT.getIndex(2026, 2, 6)

        assertEquals(10297, index)
    }

    @Test
    fun `weekend does not increase index`() {
        // 2026-02-07 is Saturday
        val fridayIndex = SUT.getIndex(2026, 2, 6)
        val saturdayIndex = SUT.getIndex(2026, 2, 7)
        val sundayIndex = SUT.getIndex(2026, 2, 8)

        assertEquals(fridayIndex, saturdayIndex)
        assertEquals(fridayIndex, sundayIndex)
    }

    @Test
    fun `holiday does not increase index`() {
        // 2026-03-19 is San José (holiday)
        val previousDayIndex = SUT.getIndex(2026, 3, 18)
        val holidayIndex = SUT.getIndex(2026, 3, 19)

        assertEquals(previousDayIndex, holidayIndex)
    }

    @Test
    fun `working day after holiday increases index`() {
        val holidayIndex = SUT.getIndex(2026, 3, 19)
        val nextDayIndex = SUT.getIndex(2026, 3, 20)

        assertEquals(holidayIndex + 1, nextDayIndex)
    }

    @Test
    fun `range with weekend and holiday counts correctly`() {
        // From reference (Thu 5 Feb 2026)
        // Fri 6  -> +1
        // Sat 7  -> +0
        // Sun 8  -> +0
        // Mon 9  -> +1
        val index = SUT.getIndex(2026, 2, 9)

        assertEquals(10298, index)
    }

    @Test
    fun `date before reference throws`() {
        assertFailsWith<IllegalArgumentException> {
            SUT.getIndex(2026, 2, 4)
        }
    }
}
