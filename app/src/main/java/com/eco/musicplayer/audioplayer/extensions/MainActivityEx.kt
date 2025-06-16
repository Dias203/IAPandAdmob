package com.eco.musicplayer.audioplayer.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.admob.banner.AdmobBannerListener
import com.eco.musicplayer.audioplayer.admob.reward.AdmobRewardListener
import com.eco.musicplayer.audioplayer.admob.reward_interstitial.AdmobRewardInterstitialListener
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.screens.activity.MainActivity
import com.eco.musicplayer.audioplayer.screens.activity.SecondActivity
import com.eco.musicplayer.audioplayer.screens.paywall.PaywallActivity

// Duong Van Duc Master
fun MainActivity.connectBilling() {
    connectBilling { checkIAP() }
}

fun MainActivity.checkIAP() {
    val cachedPremiumStatus = if (!getIsIAPChecked()) {
        PurchasePrefsHelper.isPremium(this)
    } else {
        getIsPremium()
    }

    if (cachedPremiumStatus) {
        binding.icPremium.setColorFilter(ContextCompat.getColor(this@checkIAP, R.color.red))
        binding.adBannerContainer.visibility = View.GONE
        binding.loadingProgressBarBanner.visibility = View.GONE
    } else {
        binding.icPremium.setColorFilter(ContextCompat.getColor(this@checkIAP, R.color.gray))
        binding.loadingProgressBarBanner.visibility = View.VISIBLE
        binding.adBannerContainer.visibility = View.VISIBLE
        loadBannerAdmob()
    }
}

fun MainActivity.loadBannerAdmob() {
    binding.adBannerContainer.post {
        banner.loadInlineBanner(
            binding.adBannerContainer,
            binding.adBannerContainer.width,
            binding.adBannerContainer.height
        )

        banner.listener = object : AdmobBannerListener {
            override fun onAdLoaded() {
                binding.loadingProgressBarBanner.visibility = View.GONE
            }

            override fun onFailedAdLoad(errorMessage: String) {}
            override fun onAdDismiss() {}
        }
    }
}

fun MainActivity.setOnClick() {
    with(binding) {
        icPremium.setOnClickListener {
            openPaywallActivity()
        }
        btnRewardAd.setOnClickListener {
            rewardAd.preloadRewardAd()
            showDialogReward()
        }
        btnRewardInterstitialAd.setOnClickListener {
            rewardIntersAd.preloadRewardIntersAd()
            admobOpenAppManager.locked()
            if(!rewardIntersAd.finishCoolOffTime() || getIsPremium()){
                openSecondActivity()
            }
            else {
                dialogFullScreen.showDialog()
                registerListenerRewardInters()
                showAdWithTimeOut(6, rewardIntersAd) {
                    dialogFullScreen.hideDialog()
                    if (rewardIntersAd.isAdReady()) {
                        rewardIntersAd.showAd(this@setOnClick)
                    } else {
                        openSecondActivity()
                        admobOpenAppManager.unlock()
                    }
                }
            }
        }
    }
}

fun MainActivity.registerListenerRewardInters() {
    rewardIntersAd.listener = object :AdmobRewardInterstitialListener {
        override fun onAdLoaded() {}
        override fun onFailedAdLoad(error: String) {}
        override fun onShowFullScreen(isDismiss: Boolean) {
            dialogFullScreen.hideDialog()
            openSecondActivity()
            admobOpenAppManager.unlock()
        }
    }
}

// region rewardAd
fun MainActivity.showDialogReward() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Mở khóa bộ lọc ảnh")
    builder.setMessage("Bạn có muốn xem quảng cáo để nhận một lượt mở khóa bộ lọc ảnh?")

    builder.setPositiveButton("Xem AD") { _, _ ->
        showRewardAd()
    }

    builder.setNegativeButton("Hủy") { dialog, _ ->
        dialog.dismiss()
    }

    val dialog = builder.create()
    dialog.show()
}

fun MainActivity.showRewardAd() {
    admobOpenAppManager.locked()
    dialogFullScreen.showDialog()
    registerListenerReward()
    showAdWithTimeOut(6, rewardAd) {
        dialogFullScreen.hideDialog()
        if (rewardAd.isAdReady()) {
            rewardAd.showAd(this@showRewardAd)
        } else {
            admobOpenAppManager.unlock()
        }
    }
}

private fun MainActivity.registerListenerReward() {
    rewardAd.listener = object : AdmobRewardListener {
        override fun onAdLoaded() {}
        override fun onAdFailedToLoad(error: String) {}

        override fun onShowFullScreen(isDismiss: Boolean) {
            if (isDismiss) {
                dialogFullScreen.hideDialog()
                showToast("Mở khóa thành công!")
                admobOpenAppManager.unlock()
            } else {
                dialogFullScreen.hideDialog()
                showToast("Không thể tải quảng cáo!")
                admobOpenAppManager.unlock()
            }
        }
    }
}
// endregion


private fun MainActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun MainActivity.openSecondActivity() {
    val intent = Intent(this, SecondActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    startActivity(intent)
}

// region startActivityForResult

// deprecated ===
/*fun MainActivity.openSecondActivity() {
    val intent = Intent(this, SecondActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtra("isPremium", getIsPremium())
    }
    startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
}*/
// ====

/*fun MainActivity.registerStartActivityForResult() {
    secondActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val isPremium = data?.getBooleanExtra("isPremium", false) ?: false
            this.isPremium = isPremium
            PurchasePrefsHelper.saveIsPremiumStatus(this, isPremium)
            checkIAP()
        }
    }
}

fun MainActivity.openSecondActivity() {
    val intent = Intent(this, SecondActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtra("isPremium", getIsPremium())
    }
    secondActivityLauncher.launch(intent)
}*/

// endregion

fun MainActivity.openPaywallActivity() {
    val intent = Intent(this@openPaywallActivity, PaywallActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    startActivity(intent)
}

