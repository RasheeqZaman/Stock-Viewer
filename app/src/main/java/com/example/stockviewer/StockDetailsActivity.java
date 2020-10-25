package com.example.stockviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StockDetailsActivity extends AppCompatActivity {
    ViewPager detailFragmentViewPager;
    ProgressBar progressBar;
    StockModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        model = (StockModel) intent.getSerializableExtra("Model");

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

        detailFragmentViewPager = findViewById(R.id.detail_fragment_viewpager);
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

        progressBar = findViewById(R.id.progress_bar_detail);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setAnimation(AnimationUtils.loadAnimation(StockDetailsActivity.this, R.anim.fade_in));

        WebContent content = new WebContent();
        content.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    private class WebContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setAnimation(AnimationUtils.loadAnimation(StockDetailsActivity.this, R.anim.fade_out));
            progressBar.setVisibility(View.GONE);
            detailFragmentViewPager.getAdapter().notifyDataSetChanged();
        }

        private void updateStockModel(String url) throws IOException, ParseException {
            Document document = Jsoup.connect(url).get();

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setCurrencySymbol("");
            formatter.setDecimalFormatSymbols(symbols);

            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);


            String companyFullName = document.select("h2.BodyHead").eq(0).select("i").text();
            model.setCompanyFullName(companyFullName);

            String webCompanyOpeningPrice = document.select("table#company").eq(1).select("tbody").select("tr").eq(4).select("td").eq(0).text();
            Double companyOpeningPrice = formatter.parse(webCompanyOpeningPrice).doubleValue();
            model.setOpeningPrice(companyOpeningPrice);

            String webCompanyPaidUpCapital = document.select("table#company").eq(2).select("tbody").select("tr").eq(1).select("td").eq(0).text();
            Double companyPaidUpCapital = formatter.parse(webCompanyPaidUpCapital).doubleValue();
            model.setPaidUpCapital(companyPaidUpCapital);

            String webCompanyFaceParValue = document.select("table#company").eq(2).select("tbody").select("tr").eq(2).select("td").eq(0).text();
            Double companyFaceParValue = formatter.parse(webCompanyFaceParValue).doubleValue();
            model.setFaceParValue(companyFaceParValue);

            String companyTypeOfInstrument = document.select("table#company").eq(2).select("tbody").select("tr").eq(1).select("td").eq(1).text();
            model.setTypeOfInstrument(companyTypeOfInstrument);

            String webCompanyCashDividend = document.select("table#company").eq(3).select("tbody").select("tr").eq(0).select("td").eq(0).text();
            String[] webCompanyCashDividends = webCompanyCashDividend.split(",");
            String companyCashDividend = (webCompanyCashDividends.length>2) ? webCompanyCashDividends[0].trim() + ", " + webCompanyCashDividends[1].trim() : webCompanyCashDividend;
            model.setCashDividend(companyCashDividend);

            String webCompanyStockDividend = document.select("table#company").eq(3).select("tbody").select("tr").eq(1).select("td").eq(0).text();
            String[] webCompanyStockDividends = webCompanyStockDividend.split(",");
            String companyStockDividend = (webCompanyStockDividends.length>2) ? webCompanyStockDividends[0].trim() + ", " + webCompanyStockDividends[1].trim() : webCompanyStockDividend;
            model.setStockDividend(companyStockDividend);

            String companyRightIssue = document.select("table#company").eq(3).select("tbody").select("tr").eq(2).select("td").eq(0).text();
            model.setRightIssue(companyRightIssue);

            String companyYearEnd = document.select("table#company").eq(3).select("tbody").select("tr").eq(3).select("td").eq(0).text();
            model.setYearEnd(companyYearEnd);

            String companyDate = document.select("h2.BodyHead").eq(1).select("i").text();
            String companyTime = document.select("table#company").eq(1).select("tbody").select("tr").eq(1).select("td").eq(0).text();
            Date companyLastUpdateTime = dateFormat.parse(companyDate + " " + companyTime);
            model.setLastUpdateTime(companyLastUpdateTime);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                updateStockModel("https://www.dsebd.org/displayCompany.php?name="+model.getCompanyName());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                Log.d("alright", e.getMessage());
            }
            return null;
        }
    }
}