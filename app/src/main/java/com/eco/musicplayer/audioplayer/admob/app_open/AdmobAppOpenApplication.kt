package com.eco.musicplayer.audioplayer.admob.app_open

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.eco.musicplayer.audioplayer.constants.admob.ADS_OPEN_APP_UNIT_ID_DEFAULT
import com.eco.musicplayer.audioplayer.screens.splash.SplashActivity
import com.example.openappads.admob.openapp.AdmobAppOpen
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdmobAppOpenApplication(
    private val application: Application, private val context: Context
) : DefaultLifecycleObserver {

    private val admobAppOpen by lazy {
        AdmobAppOpen(context).apply {
            setAdUnitId(ADS_OPEN_APP_UNIT_ID_DEFAULT)
            listener = object : AdMobAppOpenListener {
                override fun onAdLoaded() {
                    //ECOLog.showLog("Ad loaded successfully")
                }

                override fun onFailedAdLoad(message: String) {
                    //ECOLog.showLog("Ad failed to load: $message")
                }

                override fun onAdDismiss() {
                    //ECOLog.showLog("Ad dismissed")
                    loadAd()
                }
            }
        }
    }

    private var currentActivity: Activity? = null
    private var isLocked = false
    private var isMobileAdsInitialized = false



    fun initialize() {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(application) {
                isMobileAdsInitialized = true
                loadAd()
            }
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun locked() {
        isLocked = true
    }

    fun unlock() {
        isLocked = false
    }


    fun onActivityStarted(activity: Activity) {
        //ECOLog.showLog("onActivityStarted: ${activity.javaClass.simpleName}")
        currentActivity = activity
    }

    fun onActivityDestroyed(activity: Activity) {
        //ECOLog.showLog("onActivityDestroyed: ${activity.javaClass.simpleName}")
        if (currentActivity == activity) {
            currentActivity = null
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (currentActivity is SplashActivity) return
        if (admobAppOpen.isShowing()) return
        if (isLocked) return

        showAd(currentActivity!!)
    }

    override fun onStop(owner: LifecycleOwner) {
        //ECOLog.showLog("Application onStop")
    }

    private fun loadAd() {
        admobAppOpen.loadAd()
    }

    private fun showAd(activity: Activity) {
        if (isLocked) return
        admobAppOpen.attachOverlayToActivity(activity)
        admobAppOpen.showAd(activity) {}
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        admobAppOpen.destroyAd()
    }
}