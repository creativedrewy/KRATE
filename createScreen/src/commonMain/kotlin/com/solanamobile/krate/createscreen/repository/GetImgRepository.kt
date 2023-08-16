package com.solanamobile.krate.createscreen.repository

import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.ApiKeys
import com.solanamobile.krate.createscreen.endpoint.GetImgEndpoints
import com.solanamobile.krate.createscreen.endpoint.GetImgRequest
import de.jensklingenberg.ktorfit.converter.builtin.CallConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi

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

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImage(textPrompt: String):String {
        val request = GetImgRequest(
            prompt = textPrompt,
        )

        val token = "Bearer ${ApiKeys.GETIMG_API_KEY}"

        //TODO: Need to try/catch this, as it returns totally diff obj on err
        val result = getImgEndpoints.textToImage(token, request)

        return result.image
    }

}