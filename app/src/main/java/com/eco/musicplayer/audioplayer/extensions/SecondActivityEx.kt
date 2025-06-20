package com.eco.musicplayer.audioplayer.extensions

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.eco.musicplayer.audioplayer.admob.interstitial.AdmobInterstitialListener
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.screens.activity.MainActivity
import com.eco.musicplayer.audioplayer.screens.activity.SecondActivity

fun SecondActivity.connectBilling() {
    connectBilling { checkIAP() }
}

// endregion
fun SecondActivity.checkIAP() {
    val cachedPremiumStatus = if (!getIsIAPChecked()) {
        PurchasePrefsHelper.isPremium(this)
    } else {
        getIsPremium()
    }

    if (cachedPremiumStatus) {
        binding.adFrame.visibility = View.GONE
        binding.adBannerContainer.visibility = View.GONE
    } else {
        binding.adFrame.visibility = View.VISIBLE
        binding.adBannerContainer.visibility = View.VISIBLE
        loadAdMob()
    }
}


fun SecondActivity.setOnClick() {
    binding.icBack.setOnClickListener {
        admobOpenAppManager.locked()
        //DVDLog.showLog("getIsPremium() - ${getIsPremium()}")
        if (getIsPremium() || !interstitialAd.finishCoolOffTime()) {
            openMainActivity()
            return@setOnClickListener
        } else {
            dialogFullScreen.showDialog()
            registerListenerInterstitial()
            showAdWithTimeOut(6, interstitialAd) {
                dialogFullScreen.hideDialog()
                if (interstitialAd.isAdReady()) {
                    interstitialAd.showAd(this)
                } else {
                    openMainActivity()
                }
            }
        }
    }
}



fun SecondActivity.openMainActivity() {
    val intentAd = Intent(this, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    startActivity(intentAd)
}

fun SecondActivity.registerListenerInterstitial() {
    interstitialAd.listener = object : AdmobInterstitialListener {

        override fun onAdLoaded() {}
        override fun onAdFailedToLoad(error: String) {}

        override fun onShowFullScreen(isDismiss: Boolean) {
            if (isDismiss) {
                openMainActivity()
                dialogFullScreen.hideDialog()
                admobOpenAppManager.unlock()
            } else {
                openMainActivity()
                dialogFullScreen.hideDialog()
                admobOpenAppManager.unlock()
            }
        }
    }
}

private fun SecondActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun SecondActivity.loadAdMob() {
    bannerAd.load(binding.adBannerContainer, true)
    interstitialAd.preloadInterstitialAd()
    loadNative()
}

private fun SecondActivity.loadNative() {
    binding.adFrame.visibility = View.GONE
    nativeAd.loadNativeAd { ad ->
        binding.adFrame.visibility = View.VISIBLE
        binding.adFrame.loaded(ad)
    }
}

fun SecondActivity.onActivityDestroyed() {
    iapManager.destroy()
    interstitialAd.destroyAd()
    nativeAd.onDestroy()
    bannerAd.onDestroy()
}