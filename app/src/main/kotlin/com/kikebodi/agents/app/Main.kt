package com.kikebodi.agents.app

import com.kikebodi.agents.domain.DogvAIAgent
import com.kikebodi.agents.domain.DogvAiAgentImpl
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {

    val agent: DogvAIAgent = DogvAiAgentImpl()
    runBlocking { agent.run() }
}
