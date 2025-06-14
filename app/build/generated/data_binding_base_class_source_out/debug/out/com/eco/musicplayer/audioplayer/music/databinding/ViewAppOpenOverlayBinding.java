// Generated by view binder compiler. Do not edit!
package com.eco.musicplayer.audioplayer.music.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.eco.musicplayer.audioplayer.music.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class ViewAppOpenOverlayBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout bgOpenAppOverlay;

  private ViewAppOpenOverlayBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout bgOpenAppOverlay) {
    this.rootView = rootView;
    this.bgOpenAppOverlay = bgOpenAppOverlay;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewAppOpenOverlayBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewAppOpenOverlayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_app_open_overlay, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewAppOpenOverlayBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    ConstraintLayout bgOpenAppOverlay = (ConstraintLayout) rootView;

    return new ViewAppOpenOverlayBinding((ConstraintLayout) rootView, bgOpenAppOverlay);
  }
}
