package com.solanamobile.krate.profilescreen

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform