package com.eco.musicplayer.audioplayer.admob.interstitial

interface AdmobInterstitialListener {
    fun onAdLoaded()
    fun onAdFailedToLoad(error: String)
    fun onShowFullScreen(isDismiss: Boolean)
}