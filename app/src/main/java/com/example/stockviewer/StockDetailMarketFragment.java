package com.example.stockviewer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StockDetailMarketFragment extends Fragment {
    private StockModel model;

    public StockDetailMarketFragment(StockModel model) {
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_detail_market, container, false);

        TextView txtLastTradingPrice = view.findViewById(R.id.detail_last_trading_price);
        TextView txtLastUpdate = view.findViewById(R.id.detail_last_update);
        TextView txtPriceChangeFragment = view.findViewById(R.id.detail_company_price_change_fragment);
        TextView txtOpeningPrice = view.findViewById(R.id.detail_opening_price);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm a");

        txtLastTradingPrice.setText(formatter.format(model.getPrice()));
        txtLastUpdate.setText(dateFormatter.format(model.getLastUpdateTime()));
        txtPriceChangeFragment.setText(formatter.format(model.getPriceChange()) + " (" + formatter.format(model.getPriceChangePercent()) + "%)");
        txtOpeningPrice.setText(formatter.format(model.getOpeningPrice()));

        return view;
    }
}