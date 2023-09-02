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