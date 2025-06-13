package com.eco.musicplayer.audioplayer.helpers

import android.content.Context
import android.content.SharedPreferences
import com.eco.musicplayer.audioplayer.utils.DVDLog

object PurchasePrefsHelper {
    private const val PREF_NAME = "PurchasePrefs123"
    private const val KEY_PURCHASED_PRODUCTS = "purchased_products"
    private const val KEY_PURCHASED = "isPremium"
    private const val KEY_PURCHASED_SUBS = "key_purchased_subs"
    private const val KEY_PURCHASED_IN_APP = "key_purchased_in_app"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveIsPremiumStatus(context: Context, isPremium: Boolean) {
        DVDLog.showLog("saveIsPremiumSUBS - $isPremium")
        getPrefs(context).edit()
            .putBoolean(KEY_PURCHASED_PRODUCTS, isPremium)
            .apply()
    }

    fun saveIsPremiumSUBS(context: Context, isPremium: Boolean) {
        getPrefs(context).edit()
            .putBoolean(KEY_PURCHASED_SUBS, isPremium)
            .apply()
    }

    fun saveIsPremiumINAPP(context: Context, isPremium: Boolean) {
        getPrefs(context).edit()
            .putBoolean(KEY_PURCHASED_IN_APP, isPremium)
            .apply()
    }

    fun isPremium(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_PURCHASED_PRODUCTS, false)
    }


    fun clearPurchasedProducts(context: Context) {
        getPrefs(context).edit()
            .remove(KEY_PURCHASED_PRODUCTS)
            .apply()
    }
}