package com.underdogprotocol.api

import de.jensklingenberg.ktorfit.converter.builtin.CallConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class UnderdogApiV2(
    private val isDevnet: Boolean = false
) {

    private val apiEndpoints: UnderdogEndpoints

    init {
        val ktorfit = ktorfit {
            val prefix = if (isDevnet) "dev" else " api"
            baseUrl("https://$prefix.underdogprotocol.com/v2/")

            httpClient(HttpClient {
                install(ContentNegotiation) {
                    json(Json { isLenient = true; ignoreUnknownKeys = true })
                }
            })

            converterFactories(CallConverterFactory())
        }

        apiEndpoints = ktorfit.create()
    }

    suspend fun mintNft(request: CreateNftRequest, apiKey: String): CreateNftResponse {
        return withContext(Dispatchers.IO) {
            apiEndpoints.createNft("Bearer ${apiKey}", "1", request)
        }
    }
}