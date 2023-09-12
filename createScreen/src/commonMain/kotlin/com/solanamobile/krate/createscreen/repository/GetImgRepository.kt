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

                developmentMode = true
            })

            converterFactories(
                CallConverterFactory()
//                object: Converter.Factory {
//                    override fun suspendResponseConverter(
//                        typeData: TypeData,
//                        ktorfit: Ktorfit
//                    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
//                        return object : Converter.SuspendResponseConverter<HttpResponse, Any> {
//                            override suspend fun convert(response: HttpResponse): Any {
//                                Logger.v { ":::::: ANDREW " + response.status.value }
//
//                                return response
//                            }
//                        }
//                    }
//                }
            )
        }

        getImgEndpoints = ktorfit.create()
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImage(textPrompt: String): String {
        val request = GetImgRequest(
            prompt = textPrompt,
        )

        val token = "Bearer ${ApiKeys.GETIMG_API_KEY}"
        val result = getImgEndpoints.textToImage(token, request)

        return result.image
    }

    suspend fun inpaintImage(prompt: String, srcImg: String, maskImg: String): String {
        val request = GetImgRequest(
            prompt = prompt,
            image = srcImg,
            mask_image = maskImg
        )

        val token = "Bearer ${ApiKeys.GETIMG_API_KEY}"
        val result = getImgEndpoints.inpaintImage(token, request)

//        Logger.v { ":::: ERROR: ${result.error?.message}" }
//        Logger.v { ":::: ERROR: ${result.error?.code}" }
//        Logger.v { ":::: ERROR: ${result.error?.param}" }
//        Logger.v { ":::: ERROR: ${result.error?.type}" }

        return result.image
    }
}