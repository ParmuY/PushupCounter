package com.parmu.pushupcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class SquatActivity extends AppCompatActivity {
    final static String PREF_HIGH_SCORE_FILE_NAME = "com.parmu.pushupcounter.HighScore";
    private  SharedPreferences prefHighScoreSquat;
    private int highScoreSquat;
    private TextView recentSquatTextView;
    private int numberOfSquat;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);
        bottomNavigation();
        hamburgerToolbarActionBar();
        prefHighScoreSquat = getSharedPreferences(PREF_HIGH_SCORE_FILE_NAME,MODE_PRIVATE);
        highScoreSquat = prefHighScoreSquat.getInt("squathighscore",1);
        recentSquatTextView = findViewById(R.id.squat_count_display);
        ImageButton refreshButton = findViewById(R.id.squat_refresh);
        refreshButton.setOnClickListener(v -> {numberOfSquat=0; recentSquatTextView.setText(String.valueOf(numberOfSquat));  });
        Button startSitupCounter = findViewById(R.id.start_squat_counter);
        startSitupCounter.setOnClickListener(v -> {
            Intent iStartSitupCounter = new Intent(SquatActivity.this, SquatCounter.class);
            startActivity(iStartSitupCounter);
        });
        numberOfSquat =  getIntent().getIntExtra("numberofsquat",0);
        recentSquatTextView.setText(String.valueOf(numberOfSquat));
        displayHighScore();
    }
    @SuppressLint("NonConstantResourceId")
    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pushup_item:
                    Intent iPushup = new Intent(getApplicationContext(), MainActivity.class);
                    iPushup.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    iPushup.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(iPushup);
                    return true;
                case R.id.squat_item:
                    Intent iSquat = new Intent(getApplicationContext(), SquatActivity.class);
                    iSquat.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    iSquat.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(iSquat);
                    return true;
                case R.id.more_item:
                    Toast.makeText(getApplicationContext(),"more settings",Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        });
    }

    private void displayHighScore() {
            if (numberOfSquat >= highScoreSquat) {
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
        },3000);
    }
    @Override
    public void onPause(){
        super.onPause();
        overridePendingTransition(0,0);
    }
    // when hamberger is clicked  then drawer opens
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    private void hamburgerToolbarActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Squats Counter");
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple_shade));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        ConstraintLayout constraintLayoutHeader = headerView.findViewById(R.id.header_constraint_layout);
        constraintLayoutHeader.setBackgroundColor(getResources().getColor(R.color.purple_shade));
        CircleImageView headerIcon = constraintLayoutHeader.findViewById(R.id.header_image_icon);
        headerIcon.setImageResource(R.drawable.squat_img_300x416);
        TextView headerTextView = constraintLayoutHeader.findViewById(R.id.header_text_view);
        headerTextView.setText("Squats Counter");

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        //listener for navigation item click
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.share_drawer:
                    menuItem.setChecked(true);
                    Toast.makeText(getApplicationContext(), "Share app", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.rate:
                    menuItem.setChecked(true);
                    Toast.makeText(getApplicationContext(), "Rate", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.about:
                    menuItem.setChecked(true);
                    Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
            }
            return true;
        });
    }
}