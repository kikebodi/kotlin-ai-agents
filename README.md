# Kotlin AI Agents Portfolio

Repo hosting 3 production-ready Kotlin/Koog AI agents for enterprise automation.

## Projects Overview
| Project | Description | Status |
|---------|-------------|--------|
| 1. EU Grant Finder | RAG agent scrapes EU tenders (Horizon, EIC), matches to your company. | MVP skeleton |
| 2. Your own boss | Agentic task orchestrator. Picks task from backlog and creates the schedule of the day taking into account your business goals. | Planned |
| 3. Placeholder | TBD | Planned |

### Tech Stack
- Kotlin + Koog framework agents tools
- Spring Boot server API
- Ollama OpenAI LLMs
- Tools Web scraper EU portals vector DB grants profile matcher APIs

### Quick Setup Run
1. `git clone https://github.com/kikebodi/kotlin-ai-agents`
2. `chmod +x gradlew ./gradlew build`
3. `ollama serve`
4. `ollama pull llama3.2`
5. `./gradlew bootRun`
6. Test: `curl -X POST http localhost 8080 find-grants -H Content-Type application/json -d @example.json`

### Project 1 - EU Grant Finder

RAG agent finds EU grants/tenders matching your profile. Scrapes TED, Horizon Europe, NextGenEU. Scores by grant fit, deadline, eligibility.