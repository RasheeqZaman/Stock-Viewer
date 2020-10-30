package com.example.stockviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager, fragmentViewPager;
    StockTopAdapter adapter;
    Map<String, StockModel> modelsMap;
    List<List<StockModel>> models;
    ListView searchListView;
    ArrayAdapter searchAdapter;
    List<String> companyNames;
    SaveData saveData;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("alright", "onCreate: created");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String[] tabNames = new String[]{"All", "Recent", "Bookmark"};

        modelsMap = getAllStockModelsMap();

        models = new ArrayList<>();
        models.add(getAllStockModels());
        models.add(getRecentStockModels());
        models.add(getBookmarkStockModels());

        adapter = new StockTopAdapter(models.get(0), this);

        viewPager = findViewById(R.id.top_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));

        Timer timer = playSlideViewPager(viewPager);

        ImageButton playPauseBtn = (ImageButton)findViewById(R.id.play_pause);
        playPauseBtn.setOnClickListener(new PlayPauseSliderOnClickListener(playPauseBtn, timer, viewPager, models.size()));


        TabLayout stockFragmentTabLayout = findViewById(R.id.stock_fragment_tab_layout);
        for(String tabName : tabNames){
            stockFragmentTabLayout.addTab(stockFragmentTabLayout.newTab().setText(tabName));
        }

        fragmentViewPager = findViewById(R.id.fragment_viewpager);
        StockFragmentAdapter stockFragmentAdapter = new StockFragmentAdapter(getSupportFragmentManager(), models);
        fragmentViewPager.setAdapter(stockFragmentAdapter);

        fragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(stockFragmentTabLayout));
        stockFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        searchListView = findViewById(R.id.search_stock_list_view);
        companyNames = new ArrayList<>();
        for(StockModel model : models.get(0)){
            companyNames.add(model.getCompanyName());
        }
        searchAdapter = new ArrayAdapter<String>(this, R.layout.stock_list_search_item, companyNames);
        searchListView.setAdapter(searchAdapter);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String companyName = (String) searchAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, StockDetailsActivity.class);
                intent.putExtra("Model", modelsMap.get(companyName));
                startActivity(intent);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));

        saveData = new SaveData(this);

        WebContent content = new WebContent();
        content.execute();



        /*Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            updateStockData();
                        }catch (ParseException e){}
                    }
                });
            }
        }, 5 * 1000, 5 * 1000);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> bookmarkCompanyNames = saveData.getData("Bookmark");
        Log.d("alright", "onResume: "+bookmarkCompanyNames.toString());
        models.get(2).clear();
        for(String bcn : bookmarkCompanyNames){
            if(modelsMap.containsKey(bcn)){
                models.get(2).add(modelsMap.get(bcn));
            }
        }

        List<String> recentCompanyNames = saveData.getData("Recent");
        Log.d("alright", "doInBackground: "+recentCompanyNames.toString());
        models.get(1).clear();
        for(int n=recentCompanyNames.size(), i=n-1; i>=0; i--){
            if(modelsMap.containsKey(recentCompanyNames.get(i))){
                models.get(1).add(modelsMap.get(recentCompanyNames.get(i)));
            }
        }

        fragmentViewPager.getAdapter().notifyDataSetChanged();
    }

    private List<StockModel> getStockDataFromWebsite(){
        List<StockModel> models = new ArrayList<>();

        try {
            Document document = Jsoup.connect("https://www.dsebd.org/company_listing.php").get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return models;
    }

    private List<StockModel> getBookmarkStockModels() {
        List<StockModel> models = new ArrayList<>();
        return models;
    }

    private List<StockModel> getRecentStockModels() {
        List<StockModel> models = new ArrayList<>();
        return models;
    }

    private Map<String, StockModel> getAllStockModelsMap() {
        Map<String, StockModel> models = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        try {
            models.put("ACI", new StockModel("ACI", "ACI Limited", "Equity", "100% 2019, 115% 2018, 115% 2017", "15% 2019, 3.50% 2018", "1R:1(At Par) 1997", "30-Jun", 250.00, 1, 0.40, 573.73, 10.0, 249.00, dateFormat.parse("Oct 07, 2020 11:40 AM")));
            models.put("ACMELAB", new StockModel("ACMELAB", "The ACME Laboratories Limited", "Equity", "35% 2019, 35% 2018", "-", "-", "30-Jun", 71.10, 1.1, 1.57, 2116.00, 10.0, 70.00, dateFormat.parse("Oct 07, 2020 12:28 PM")));
            models.put("AGRANINS", new StockModel("AGRANINS", "Agrani Insurance Co. Ltd.", "Equity", "10% 2019, 5% 2017", "5% 2018, 5% 2017", "n/a", "31-Dec", 38.00,  	0.9, 2.43, 302.45, 10.0, 37.50, dateFormat.parse("Oct 07, 2020 12:32 PM")));
            models.put("AIL", new StockModel("AIL", "Alif Industries Limited", "Equity", "3% 2019, 25% 2018", "7% 2019, 10% 2018", "-", "30-Jun", 33.00, 0.4, 1.23, 442.52, 10.0, 32.90, dateFormat.parse("Oct 08, 2020 4:00 PM")));
            models.put("AIBL1STMF", new StockModel("AIBL1STMF", "AIBL 1st Islamic Mutual Fund", "Mutual Fund", "8% 2019, 8% 2018", "-", "-", "31-Mar", 7.90, -0.1, 1.25, 1000.00, 10.0, 8.10, dateFormat.parse("Oct 08, 2020 4:00 PM")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return models;
    }

    private List<StockModel> getAllStockModels(){
        List<StockModel> models = new ArrayList<>();
        for(StockModel m : modelsMap.values()){
            models.add(m);
        }
        return models;
    }

    private Timer playSlideViewPager(ViewPager viewPager) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTask(viewPager), 3*1000, 3*1000);
        return timer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        final LinearLayout btnLayout = findViewById(R.id.btn_layout);
        final LinearLayout bottomFragmentLayout = findViewById(R.id.bottom_fragment_layout);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchListView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
                bottomFragmentLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchListView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
                bottomFragmentLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class PlayPauseSliderOnClickListener implements View.OnClickListener{
        private boolean currentlyPaused;
        private ImageButton imageButton;
        private Timer timer;
        private ViewPager viewPager;
        private int totalItems;

        public PlayPauseSliderOnClickListener(ImageButton imageButton, Timer timer, ViewPager viewPager, int totalItems) {
            this.currentlyPaused = false;
            this.imageButton = imageButton;
            this.timer = timer;
            this.viewPager = viewPager;
            this.totalItems = totalItems;
        }

        public boolean isCurrentlyPaused() {
            return currentlyPaused;
        }

        @Override
        public void onClick(View v) {
            imageButton.setImageResource((currentlyPaused) ? R.drawable.pause : R.drawable.play);
            if(currentlyPaused){
                timer = playSlideViewPager(viewPager);
            }else{
                timer.cancel();
            }
            currentlyPaused = !currentlyPaused;
        }
    }

    public class SliderTask extends TimerTask {
        private ViewPager viewPager;

        public SliderTask(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    int itemIndex = (viewPager.getCurrentItem() + 1) % modelsMap.size();
                    viewPager.setCurrentItem(itemIndex);
                }
            });
        }
    }

    private class WebContent extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
            progressBar.setVisibility(View.GONE);
            viewPager.getAdapter().notifyDataSetChanged();
            fragmentViewPager.getAdapter().notifyDataSetChanged();
            searchAdapter.notifyDataSetChanged();
        }

        private StockModel getPrimaryStockModel(String data) throws ParseException {
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setCurrencySymbol("");
            formatter.setDecimalFormatSymbols(symbols);

            String[] datas = data.split(" ");
            return new StockModel(datas[0], formatter.parse(datas[1]).doubleValue(), formatter.parse(datas[2]).doubleValue(), Double.parseDouble(datas[3].substring(0, datas[3].length()-1)));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("https://www.dsebd.org/").get();
                Elements companyLinks = document.select("a.abhead");
                for(Element e : companyLinks){
                    StockModel model = getPrimaryStockModel(e.text());
                    if(modelsMap.containsKey(model.getCompanyName())){
                        StockModel oldModel = modelsMap.get(model.getCompanyName());
                        oldModel.setPrice(model.getPrice());
                        oldModel.setPriceChange(model.getPriceChange());
                        oldModel.setPriceChangePercent(model.getPriceChangePercent());
                    }else{
                        modelsMap.put(model.getCompanyName(), model);
                        models.get(0).add(model);
                        companyNames.add(model.getCompanyName());
                    }
                }

                List<String> bookmarkCompanyNames = saveData.getData("Bookmark");
                Log.d("alright", "doInBackground: "+bookmarkCompanyNames.toString());
                models.get(2).clear();
                for(String bcn : bookmarkCompanyNames){
                    if(modelsMap.containsKey(bcn)){
                        models.get(2).add(modelsMap.get(bcn));
                    }
                }

                List<String> recentCompanyNames = saveData.getData("Recent");
                Log.d("alright", "doInBackground: "+recentCompanyNames.toString());
                models.get(1).clear();
                for(int n=recentCompanyNames.size(), i=n-1; i>=0; i--){
                    if(modelsMap.containsKey(recentCompanyNames.get(i))){
                        models.get(1).add(modelsMap.get(recentCompanyNames.get(i)));
                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                Log.d("alright", e.getMessage());
            }
            return null;
        }
    }
}