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
)

//{
//  "projectId": 1,
//  "transactionId": "90447fe1-5c2d-4a9c-b26f-4d867137041a",
//  "nftId": 4
//}