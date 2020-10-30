package com.example.stockviewer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StockTopAdapter extends PagerAdapter {
    private List<StockModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public StockTopAdapter(List<StockModel> models, Context context) {
        this.models = models;
        this.context = context;
    }


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

        final int itemPosition = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StockDetailsActivity.class);
                intent.putExtra("Model", models.get(itemPosition));
                context.startActivity(intent);
            }
        });

        TextView topCompanyName = view.findViewById(R.id.top_company_name);
        TextView topCompanyPrice = view.findViewById(R.id.top_company_price);
        TextView topCompanyPriceChange = view.findViewById(R.id.top_company_price_change);
        TextView topCompanyPriceSign = view.findViewById(R.id.top_company_price_sign);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        StockModel model = models.get(position);
        topCompanyName.setText(model.getCompanyName());
        topCompanyPrice.setText(formatter.format(model.getPrice()));
        topCompanyPriceSign.setText((model.getPriceChange()>0.0) ? "+" : (model.getPriceChange()<0.0 ? "-" : ""));
        topCompanyPriceChange.setText(formatter.format(Math.abs(model.getPriceChange())) + " (" + formatter.format(model.getPriceChangePercent()) + "%)");
        topCompanyPriceChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, (model.getPriceChange()>0.0) ? R.drawable.up : (model.getPriceChange()<0.0 ? R.drawable.down : R.drawable.equal), 0);

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
