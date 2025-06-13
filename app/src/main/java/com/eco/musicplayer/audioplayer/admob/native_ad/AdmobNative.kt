package com.eco.musicplayer.audioplayer.admob.native_ad

import android.content.Context
import com.eco.musicplayer.audioplayer.constants.admob.ADS_NATIVE_UNIT_ID
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaAspectRatio
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class AdmobNative(private val context: Context)  {
    private var nativeAd: NativeAd? = null
    private val listener: AdmobNativeListener? = null

    fun loadNativeAd(bind: (NativeAd) -> Unit) {
        val builder = AdLoader.Builder(context, ADS_NATIVE_UNIT_ID)
            .forNativeAd {
                nativeAd?.destroy()
                nativeAd = it
                bind(it)
                listener?.onAdLoaded()
            }

        val videoOptions = VideoOptions.Builder().setStartMuted(false).build()
        val adOptions = NativeAdOptions.Builder().setMediaAspectRatio(MediaAspectRatio.PORTRAIT)
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build()
        /*val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .build()*/

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                listener?.onAdLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                listener?.onFailedAdLoad(error.message)
            }
        })
            .withNativeAdOptions(adOptions) // -> Đặt các tùy chọn cho quảng cáo gốc
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun onDestroy() {
        nativeAd?.destroy()
    }
}