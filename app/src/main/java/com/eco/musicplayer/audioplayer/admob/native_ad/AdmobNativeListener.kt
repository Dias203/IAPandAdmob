package com.eco.musicplayer.audioplayer.admob.native_ad

interface AdmobNativeListener {
    fun onAdLoaded()
    fun onFailedAdLoad(error: String)
}