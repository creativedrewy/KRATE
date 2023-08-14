package com.solanamobile.krate.screen

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform