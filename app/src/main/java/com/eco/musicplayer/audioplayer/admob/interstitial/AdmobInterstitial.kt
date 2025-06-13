package com.eco.musicplayer.audioplayer.admob.interstitial

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.constants.admob.ADS_INTERSTITIAL_UNIT_ID
import com.eco.musicplayer.audioplayer.utils.CoolOffTime
import com.eco.musicplayer.audioplayer.utils.DVDLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdmobInterstitial(private val context: Context) {
    var listener: AdmobInterstitialListener? = null
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var isError = false


    fun preloadInterstitialAd() {
        if (interstitialAd == null && !isLoading) {
            loadAd()
        }
    }

    fun isAdReady(): Boolean {
        //DVDLog.showLog("$interstitialAd - $isLoading")
        return interstitialAd != null && !isLoading
    }

    fun isLoading() : Boolean {
        //DVDLog.showLog("$interstitialAd - $isLoading")
        return interstitialAd == null && isLoading
    }

    fun isError() : Boolean {
        return isError
    }

    fun finishCoolOffTime(): Boolean {
        return CoolOffTime.finishCoolOffTime()
    }

    private fun loadAd() {
        if (interstitialAd != null || isLoading) return

        isLoading = true

        InterstitialAd.load(context,
            ADS_INTERSTITIAL_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setStateOnAdLoaded(ad)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    setStateOnAdFailedToLoad(error)
                }
            })
    }

    private fun setStateOnAdLoaded(ad: InterstitialAd) {
        //DVDLog.showLog("Quảng cáo interstitial đã được tải thành công")
        interstitialAd = ad
        isLoading = false
        isError = false
        listener?.onAdLoaded()
    }

    private fun setStateOnAdFailedToLoad(error: LoadAdError) {
        interstitialAd = null
        isLoading = false
        isError = true
        //DVDLog.showLog("Tải quảng cáo interstitial thất bại - Error Message: ${error.message}")
        listener?.onAdFailedToLoad(error.message)
    }


    fun showAd(activity: AppCompatActivity) {
        if (!isAdReady()) return

        if(!finishCoolOffTime()) return


        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                setStateOnAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                //DVDLog.showLog("Hiển thị quảng cáo interstitial thất bại - Error Message: ${adError.message}")
                setStateOnAdFailedToShowFullScreenContent(adError)
            }
        }
        interstitialAd?.show(activity)
    }


    private fun setStateOnAdDismissedFullScreenContent() {
        interstitialAd = null
        isLoading = false
        isError = false
        listener?.onShowFullScreen(true)
    }

    private fun setStateOnAdFailedToShowFullScreenContent(adError: AdError) {
        interstitialAd = null
        isLoading = false
        isError = false
        listener?.onShowFullScreen(true)
    }

    fun destroyAd() {
        interstitialAd?.fullScreenContentCallback = null
        interstitialAd = null
        isLoading = false
        isError = false
    }
}