package com.example.stockviewer;

import androidx.viewpager.widget.ViewPager;

public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private int currentPosition, scrollState;

    public CircularViewPagerHandler(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && this.scrollState == ViewPager.SCROLL_STATE_DRAGGING) {
            if (this.scrollState != ViewPager.SCROLL_STATE_SETTLING) {
                final int lastPosition = this.viewPager.getAdapter().getCount() - 1;
                if(this.currentPosition == 0) {
                    this.viewPager.setCurrentItem(lastPosition, true);
                } else if(this.currentPosition == lastPosition) {
                    this.viewPager.setCurrentItem(0, true);
                }
            }
        }
        this.scrollState = state;
    }
}
