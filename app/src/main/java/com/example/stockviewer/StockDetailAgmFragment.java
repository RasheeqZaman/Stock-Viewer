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
import java.util.Locale;

public class StockDetailAgmFragment extends Fragment {
    private StockModel model;

    public StockDetailAgmFragment(StockModel model) {
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_detail_agm, container, false);

        TextView txtCashDividend = view.findViewById(R.id.detail_cash_dividend);
        TextView txtStockDividend = view.findViewById(R.id.detail_stock_dividend);
        TextView txtRightIssue = view.findViewById(R.id.detail_right_issue);
        TextView txtYearEnd = view.findViewById(R.id.detail_year_end);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        txtCashDividend.setText(model.getCashDividend());
        txtStockDividend.setText(model.getStockDividend());
        txtRightIssue.setText(model.getRightIssue());
        txtYearEnd.setText(model.getYearEnd());

        return view;
    }
}