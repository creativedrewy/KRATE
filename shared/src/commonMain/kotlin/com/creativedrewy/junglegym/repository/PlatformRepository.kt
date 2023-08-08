package com.creativedrewy.junglegym.repository

import com.moriatsushi.koject.Provides

@Provides
class PlatformRepository {

    suspend fun getPlatformString(): String {
        return getPlatformName()
    }

}