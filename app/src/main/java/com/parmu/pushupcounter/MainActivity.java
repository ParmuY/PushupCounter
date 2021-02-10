package com.parmu.pushupcounter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class MainActivity extends AppCompatActivity{
    private int numberOfPushups;
    private TextView recentPushupsTextView;
    final static String PREF_HIGH_SCORE_FILE_NAME = "com.parmu.pushupcounter.HighScore";
    private SharedPreferences prefHighScore;
    private int highScore;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(MainActivity.this, " sucessfull initi ", Toast.LENGTH_SHORT).show();
            }
        });

        admobBanner();
        hamburgerToolbarActionBar();
        prefHighScore = getSharedPreferences(PREF_HIGH_SCORE_FILE_NAME,MODE_PRIVATE);
        highScore = prefHighScore.getInt("highscore",0);
        recentPushupsTextView = findViewById(R.id.text_view_count_display);
        ImageButton refreshButton = findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(v -> {numberOfPushups=0; recentPushupsTextView.setText(String.valueOf(numberOfPushups));  });
        Button startCounterButton = findViewById(R.id.button_start_counter);
        startCounterButton.setOnClickListener(v -> {
            Intent iCountup = new Intent(MainActivity.this, CountupActivity.class);
            startActivity(iCountup);
        });
        //numberof pushups got from countup activity as intentextra
        numberOfPushups =  getIntent().getIntExtra("numberofpushups",0);
        recentPushupsTextView.setText(String.valueOf(numberOfPushups));
        displayHighScore();
    }

    private void admobBanner() {
        mAdView = findViewById(R.id.adView);
        //remove this code for release
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1B17F2F74B21C1CC8F0D69FD617DFA0E"))
                        .build());
        //remove the code above when releasing
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    // when hamberger is clicked  then drawer opens
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.situp_counter:
                Intent iToSitupActivity = new Intent(MainActivity.this, SquatActivity.class);
                startActivity(iToSitupActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayHighScore() {
        SharedPreferences prefIsFirstRun = getSharedPreferences("com.parmu.pushupcounter.isfirstrun", MODE_PRIVATE);
        boolean isFirstRun = prefIsFirstRun.getBoolean("isfirstrun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editorIsFirstRun = prefIsFirstRun.edit();
            editorIsFirstRun.putBoolean("isfirstrun", false).apply();
        }

        if(!isFirstRun) {
            if (numberOfPushups >= highScore) {
                final KonfettiView konfettiView = findViewById(R.id.view_konfetti);
                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(0.0f, konfettiView.getWidth() + 1000f, -50f, -50f)
                        .streamFor(300, 3000L);
                        openDialog();
            }

        }
    }
    public void openDialog(){
        DialogHighScore dialog = new DialogHighScore();
        dialog.show(getSupportFragmentManager(),"tag highscore dialog");
        //close the dialog box after few seconds
        final Timer timerDismissDialog = new Timer ();
        timerDismissDialog.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                timerDismissDialog.cancel();
            }
        },4000);
    }
    @SuppressLint("NonConstantResourceId")

    private void hamburgerToolbarActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Pushup Counter");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        //listener for navigation item click
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.share_drawer:
                    menuItem.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Share app", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.rate:
                    menuItem.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Rate", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.about:
                    menuItem.setChecked(false);
                    Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
            }
            return true;
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        if(mAdView!=null){mAdView.resume();
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if(mAdView!=null){
            mAdView.pause(); }
    }
    @Override
    public void onDestroy(){
        if(mAdView!=null){
            mAdView.destroy();
            super.onDestroy();
        }
    }


}