package com.example.stockviewer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class StockDetailFragmentAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;

    public StockDetailFragmentAdapter(@NonNull FragmentManager fm, StockModel model) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = new Fragment[]{new StockDetailBasicFragment(model), new StockDetailMarketFragment(model), new StockDetailAgmFragment(model)};
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
