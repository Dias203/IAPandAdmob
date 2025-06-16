package com.eco.musicplayer.audioplayer

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.billingclient.api.Purchase
import com.eco.musicplayer.audioplayer.admob.app_open.AdmobAppOpenApplication
import com.eco.musicplayer.audioplayer.admob.interstitial.AdmobInterstitial
import com.eco.musicplayer.audioplayer.admob.reward.AdmobReward
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitial
import com.eco.musicplayer.audioplayer.billing.InAppBillingListener
import com.eco.musicplayer.audioplayer.billing.InAppBillingManager
import com.eco.musicplayer.audioplayer.billing.model.BaseProductDetails
import com.eco.musicplayer.audioplayer.billing.model.ProductInfo
import com.eco.musicplayer.audioplayer.extensions.checkIAP
import com.eco.musicplayer.audioplayer.helpers.DialogFullScreen
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.utils.DVDLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

open class BaseActivity : AppCompatActivity() {
    val dialogFullScreen: DialogFullScreen by inject{ parametersOf(this) }
    val iapManager: InAppBillingManager by inject()
    val admobOpenAppManager: AdmobAppOpenApplication by inject()
    var isPremium = false
    var isIAPChecked = false

    fun showAdWithTimeOut(
        seconds: Int,
        condition: Any,
        onComplete: () -> Unit
    ) {
        var job: Job? = null
        val delayStep = ((seconds * 1000) / 100).toLong()

        job = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                var progress = 0
                while (isActive && progress <= 100) {
                    val isReady = when (condition) {
                        is AdmobReward -> condition.isAdReady() || condition.isError()
                        is AdmobRewardInterstitial -> condition.isAdReady() || condition.isError()
                        is AdmobInterstitial -> condition.isAdReady() || condition.isError()
                        else -> false
                    }
                    if (isReady && progress >= 10) break
                    delay(delayStep)
                    progress++
                }
                withContext(Dispatchers.Main) { if (isActive) onComplete() }
                job?.cancel()
                job = null
            }
        }

    }

    fun getIsPremium(): Boolean {
        DVDLog.showLog("isPremium - $isPremium")
        return isPremium
    }

    fun getIsIAPChecked(): Boolean = isIAPChecked


    fun checkPurchased(onCheckIAP: () -> Unit) {
        iapManager.listener = createInAppBillingListener(onCheckIAP)
        iapManager.startConnectToGooglePlay()
    }


    private fun createInAppBillingListener(onCheckIAP: () -> Unit) = object : InAppBillingListener {
        override fun onStartConnectToGooglePlay() {}

        override fun onProductsLoaded(products: List<BaseProductDetails>) {}

        override fun onPurchasesLoaded(purchases: List<Purchase>) {
            isPremium = purchases.isNotEmpty()
            DVDLog.showLog(isPremium)
            isIAPChecked = true

            PurchasePrefsHelper.saveIsPremiumStatus(this@BaseActivity, isPremium)

            onCheckIAP()
        }

        override fun onInAppBillingError(message: String) {}
        override fun onStartAcknowledgePurchase() {}
        override fun onPurchaseAcknowledged(productInfo: ProductInfo, purchase: Purchase) {
            DVDLog.showLog("isPremium 13112003 - $isPremium")
            isPremium = true
            PurchasePrefsHelper.saveIsPremiumStatus(this@BaseActivity, isPremium)
            onCheckIAP()
        }

        override fun onUserCancelPurchase() {}
        override fun onPurchaseError(message: String, productInfo: ProductInfo) {}
    }
}