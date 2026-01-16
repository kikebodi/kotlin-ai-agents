package com.kikebodi.agents

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.context.AIAgentFunctionalContext
import ai.koog.agents.core.agent.functionalStrategy
import ai.koog.agents.core.dsl.extension.requestLLM
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels

suspend fun main() {

    simpleOllamaAIExecutor().use { executor ->
        val agent = AIAgent<String, Unit>(
            promptExecutor = executor,
            llmModel = OllamaModels.Meta.LLAMA_3_2,
            temperature = 0.7,
            systemPrompt = "You are a helpful assistant.",
            strategy = functionalStrategy {
                var userResponse = it
                while (userResponse != "/bye") {
                    val responses = requestLLM(userResponse)
                    println(responses.content)
                    userResponse = readln()
                }
            },
        )

        println("Start conversation by typing something:")
        val initialMessage = readln()
        agent.run(initialMessage)
    }
}