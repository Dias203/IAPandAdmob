package com.eco.musicplayer.audioplayer.modules

import android.content.Context
import com.eco.musicplayer.audioplayer.BaseActivity
import com.eco.musicplayer.audioplayer.MyApplication
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBanner
import com.eco.musicplayer.audioplayer.admob.interstitial.AdmobInterstitial
import com.eco.musicplayer.audioplayer.admob.native_ad.AdmobNative
import com.eco.musicplayer.audioplayer.admob.reward.AdmobReward
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitial
import com.eco.musicplayer.audioplayer.billing.InAppBillingManager
import com.eco.musicplayer.audioplayer.helpers.DialogFullScreen
import com.eco.musicplayer.audioplayer.screens.activity.MainActivity
import com.eco.musicplayer.audioplayer.screens.activity.SecondActivity
import com.example.openappads.admob.openapp.AdmobAppOpen
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { AdmobAppOpenApplication(androidContext() as MyApplication, get()) }
    single { AdmobAppOpen(get()) }
    factory { AdmobInterstitial(get()) }
    factory { AdmobRewardInterstitial(get()) }
    factory { AdmobBanner(get()) }
    factory { AdmobNative(get()) }
    factory { AdmobReward(get()) }
    factory { AdmobReward(get()) }

    factory { (context: Context) -> DialogFullScreen(context) }

    single { InAppBillingManager(get()) }

}