package com.eco.musicplayer.audioplayer.admob.reward

interface AdmobRewardListener {
    fun onAdLoaded()
    fun onAdFailedToLoad(error: String)
    fun onShowFullScreen(isDismiss: Boolean)
}