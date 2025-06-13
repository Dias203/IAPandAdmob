package com.eco.musicplayer.audioplayer.admob.banner

interface AdmobBannerListener {
    fun onAdLoaded()
    fun onFailedAdLoad(errorMessage: String)
    fun onAdDismiss()
}