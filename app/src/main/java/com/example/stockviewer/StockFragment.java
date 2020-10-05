package com.example.stockviewer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {
    private List<StockModel> models;

    public StockFragment(List<StockModel> models) {
        this.models = models;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        ListView listView = view.findViewById(R.id.stock_fragment_list_view);
        List<String> companyNames = new ArrayList<>();
        for(StockModel model : models){
            companyNames.add(model.getCompanyName());
        }
        StockListAdapter adapter = new StockListAdapter(getContext(), models, companyNames);
        listView.setAdapter(adapter);

        return view;
    }
}