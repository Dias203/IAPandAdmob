package com.eco.musicplayer.audioplayer.extensions

import android.content.Intent
import com.eco.musicplayer.audioplayer.admob.app_open.AdMobAppOpenListener
import com.eco.musicplayer.audioplayer.constants.admob.ADS_OPEN_APP_UNIT_ID_SPLASH
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.screens.activity.MainActivity
import com.eco.musicplayer.audioplayer.screens.paywall.PaywallActivity
import com.eco.musicplayer.audioplayer.screens.splash.SplashActivity
import com.eco.musicplayer.audioplayer.utils.DVDLog


fun SplashActivity.loadAppOpenAdSplash() {
    admobAppOpen.listener = object : AdMobAppOpenListener {
        override fun onAdLoaded() {}

        override fun onFailedAdLoad(errorMessage: String) {}

        override fun onAdDismiss() {}

    }
    admobAppOpen.setAdUnitId(ADS_OPEN_APP_UNIT_ID_SPLASH)
    admobAppOpen.loadAd()
}


fun SplashActivity.isPremium(): Boolean {
    DVDLog.showLog(111111)
    return PurchasePrefsHelper.isPremium(this)
}


fun SplashActivity.goToPaywallActivity() {
    val intent = Intent(this, PaywallActivity::class.java)
    startActivity(intent)
    finish()
    jobSplash.destroy()
}

fun SplashActivity.goToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
    jobSplash.destroy()
}