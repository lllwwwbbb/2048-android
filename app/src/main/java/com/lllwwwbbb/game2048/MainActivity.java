package com.lllwwwbbb.game2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvScore;
    private TextView tvBest;
    private int score;
    private int best;
    private static MainActivity mainActivity = null;

    public final String SP_FILE = "game2048";
    public final String BEST_KEY = "best";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBest = (TextView) findViewById(R.id.tvBest);
        SharedPreferences sp = getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        best = sp.getInt(BEST_KEY, 0);
        tvBest.setText(best + "");

        mainActivity = this;
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
