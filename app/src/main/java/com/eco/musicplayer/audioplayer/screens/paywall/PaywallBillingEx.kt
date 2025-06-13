package com.eco.musicplayer.audioplayer.screens.paywall

import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.Purchase
import com.eco.musicplayer.audioplayer.billing.InAppBillingListener
import com.eco.musicplayer.audioplayer.billing.model.BaseProductDetails
import com.eco.musicplayer.audioplayer.billing.model.ProductInfo
import com.eco.musicplayer.audioplayer.billing.model.SUBS
import com.eco.musicplayer.audioplayer.billing.model.IN_APP
import com.eco.musicplayer.audioplayer.constants.iap.PRODUCT_ID_LIFETIME
import com.eco.musicplayer.audioplayer.constants.iap.PRODUCT_ID_WEEK
import com.eco.musicplayer.audioplayer.constants.iap.PRODUCT_ID_YEAR
import com.eco.musicplayer.audioplayer.helpers.PurchasePrefsHelper
import com.eco.musicplayer.audioplayer.utils.DVDLog

fun PaywallActivity.initBilling() {
    inAppBillingManager.apply {
        listener = createInAppBillingListener()
        productInfoList = listOf(
            ProductInfo(SUBS, PRODUCT_ID_WEEK),
            ProductInfo(SUBS, PRODUCT_ID_YEAR),
            ProductInfo(IN_APP, PRODUCT_ID_LIFETIME)
        )
        startConnectToGooglePlay()
    }
}

fun PaywallActivity.createInAppBillingListener() = object : InAppBillingListener {
    override fun onStartConnectToGooglePlay() {
        showToast("Connecting to Google Play...")
    }


    override fun onProductsLoaded(products: List<BaseProductDetails>) {
        loadPriceUI(products)
        updatePlanSelectionBasedOnPurchases()
    }

    override fun onPurchasesLoaded(purchases: List<Purchase>) {
        DVDLog.showLog("13333111" + purchases.size + isPremium)
        PurchasePrefsHelper.saveIsPremiumStatus(this@createInAppBillingListener, purchases.isNotEmpty())
        isPremium = purchases.isNotEmpty()
        isIAPChecked = true
        if (purchases.isNotEmpty()) {
            updatePurchases(purchases)
        }
    }

    override fun onInAppBillingError(message: String) {
        updatePlanSelectionBasedOnPurchases()
        updateItem()
    }

    override fun onStartAcknowledgePurchase() {
        // Hiển thị loading nếu cần
    }

    override fun onPurchaseAcknowledged(productInfo: ProductInfo, purchase: Purchase) {
        purchase.products.firstOrNull()?.let { productId ->
            purchasedProducts.add(productId)
            updatePlanSelectionBasedOnPurchases()
            if(productId != PRODUCT_ID_LIFETIME) {
                updateItem()
            }
            PurchasePrefsHelper.saveIsPremiumStatus(this@createInAppBillingListener, true)
            showToast("Purchase successful: $productId")
        }
        isPremium = true
    }

    override fun onUserCancelPurchase() {
        showToast("Purchase cancelled")
    }

    override fun onPurchaseError(message: String, productInfo: ProductInfo) {
        showToast("Purchase error: $message")
    }


    fun PaywallActivity.updatePurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            purchase.products.forEach { productId ->
                purchasedProducts.add(productId)
            }
        }
        updatePlanSelectionBasedOnPurchases()
        updateItem()
    }

    private fun PaywallActivity.showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}