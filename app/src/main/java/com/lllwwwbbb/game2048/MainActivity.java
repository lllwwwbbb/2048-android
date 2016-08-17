package com.lllwwwbbb.game2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tvScore;
    private TextView tvBest;
    private Toolbar toolbar;
    private int score;
    private int best;
    private static MainActivity mainActivity = null;

    public final String SP_FILE = "game2048";
    public final String BEST_KEY = "best";
    public final String SCORE_KEY = "score";
    public final String SQUARE_KEY = "square";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.app_version);
        toolbar.setSubtitle(R.string.app_version_number);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_restart :
                        GameView.getGameView().onRestart();
                        break;
                    default:
                }
                return true;
            }
        });

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBest = (TextView) findViewById(R.id.tvBest);
        SharedPreferences sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        best = sp.getInt(BEST_KEY, 0);
        tvBest.setText(best + "");

        mainActivity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        clearScore();
        addScore(savedInstanceState.getInt(SCORE_KEY));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_KEY, score);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearScore() {
        this.score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
        tvBest.setText(best + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }

    public int getScore() {
        return score;
    }

    public void setBest(int s) {
        best = s;
        showScore();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
}
