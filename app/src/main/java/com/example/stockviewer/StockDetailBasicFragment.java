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

public class StockDetailBasicFragment extends Fragment {
    private StockModel model;

    public StockDetailBasicFragment(StockModel model) {
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_detail_basic, container, false);

        TextView txtCompanyFullName = view.findViewById(R.id.detail_company_full_name);
        TextView txtPaidUpCapital = view.findViewById(R.id.detail_paid_up_capital);
        TextView txtFaceParValue = view.findViewById(R.id.detail_face_par_value);
        TextView txtTypeOfInstrument = view.findViewById(R.id.detail_type_of_instrument);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        txtCompanyFullName.setText(model.getCompanyFullName());
        txtPaidUpCapital.setText(formatter.format(model.getPaidUpCapital()));
        txtFaceParValue.setText(formatter.format(model.getFaceParValue()));
        txtTypeOfInstrument.setText(model.getTypeOfInstrument());

        return view;
    }
}