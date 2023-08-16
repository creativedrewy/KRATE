package com.solanamobile.krate.createscreen.endpoint

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.Serializable

@Serializable
data class GetImgRequest(
    val model: String = "stable-diffusion-v1-5",
    val prompt: String,
    val negative_prompt: String = "",
    val width: Int = 512,
    val height: Int = 512,
    val steps: Int = 30,
    val guidance: Double = 7.5,
    val seed: Int = 42,
    val scheduler: String = "dpmsolver++",
    val output_format: String = "jpeg"
)

@Serializable
data class GetImgResponse(
    val image: String
)

interface GetImgEndpoints {

    @Headers("Content-Type: application/json")
    @POST("stable-diffusion/text-to-image")
    suspend fun textToImage(
        @Header("Authorization") token: String,
        @Body data: GetImgRequest
    ): GetImgResponse
}