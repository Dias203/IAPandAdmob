// Generated by view binder compiler. Do not edit!
package com.eco.musicplayer.audioplayer.music.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.eco.musicplayer.audioplayer.music.R;
import com.google.android.gms.ads.nativead.AdChoicesView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AdUnifiedBinding implements ViewBinding {
  @NonNull
  private final NativeAdView rootView;

  @NonNull
  public final AppCompatImageView adAppIcon;

  @NonNull
  public final AppCompatTextView adBody;

  @NonNull
  public final AppCompatButton adCallToAction;

  @NonNull
  public final AdChoicesView adChoices;

  @NonNull
  public final AppCompatTextView adHeadline;

  @NonNull
  public final MediaView adMedia;

  @NonNull
  public final RatingBar adStars;

  @NonNull
  public final AppCompatTextView adText;

  @NonNull
  public final View line;

  private AdUnifiedBinding(@NonNull NativeAdView rootView, @NonNull AppCompatImageView adAppIcon,
      @NonNull AppCompatTextView adBody, @NonNull AppCompatButton adCallToAction,
      @NonNull AdChoicesView adChoices, @NonNull AppCompatTextView adHeadline,
      @NonNull MediaView adMedia, @NonNull RatingBar adStars, @NonNull AppCompatTextView adText,
      @NonNull View line) {
    this.rootView = rootView;
    this.adAppIcon = adAppIcon;
    this.adBody = adBody;
    this.adCallToAction = adCallToAction;
    this.adChoices = adChoices;
    this.adHeadline = adHeadline;
    this.adMedia = adMedia;
    this.adStars = adStars;
    this.adText = adText;
    this.line = line;
  }

  @Override
  @NonNull
  public NativeAdView getRoot() {
    return rootView;
  }

  @NonNull
  public static AdUnifiedBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdUnifiedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.ad_unified, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdUnifiedBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adAppIcon;
      AppCompatImageView adAppIcon = ViewBindings.findChildViewById(rootView, id);
      if (adAppIcon == null) {
        break missingId;
      }

      id = R.id.adBody;
      AppCompatTextView adBody = ViewBindings.findChildViewById(rootView, id);
      if (adBody == null) {
        break missingId;
      }

      id = R.id.adCallToAction;
      AppCompatButton adCallToAction = ViewBindings.findChildViewById(rootView, id);
      if (adCallToAction == null) {
        break missingId;
      }

      id = R.id.adChoices;
      AdChoicesView adChoices = ViewBindings.findChildViewById(rootView, id);
      if (adChoices == null) {
        break missingId;
      }

      id = R.id.adHeadline;
      AppCompatTextView adHeadline = ViewBindings.findChildViewById(rootView, id);
      if (adHeadline == null) {
        break missingId;
      }

      id = R.id.adMedia;
      MediaView adMedia = ViewBindings.findChildViewById(rootView, id);
      if (adMedia == null) {
        break missingId;
      }

      id = R.id.ad_stars;
      RatingBar adStars = ViewBindings.findChildViewById(rootView, id);
      if (adStars == null) {
        break missingId;
      }

      id = R.id.adText;
      AppCompatTextView adText = ViewBindings.findChildViewById(rootView, id);
      if (adText == null) {
        break missingId;
      }

      id = R.id.line;
      View line = ViewBindings.findChildViewById(rootView, id);
      if (line == null) {
        break missingId;
      }

      return new AdUnifiedBinding((NativeAdView) rootView, adAppIcon, adBody, adCallToAction,
          adChoices, adHeadline, adMedia, adStars, adText, line);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
