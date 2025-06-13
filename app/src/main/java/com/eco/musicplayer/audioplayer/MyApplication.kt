package com.eco.musicplayer.audioplayer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.utils.DVDLog

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    val admobAppOpenManager by lazy {  AdmobAppOpenApplication(this, applicationContext) }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        DVDLog.showLog("On Create")
        admobAppOpenManager.initialize()
        ProcessLifecycleOwner.get().lifecycle.addObserver(admobAppOpenManager)
    }
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(p0: Activity) {
        admobAppOpenManager.onActivityStarted(activity = p0)
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {
        admobAppOpenManager.onActivityDestroyed(activity = p0)
    }
}