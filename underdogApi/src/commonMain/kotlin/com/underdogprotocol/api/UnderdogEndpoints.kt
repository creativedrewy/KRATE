package com.underdogprotocol.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface UnderdogEndpoints {

    //
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    @POST("projects/{projectId}/nfts")
    suspend fun createNft(
        @Header("authorization") apiKey: String,
        @Path("projectId") projId: String,
        @Body requestData: CreateNftRequest
    ): CreateNftResponse

}