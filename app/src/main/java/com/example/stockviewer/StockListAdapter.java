package com.example.stockviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StockListAdapter extends ArrayAdapter<String> {
    private List<StockModel> models;
    private Context context;

    public StockListAdapter(@NonNull Context context, List<StockModel> models, List<String> companyNames) {
        super(context, R.layout.stock_list_item, companyNames);
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View itemRow = inflater.inflate(R.layout.stock_list_item, parent, false);

        StockModel model = models.get(position);

        TextView companyName = itemRow.findViewById(R.id.stock_list_company_name);
        TextView companyPrice = itemRow.findViewById(R.id.stock_list_company_price);
        TextView companyPriceChange = itemRow.findViewById(R.id.stock_list_company_price_change);
        TextView companyPriceChangePercent = itemRow.findViewById(R.id.stock_list_company_price_change_percent);
        LinearLayout priceChangePercentLayout = itemRow.findViewById(R.id.stock_list_company_price_change_percent_layout);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        companyName.setText(model.getCompanyName());
        companyPrice.setText(formatter.format(model.getPrice()));
        companyPriceChange.setText(formatter.format(Math.abs(model.getPriceChange())));
        companyPriceChangePercent.setText(formatter.format(model.getPriceChangePercent()));
        priceChangePercentLayout.setBackgroundResource((model.getPriceChange()>0.0) ? R.drawable.stock_list_item_percent_inc_background : ((model.getPriceChange()<0.0) ? R.drawable.stock_list_item_percent_dec_background : R.drawable.stock_list_item_percent_same_background));

        return itemRow;
    }

    @Override
    public int getCount() {
        return models.size();
    }
}
