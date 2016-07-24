package com.vizy.newsapp.realread.ui.customview;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;
import android.view.View;

public abstract class CarouselSmoothScroller extends LinearSmoothScroller {

    protected CarouselSmoothScroller(final Context context) {
        super(context);
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public int calculateDyToMakeVisible(final View view, final int snapPreference) {
        final CarouselLayoutManager layoutManager = (CarouselLayoutManager) getLayoutManager();
        if (null == layoutManager || !layoutManager.canScrollVertically()) {
            return 0;
        }

        return layoutManager.getOffsetForCurrentView(view);
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public int calculateDxToMakeVisible(final View view, final int snapPreference) {
        final CarouselLayoutManager layoutManager = (CarouselLayoutManager) getLayoutManager();
        if (null == layoutManager || !layoutManager.canScrollHorizontally()) {
            return 0;
        }
        return layoutManager.getOffsetForCurrentView(view);
    }
}
