package com.kikebodi.agents.data.observability

interface MetricsRegistry {
    fun incrementCounter(name: String, tags: Map<String, String> = emptyMap())
    fun recordTimer(name: String, durationMs: Long, tags: Map<String, String> = emptyMap())
}

object NoopMetricsRegistry : MetricsRegistry {
    override fun incrementCounter(name: String, tags: Map<String, String>) = Unit
    override fun recordTimer(name: String, durationMs: Long, tags: Map<String, String>) = Unit
}
