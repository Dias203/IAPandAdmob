package com.eco.musicplayer.audioplayer.admob.reward_interstitial

interface AdmobRewardInterstitialListener {
    fun onAdLoaded()
    fun onFailedAdLoad(error: String)
    fun onShowFullScreen(isDismiss: Boolean)
}