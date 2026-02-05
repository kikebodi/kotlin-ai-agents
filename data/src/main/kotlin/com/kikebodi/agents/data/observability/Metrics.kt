package com.kikebodi.agents.data.observability

// TODO: Decide lates if to keep this
interface MetricsRegistry {
    fun incrementCounter(name: String, tags: Map<String, String> = emptyMap())
    fun recordTimer(name: String, durationMs: Long, tags: Map<String, String> = emptyMap())
}

object NoopMetricsRegistry : MetricsRegistry {
    override fun incrementCounter(name: String, tags: Map<String, String>) = Unit
    override fun recordTimer(name: String, durationMs: Long, tags: Map<String, String>) = Unit
}
