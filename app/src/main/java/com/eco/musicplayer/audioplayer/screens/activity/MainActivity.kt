package com.eco.musicplayer.audioplayer.screens.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.BaseActivity
import com.eco.musicplayer.audioplayer.MyApplication
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBanner
import com.eco.musicplayer.audioplayer.admob.reward.AdmobReward
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitial
import com.eco.musicplayer.audioplayer.billing.InAppBillingManager
import com.eco.musicplayer.audioplayer.extensions.checkIAP
import com.eco.musicplayer.audioplayer.extensions.connectBilling
import com.eco.musicplayer.audioplayer.extensions.registerStartActivityForResult
import com.eco.musicplayer.audioplayer.extensions.setOnClick
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityMainBinding
import com.eco.musicplayer.audioplayer.utils.DVDLog
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    val admobOpenAppManager by inject<AdmobAppOpenApplication>()
    val banner by inject<AdmobBanner>()
    val rewardAd by inject<AdmobReward>()
    val rewardIntersAd by inject<AdmobRewardInterstitial>()
    lateinit var secondActivityLauncher: ActivityResultLauncher<Intent>
    val SECOND_ACTIVITY_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectBilling()
        setOnClick()

        registerStartActivityForResult()

        DVDLog.showLog("DVD")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val isPremium = data?.getBooleanExtra("isPremium", false) ?: false
            this.isPremium = isPremium
            PurchasePrefsHelper.saveIsPremiumStatus(this, isPremium)
            checkIAP()
        }
    }

    override fun onResume() {
        super.onResume()
        connectBilling()
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardIntersAd.destroyAd()
        banner.onDestroy()
        rewardAd.destroyAd()
    }
}