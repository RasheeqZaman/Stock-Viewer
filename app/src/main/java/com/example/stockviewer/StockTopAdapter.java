package com.example.stockviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class StockTopAdapter extends PagerAdapter {
    private List<StockModel> models;
    private LayoutInflater layoutInflater;

    public StockTopAdapter(List<StockModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    private Context context;

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.top_item, container, false);

        TextView topCompanyName = view.findViewById(R.id.top_company_name);
        TextView topCompanyPrice = view.findViewById(R.id.top_company_price);
        TextView topCompanyPriceChange = view.findViewById(R.id.top_company_price_change);
        TextView topCompanyPriceSign = view.findViewById(R.id.top_company_price_sign);

        topCompanyName.setText(models.get(position).getCompanyName());
        topCompanyPrice.setText(String.format("%.2f", models.get(position).getPrice()));
        topCompanyPriceSign.setText(models.get(position).isPriceIncrease() ? "+" : "-");
        topCompanyPriceChange.setText(String.format("%.2f", models.get(position).getPriceChange()) + " " + String.format("%.2f", models.get(position).getPriceChangePercent()) + "%");
        topCompanyPriceChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, models.get(position).isPriceIncrease() ? R.drawable.up : R.drawable.down, 0);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
