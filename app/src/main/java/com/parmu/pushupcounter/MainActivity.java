package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Color;

import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class MainActivity extends AppCompatActivity{
    int numberOfPushups;
    TextView recentPushupsTextView;
    final static String PREF_HIGH_SCORE_FILE_NAME = "com.parmu.pushupcounter.HighScore";
    SharedPreferences prefHighScore;
    private int highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        },5000);
    }

}