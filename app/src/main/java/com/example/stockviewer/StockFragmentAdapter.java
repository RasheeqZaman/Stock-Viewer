package com.example.stockviewer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class StockFragmentAdapter extends FragmentStatePagerAdapter {
    private List<List<StockModel>> models;

    public StockFragmentAdapter(@NonNull FragmentManager fm, List<List<StockModel>> models) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.models = models;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new StockFragment(models.get(position));
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
