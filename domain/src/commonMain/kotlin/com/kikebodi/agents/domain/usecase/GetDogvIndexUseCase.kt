package com.kikebodi.agents.domain.usecase

internal interface GetDogvIndexUseCase {
    fun getIndex(year: Int, month: Int, day: Int): Int
}