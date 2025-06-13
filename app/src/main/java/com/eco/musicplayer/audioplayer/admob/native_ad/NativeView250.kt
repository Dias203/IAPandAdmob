package com.eco.musicplayer.audioplayer.admob.native_ad

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.eco.musicplayer.audioplayer.music.databinding.AdUnifiedBinding
import com.google.android.gms.ads.nativead.NativeAd

class NativeView250 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var binding: AdUnifiedBinding? = null
    init {
        binding = AdUnifiedBinding.inflate(LayoutInflater.from(context), this, true)
    }


    fun loaded(nativeAd: NativeAd) {
        binding?.let {unifiedAdBinding ->
            val nativeAdView = unifiedAdBinding.root


            unifiedAdBinding.adHeadline.text = nativeAd.headline


            nativeAdView.apply {
                headlineView = unifiedAdBinding.adHeadline
                mediaView = unifiedAdBinding.adMedia
                nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.mediaContent = it }
                callToActionView = unifiedAdBinding.adCallToAction
                unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
                unifiedAdBinding.adStars.rating = nativeAd.starRating!!.toFloat()
                unifiedAdBinding.adBody.text = nativeAd.body

                setNativeAd(nativeAd)
            }
        }
    }
}