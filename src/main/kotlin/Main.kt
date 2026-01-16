package com.kikebodi.agents

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    // Create an agent
    val agent = AIAgent(
        promptExecutor = simpleOllamaAIExecutor(),
        llmModel = OllamaModels.Meta.LLAMA_3_2
    )

    // Run the agent
    val result = agent.run("Hello! How can you help me?")
    println(result)
}