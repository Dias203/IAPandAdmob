package com.eco.musicplayer.audioplayer.screens.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.BaseActivity
import com.eco.musicplayer.audioplayer.MyApplication
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBanner
import com.eco.musicplayer.audioplayer.admob.interstitial.AdmobInterstitial
import com.eco.musicplayer.audioplayer.admob.native_ad.AdmobNative
import com.eco.musicplayer.audioplayer.billing.InAppBillingManager
import com.eco.musicplayer.audioplayer.extensions.checkIAP
import com.eco.musicplayer.audioplayer.extensions.connectBilling
import com.eco.musicplayer.audioplayer.extensions.loadAdMob
import com.eco.musicplayer.audioplayer.extensions.onActivityDestroyed
import com.eco.musicplayer.audioplayer.extensions.openMainActivity
import com.eco.musicplayer.audioplayer.extensions.setOnClick
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivitySecondBinding
import com.eco.musicplayer.audioplayer.utils.DVDLog
import org.koin.android.ext.android.inject

class SecondActivity : BaseActivity(){
    lateinit var binding: ActivitySecondBinding
    val bannerAd by inject<AdmobBanner>()
    val interstitialAd by inject<AdmobInterstitial>()
    val nativeAd by inject<AdmobNative>()
    val admobOpenAppManager by inject<AdmobAppOpenApplication>()
    private lateinit var prefsListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bgSecond)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DVDLog.showLog("DVD")
        prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == PurchasePrefsHelper.KEY_PURCHASED_PRODUCTS) {
                isPremium = PurchasePrefsHelper.isPremium(this)
                checkIAP()
            }
        }
        PurchasePrefsHelper.getPrefs(this).registerOnSharedPreferenceChangeListener(prefsListener)
        //connectBilling()
        checkIAP()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        //connectBilling()
        //checkIAP()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        dialogFullScreen.showDialog()
        admobOpenAppManager.locked()
        showAdWithTimeOut(6, interstitialAd) {
            if(interstitialAd.isAdReady()) {
                interstitialAd.showAd(this)
            }
            else{
                openMainActivity()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onActivityDestroyed()
        PurchasePrefsHelper.getPrefs(this).unregisterOnSharedPreferenceChangeListener(prefsListener)
        onActivityDestroyed()
    }
}