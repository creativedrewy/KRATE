package com.creativedrewy.junglegym

import com.moriatsushi.koject.Provides

@Provides
class PlatformRepository {

    suspend fun getPlatformString(): String {
        return getPlatformName()
    }

}

expect fun getPlatformName(): String