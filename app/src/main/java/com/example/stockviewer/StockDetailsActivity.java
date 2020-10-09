package com.example.stockviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class StockDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        StockModel model = (StockModel) intent.getSerializableExtra("Model");

        TextView txtCompanyName = findViewById(R.id.detail_company_name);
        TextView txtCompanyPrice = findViewById(R.id.detail_company_price);
        TextView txtCompanyPriceChange = findViewById(R.id.detail_company_price_change);
        TextView txtCompanyPriceSign = findViewById(R.id.detail_company_price_sign);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        txtCompanyName.setText(model.getCompanyName());
        txtCompanyPrice.setText(formatter.format(model.getPrice()));
        txtCompanyPriceSign.setText((model.getPriceChange()>0.0) ? "+" : (model.getPriceChange()<0.0 ? "-" : ""));
        txtCompanyPriceChange.setText(formatter.format(Math.abs(model.getPriceChange())) + " (" + formatter.format(model.getPriceChangePercent()) + "%)");
        txtCompanyPriceChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, (model.getPriceChange()>0.0) ? R.drawable.up : (model.getPriceChange()<0.0 ? R.drawable.down : R.drawable.equal), 0);

        String[] tabNames = new String[]{"Basic", "Market", "AGM"};
        TabLayout detailFragmentTabLayout = findViewById(R.id.detail_fragment_tab_layout);
        for(String tabName : tabNames){
            detailFragmentTabLayout.addTab(detailFragmentTabLayout.newTab().setText(tabName));
        }

        final ViewPager detailFragmentViewPager = findViewById(R.id.detail_fragment_viewpager);
        StockDetailFragmentAdapter fragmentAdapter = new StockDetailFragmentAdapter(getSupportFragmentManager(), model);
        detailFragmentViewPager.setAdapter(fragmentAdapter);

        detailFragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(detailFragmentTabLayout));
        detailFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                detailFragmentViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        return super.onCreateOptionsMenu(menu);
    }
}