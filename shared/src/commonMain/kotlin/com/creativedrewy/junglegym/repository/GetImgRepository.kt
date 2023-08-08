package com.creativedrewy.junglegym.repository

import com.creativedrewy.junglegym.ApiKeys
import com.creativedrewy.junglegym.endpoint.GetImgEndpoints
import com.creativedrewy.junglegym.endpoint.GetImgRequest
import com.moriatsushi.koject.Provides
import de.jensklingenberg.ktorfit.converter.builtin.CallConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Provides
class GetImgRepository {

    private val getImgEndpoints: GetImgEndpoints

    init {
        val ktorfit = ktorfit {
            baseUrl("https://api.getimg.ai/v1/")

            httpClient(HttpClient {
                install(ContentNegotiation) {
                    json(Json { isLenient = true; ignoreUnknownKeys = true })
                }
            })

            converterFactories(
                CallConverterFactory()
            )
        }

        getImgEndpoints = ktorfit.create()
    }

    suspend fun generateImage(textPrompt: String) {
        val request = GetImgRequest(prompt = textPrompt)

        val token = "Bearer ${ApiKeys.GETIMG_API_KEY}"

        //val result = getImgEndpoints.textToImage(token, request)
    }

}