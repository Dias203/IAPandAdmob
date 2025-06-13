package com.eco.musicplayer.audioplayer.admob.app_open

interface AdMobAppOpenListener {
    fun onAdLoaded()
    fun onFailedAdLoad(errorMessage: String)
    fun onAdDismiss()
}