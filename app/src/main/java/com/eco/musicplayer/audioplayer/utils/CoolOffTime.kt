package com.eco.musicplayer.audioplayer.utils

object CoolOffTime {
    private var lastTimeShowAd = 0L
    private var coolOffTime = 10000L

    fun setLastTimeShowAd() {
        lastTimeShowAd = System.currentTimeMillis()
    }

    fun finishCoolOffTime() : Boolean {
        return System.currentTimeMillis() - lastTimeShowAd >= coolOffTime
    }
}