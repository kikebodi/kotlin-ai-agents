package com.kikebodi.agents.data

import com.kikebodi.agents.data.model.Opportunity
import com.kikebodi.agents.data.model.OpportunityList

interface LlmRepository {
    suspend fun getOpportunitiesFromDogvDocument(documentData: String): OpportunityList
}