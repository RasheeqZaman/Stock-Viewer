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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    StockTopAdapter adapter;
    List<StockModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        models = new ArrayList<>();
        models.add(new StockModel("ACI", 250.50, 4.20, 1.65, false));
        models.add(new StockModel("ACMELAB", 72.20, 0.50, 0.69, false));
        models.add(new StockModel("AGRANINS", 36.00, 0.50, 1.41, true));
        models.add(new StockModel("AIL", 31.30, 0.10, 0.32, false));
        models.add(new StockModel("AIBL1STMF", 7.70, 0.10, 1.32, true));

        adapter = new StockTopAdapter(models, this);

        viewPager = findViewById(R.id.top_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));

        Timer timer = playSlideViewPager(viewPager, models.size());

        ImageButton playPauseBtn = (ImageButton)findViewById(R.id.play_pause);
        playPauseBtn.setOnClickListener(new PlayPauseSliderOnClickListener(playPauseBtn, timer, viewPager, models.size()));
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