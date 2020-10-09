package com.example.stockviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    StockTopAdapter adapter;
    List<List<StockModel>> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        String[] tabNames = new String[]{"All", "Recent", "Bookmark"};
        models = new ArrayList<>();
        for(int i=0; i<tabNames.length; i++){
            models.add(new ArrayList<StockModel>());
        }

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        try {
            models.get(0).add(new StockModel("ACI", "ACI Limited", "Equity", "100% 2019, 115% 2018, 115% 2017", "15% 2019, 3.50% 2018", "1R:1(At Par) 1997", "30-Jun", 250.00, 1, 0.40, 573.73, 10.0, 249.00, dateFormat.parse("Oct 07, 2020 11:40 AM")));
            models.get(0).add(new StockModel("ACMELAB", "The ACME Laboratories Limited", "Equity", "35% 2019, 35% 2018", "-", "-", "30-Jun", 71.10, 1.1, 1.57, 2116.00, 10.0, 70.00, dateFormat.parse("Oct 07, 2020 12:28 PM")));
            models.get(0).add(new StockModel("AGRANINS", "Agrani Insurance Co. Ltd.", "Equity", "10% 2019, 5% 2017", "5% 2018, 5% 2017", "n/a", "31-Dec", 38.00,  	0.9, 2.43, 302.45, 10.0, 37.50, dateFormat.parse("Oct 07, 2020 12:32 PM")));
            models.get(0).add(new StockModel("AIL", "Alif Industries Limited", "Equity", "3% 2019, 25% 2018", "7% 2019, 10% 2018", "-", "30-Jun", 33.00, 0.4, 1.23, 442.52, 10.0, 32.90, dateFormat.parse("Oct 08, 2020 4:00 PM")));
            models.get(0).add(new StockModel("AIBL1STMF", "AIBL 1st Islamic Mutual Fund", "Mutual Fund", "8% 2019, 8% 2018", "-", "-", "31-Mar", 7.90, -0.1, 1.25, 1000.00, 10.0, 8.10, dateFormat.parse("Oct 08, 2020 4:00 PM")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        models.get(1).add(models.get(0).get(1));
        models.get(1).add(models.get(0).get(4));

        models.get(2).add(models.get(0).get(0));
        models.get(2).add(models.get(0).get(2));
        models.get(2).add(models.get(0).get(4));

        adapter = new StockTopAdapter(models.get(0), this);

        viewPager = findViewById(R.id.top_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));

        Timer timer = playSlideViewPager(viewPager, models.get(0).size());

        ImageButton playPauseBtn = (ImageButton)findViewById(R.id.play_pause);
        playPauseBtn.setOnClickListener(new PlayPauseSliderOnClickListener(playPauseBtn, timer, viewPager, models.size()));


        TabLayout stockFragmentTabLayout = findViewById(R.id.stock_fragment_tab_layout);
        for(String tabName : tabNames){
            stockFragmentTabLayout.addTab(stockFragmentTabLayout.newTab().setText(tabName));
        }

        final ViewPager fragmentViewPager = findViewById(R.id.fragment_viewpager);
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

    }

    private Timer playSlideViewPager(ViewPager viewPager, int totalItems) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTask(viewPager, totalItems), 3*1000, 3*1000);
        return timer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
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
                timer = playSlideViewPager(viewPager, totalItems);
            }else{
                timer.cancel();
            }
            currentlyPaused = !currentlyPaused;
        }
    }

    public class SliderTask extends TimerTask {
        private ViewPager viewPager;
        private int totalItems;

        public SliderTask(ViewPager viewPager, int totalItems) {
            this.viewPager = viewPager;
            this.totalItems = totalItems;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    int itemIndex = (viewPager.getCurrentItem() + 1) % totalItems;
                    viewPager.setCurrentItem(itemIndex);
                }
            });
        }
    }
}