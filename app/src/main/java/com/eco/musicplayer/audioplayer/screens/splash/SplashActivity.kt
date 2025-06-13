package com.eco.musicplayer.audioplayer.screens.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpen
import com.eco.musicplayer.audioplayer.extensions.goToMainActivity
import com.eco.musicplayer.audioplayer.extensions.goToPaywallActivity
import com.eco.musicplayer.audioplayer.extensions.isPremium
import com.eco.musicplayer.audioplayer.extensions.loadAppOpenAdSplash
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.utils.DVDLog
import com.eco.musicplayer.audioplayer.utils.JobSplash

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), JobSplash.ProgressUpdated {
    val jobSplash by lazy { JobSplash() }
    val admobAppOpen by lazy { AdmobAppOpen(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        if (isPremium()) {
            goToMainActivity()
        } else {
            loadAppOpenAdSplash()
        }
    }

    override fun onPause() {
        super.onPause()
        jobSplash.stopJob()
    }

    override fun onResume() {
        super.onResume()
        jobSplash.startJob(this)
    }

    override fun onProgressUpdated(count: Int) {
        findViewById<ProgressBar>(R.id.loadingBar).progress = count

        if (jobSplash.isProgressMax()) {
            goToMainActivity()
            return
        }
        if (admobAppOpen.isLoaded()) {
            jobSplash.stopJob()
            DVDLog.showLog(isPremium())
            admobAppOpen.showAd(this) {
                goToMainActivity()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobSplash.destroy()
    }
}