package com.example.openappads.admob.openapp

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.eco.musicplayer.audioplayer.admob.app_open.AdMobAppOpenListener
import com.eco.musicplayer.audioplayer.admob.app_open.AppOpenOverlay
import com.eco.musicplayer.audioplayer.utils.CoolOffTime
import com.eco.musicplayer.audioplayer.utils.DVDLog
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
    private var adUnitId: String = ""
    var isLoadingAd = false
    var isShowingAd = false
    private var loadTime: Long = 0
    private var overlayView: AppOpenOverlay? = null

    fun setAdUnitId(id: String) {
        this.adUnitId = id
    }

    fun finishCoolOffTime() : Boolean {
        return CoolOffTime.finishCoolOffTime()
    }

    fun loadAd() {
        if (isLoadingAd || isLoaded()) return
        isLoadingAd = true
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(context, adUnitId, adRequest, object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                isLoadingAd = false
                loadTime = Date().time
                listener?.onAdLoaded()
                DVDLog.showLog("Open App Ad tải thành công")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                appOpenAd = null
                isLoadingAd = false
                listener?.onFailedAdLoad(loadAdError.message)
                DVDLog.showLog("Open App Ad tải thất bại")
            }
        })
    }

    fun showAd(activity: Activity, complete: (() -> Unit)) {
        if (isShowingAd) return

        if(!finishCoolOffTime()) return

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                overlayView?.hide()
                listener?.onAdDismiss()
                complete()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                DVDLog.showLog(adError.message)
                complete()
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

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }


    fun isShowing() = isShowingAd

    fun isLoaded(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo()
    }

    fun attachOverlayToActivity(activity: Activity) {
        if (overlayView != null && overlayView!!.isAttachedToWindow) return

        overlayView = AppOpenOverlay(activity)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        val decorView = activity.window.decorView as ViewGroup
        decorView.addView(overlayView, layoutParams)
    }
}