package com.underdogprotocol.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateNftRequest(
    val name: String,
    val symbol: String = "",
    val description: String = "",
    val image: String,
    val animationUrl: String = "",
    val externalUrl: String = "",
    val attributes: List<Map<String, String>> = listOf(),
    val receiverAddress: String = "",
    val delegated: Boolean = false,
    val upsert: Boolean = false
)

@Serializable
data class CreateNftResponse(
    val code: Int = -1,
    val message: String = "",
    val projectIdId: Int = -1,
    val transactionId: String = ""
) {
    val isSuccess
        get() = code == -1 && message == ""
}

@Serializable
data class ListNftsResponse(
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val totalResults: Int,
    val results: List<NftData>
)

@Serializable
data class NftData(
    val id: Int,
    val status: String,
    val projectId: Int,
    val mintAddress: String,
    val ownerAddress: String,
    val name: String,
    val image: String
)