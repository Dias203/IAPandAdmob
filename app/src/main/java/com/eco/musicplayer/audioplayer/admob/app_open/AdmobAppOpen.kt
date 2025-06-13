package com.eco.musicplayer.audioplayer.admob.app_open

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.eco.musicplayer.audioplayer.utils.CoolOffTime
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.Date

class AdmobAppOpen(private val context: Context) {
    private var appOpenAd: AppOpenAd? = null
    var listener: AdMobAppOpenListener? = null
    var adUnitId: String = ""
    var isLoading = false
    var isShowingAd = false
    private var loadTime = 0L
    private var overlayView: AppOpenOverlay? = null

    fun setAdUnitID(adUnitId: String) {
        this.adUnitId = adUnitId
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    fun isLoaded() =  appOpenAd != null && wasLoadTimeLessThanNHoursAgo()


    fun isShowing() = isShowingAd

    fun finishCoolOffTime() = CoolOffTime.finishCoolOffTime()

    fun loadAd() {
        if (isLoading || isLoaded()) return
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(context, adUnitId, adRequest, object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                isLoading = false
                loadTime = Date().time
                listener?.onAdLoaded()
            }

            override fun onAdFailedToLoad(ad: LoadAdError) {
                appOpenAd = null
                isLoading = false
                listener?.onFailedAdLoad(ad.message)
            }
        })
    }

    fun showAd(activity: Activity, onComplete: () -> Unit) {
        if (isShowingAd) return

        if (!finishCoolOffTime()) return

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isLoading = false
                overlayView?.hide()
                listener?.onAdDismiss()
                onComplete()
            }

            override fun onAdFailedToShowFullScreenContent(ad: AdError) {
                appOpenAd = null
                isShowingAd = false
                onComplete()
            }

            override fun onAdShowedFullScreenContent() {
                isShowingAd = true
                overlayView?.show()
            }
        }
        isShowingAd = true
        appOpenAd?.show(activity)
    }

    fun destroyAd() {
        appOpenAd?.fullScreenContentCallback = null
        appOpenAd = null
        listener = null
    }

    fun attachOverlayToActivity(activity: Activity) {
        if (overlayView != null && overlayView!!.isAttachedToWindow) return

        overlayView = AppOpenOverlay(activity)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val decorView = activity.window.decorView as ViewGroup
        decorView.addView(overlayView, layoutParams)
    }
}