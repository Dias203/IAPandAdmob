package com.eco.musicplayer.audioplayer.screens.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.eco.musicplayer.audioplayer.BaseActivity
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBanner
import com.eco.musicplayer.audioplayer.admob.reward.AdmobReward
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitial
import com.eco.musicplayer.audioplayer.extensions.checkIAP
import com.eco.musicplayer.audioplayer.extensions.registerStartActivityForResult
import com.eco.musicplayer.audioplayer.extensions.setOnClick
import com.eco.musicplayer.audioplayer.music.databinding.ActivityMainBinding
import com.eco.musicplayer.audioplayer.utils.DVDLog
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val banner:AdmobBanner by inject()
    val rewardAd: AdmobReward by inject()
    val rewardIntersAd: AdmobRewardInterstitial by inject()
    lateinit var secondActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPurchased { checkIAP() }
        setOnClick()

        registerStartActivityForResult()

        DVDLog.showLog("DVD")
    }


    override fun onDestroy() {
        super.onDestroy()
        rewardIntersAd.destroyAd()
        banner.onDestroy()
        rewardAd.destroyAd()
    }
}