package com.underdogprotocol.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface UnderdogEndpoints {

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

    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    @GET("projects/{projectId}/nfts")
    suspend fun listNfts(
        @Header("authorization") apiKey: String,
        @Path("projectId") projId: String,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("ownerAddress") ownerAddress: String? = null
    ): ListNftsResponse
}