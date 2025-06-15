package com.eco.musicplayer.audioplayer.app_modules

import com.eco.musicplayer.audioplayer.MyApplication
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpen
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBanner
import com.eco.musicplayer.audioplayer.admob.interstitial.AdmobInterstitial
import com.eco.musicplayer.audioplayer.admob.native_ad.AdmobNative
import com.eco.musicplayer.audioplayer.admob.reward.AdmobReward
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitial
import com.eco.musicplayer.audioplayer.billing.InAppBillingManager
import com.eco.musicplayer.audioplayer.helpers.DialogFullScreen
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

    val appModule = module {
        single { androidContext().applicationContext }

        single { AdmobAppOpen(get()) }
        single { AdmobAppOpenApplication(androidContext() as MyApplication, get()) }
        single { AdmobBanner(get()) }
        single { AdmobNative(get()) }
        single { AdmobInterstitial(get()) }
        single { AdmobReward(get()) }
        single { AdmobRewardInterstitial(get()) }

        single { InAppBillingManager(get()) }

        single { DialogFullScreen(get()) }
    }