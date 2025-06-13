package com.eco.musicplayer.audioplayer.admob.reward_interstitial

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.constants.admob.ADS_REWARD_INTERSTITIAL_AD
import com.eco.musicplayer.audioplayer.utils.CoolOffTime
import com.eco.musicplayer.audioplayer.utils.DVDLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

class AdmobRewardInterstitial(private val context: Context) {
    var listener: AdmobRewardInterstitialListener? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading = false
    private var isError = false

    fun preloadRewardIntersAd() {
        if (rewardedInterstitialAd == null && !isLoading) {
            loadAd()
        }
    }

    fun isAdReady(): Boolean {
        //ECOLog.showLog("$rewardedInterstitialAd - $isLoading")
        return rewardedInterstitialAd != null && !isLoading
    }

    fun isError() : Boolean {
        return isError
    }

    fun finishCoolOffTime() : Boolean{
        return CoolOffTime.finishCoolOffTime()
    }

    private fun loadAd() {
        if (rewardedInterstitialAd != null || isLoading) return

        isLoading = true

        RewardedInterstitialAd.load(context,
            ADS_REWARD_INTERSTITIAL_AD,
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    super.onAdLoaded(ad)
                    setStateOnAdLoaded(ad)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    setStateOnAdFailedToLoad(error)
                }
            })
    }

    private fun setStateOnAdLoaded(ad: RewardedInterstitialAd) {
        DVDLog.showLog("Quảng cáo rewardedInterstitialAd đã được tải thành công")
        rewardedInterstitialAd = ad
        isLoading = false
        isError = false
        listener?.onAdLoaded()
    }

    private fun setStateOnAdFailedToLoad(error: LoadAdError) {
        rewardedInterstitialAd = null
        isLoading = false
        isError = true
        DVDLog.showLog("Tải quảng cáo rewardedInterstitialAd thất bại - Error Message: ${error.message}")
        listener?.onFailedAdLoad(error.message)
    }



    fun showAd(activity: AppCompatActivity) {
        if (!isAdReady()) return
        if(!finishCoolOffTime()) return

        DVDLog.showLog("SHOW AD")
        rewardedInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                DVDLog.showLog("Dismiss12312313")
                setStateOnAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                DVDLog.showLog("Hiển thị quảng cáo rewardedInterstitialAd thất bại - Error Message: ${adError.message}")
                setStateOnAdFailedToShowFullScreenContent(adError)
            }

        }
        rewardedInterstitialAd?.show(activity, OnUserEarnedRewardListener{})
    }


    private fun setStateOnAdDismissedFullScreenContent() {
        rewardedInterstitialAd = null
        isLoading = false
        isError = false
        CoolOffTime.setLastTimeShowAd()
        listener?.onShowFullScreen(true)
    }

    private fun setStateOnAdFailedToShowFullScreenContent(adError: AdError) {
        rewardedInterstitialAd = null
        isLoading = false
        isError = false
        listener?.onShowFullScreen(false)
    }


    fun destroyAd() {
        rewardedInterstitialAd?.fullScreenContentCallback = null
        rewardedInterstitialAd = null
        isLoading = false
        isError = false
    }

}