package com.lllwwwbbb.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lllwwwbbb on 16-8-14.
 */
public class GameView extends GridLayout {
    private Card[][] cardMap = new Card[this.ROW][this.COL];
    private boolean isStart;
    private boolean isMoved;
    private List<Point> emptyPoints = new ArrayList<>();
    private List<Card> cardList = new ArrayList<>();

    private final int ROW = 4;
    private final int COL = 4;
    private final int MARGIN = 10;
    private final int BGCOLOR = 0xFFBBADA0;
    private final String CARD_KEY = "card";
    private final String START_KEY = "start";

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attr) {
        super(context, attr);
        initGameView();
    }

    public GameView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initGameView();
    }

    private void initGameView() {
        this.isStart = true;
        setColumnCount(this.COL);
        setBackgroundColor(this.BGCOLOR);

        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;
            private float endX, endY;
            private float offsetX, offsetY;

            private final float OFFSET = 100;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();

                        offsetX = endX - startX;
                        offsetY = endY - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -OFFSET) {
                                moveLeft();
                            } else if (offsetX > OFFSET) {
                                moveRight();
                            }
                        } else {
                            if (offsetY < -OFFSET) {
                                moveUp();
                            } else if (offsetY > OFFSET) {
                                moveDown();
                            }
                        }
                }
                return true;
            }
        });
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(this.toString(), super.onSaveInstanceState());
        bundle.putBoolean(START_KEY, isStart);
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                bundle.putInt(r * COL + c + CARD_KEY, cardMap[r][c].getNumber());
            }
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isStart = bundle.getBoolean(START_KEY);
            for (int r = 0; r < ROW; r++) {
                for (int c = 0; c < COL; c++) {
                    if (cardMap[r][c] == null) {
                        cardMap[r][c] = new Card(getContext());
                    }
                    cardMap[r][c].setNumber(bundle.getInt(r * COL + c + CARD_KEY, 0));
                }
            }
            super.onRestoreInstanceState(bundle.getParcelable(this.toString()));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardSize = (Math.min(w, h) - this.MARGIN) / 4;
        addCards(cardSize);

        if (this.isStart) {
            startGame();
            this.isStart = false;
        }
    }

    private void addCards(int cardSize) {
        for (int r = 0; r < this.ROW; r++) {
            for (int c = 0; c < this.COL; c++) {
                if (cardMap[r][c] == null) {
                    cardMap[r][c] = new Card(getContext());
                }
                addView(cardMap[r][c], cardSize, cardSize);
            }
        }
    }

    private void addRandomNumber() {
        this.emptyPoints.clear();

        for (int r = 0; r < this.ROW; r++) {
            for (int c = 0; c < this.COL; c++) {
                if (cardMap[r][c].getNumber() <= 0) {
                    emptyPoints.add(new Point(r, c));
                }
            }
        }

        Point point = this.emptyPoints.remove((int)(Math.random() * this.emptyPoints.size()));
        cardMap[point.x][point.y].setNumber(Math.random() > 0.1 ? 2 : 4);
    }

    private void startGame() {
        MainActivity.getMainActivity().clearScore();

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                cardMap[r][c].setNumber(0);
            }
        }

        this.addRandomNumber();
        this.addRandomNumber();
    }

    private void moveLeft() {
        this.isMoved = false;
        for (int r = 0; r < this.ROW; r++) {
            cardList.clear();
            boolean empty = false;
            for (int i = 0; i < this.COL; i++) {
                if (cardMap[r][i].getNumber() > 0) {
                    Card card = new Card(getContext());
                    card.setNumber(cardMap[r][i].getNumber());
                    cardList.add(card);
                    cardMap[r][i].setNumber(0);
                    if (empty) {
                        this.isMoved = true;
                    }
                } else {
                    empty = true;
                }
            }
            mergeCard();
            for (int i = 0; i < cardList.size(); i++) {
                cardMap[r][i].setNumber(cardList.get(i).getNumber());
            }
        }
        if (this.isMoved) {
            addRandomNumber();
            if (isOver()) {
                gameOver();
            }
        }
    }

    private void moveRight() {
        this.isMoved = false;
        for (int r = 0; r < this.ROW; r++) {
            cardList.clear();
            boolean empty = false;
            for (int i = this.COL - 1; i >= 0; i--) {
                if (cardMap[r][i].getNumber() > 0) {
                    Card card = new Card(getContext());
                    card.setNumber(cardMap[r][i].getNumber());
                    cardList.add(card);
                    cardMap[r][i].setNumber(0);
                    if (empty) {
                        this.isMoved = true;
                    }
                } else {
                    empty = true;
                }
            }
            mergeCard();
            for (int i = 0; i < cardList.size(); i++) {
                cardMap[r][this.COL - 1 - i].setNumber(cardList.get(i).getNumber());
            }
        }
        if (this.isMoved) {
            addRandomNumber();
            if (isOver()) {
                gameOver();
            }
        }
    }

    private void moveUp() {
        this.isMoved = false;
        for (int c = 0; c < this.COL; c++) {
            cardList.clear();
            boolean empty = false;
            for (int i = 0; i < this.ROW; i++) {
                if (cardMap[i][c].getNumber() > 0) {
                    Card card = new Card(getContext());
                    card.setNumber(cardMap[i][c].getNumber());
                    cardList.add(card);
                    cardMap[i][c].setNumber(0);
                    if (empty) {
                        this.isMoved = true;
                    }
                } else {
                    empty = true;
                }
            }
            mergeCard();
            for (int i = 0; i < cardList.size(); i++) {
                cardMap[i][c].setNumber(cardList.get(i).getNumber());
            }
        }
        if (this.isMoved) {
            addRandomNumber();
            if (isOver()) {
                gameOver();
            }
        }
    }

    private void moveDown() {
        this.isMoved = false;
        for (int c = 0; c < this.COL; c++) {
            cardList.clear();
            boolean empty = false;
            for (int i = this.ROW - 1; i >= 0; i--) {
                if (cardMap[i][c].getNumber() > 0) {
                    Card card = new Card(getContext());
                    card.setNumber(cardMap[i][c].getNumber());
                    cardList.add(card);
                    cardMap[i][c].setNumber(0);
                    if (empty) {
                        this.isMoved = true;
                    }
                } else {
                    empty = true;
                }
            }
            mergeCard();
            for (int i = 0; i < cardList.size(); i++) {
                cardMap[this.ROW - 1 - i][c].setNumber(cardList.get(i).getNumber());
            }
        }
        if (this.isMoved) {
            addRandomNumber();
            if (isOver()) {
                gameOver();
            }
        }
    }

    private void mergeCard() {
        //merge the cards in cardList, the direction is LEFT
        if (cardList.size() <= 1) {
            return;
        }
        for (int i = 0; i < cardList.size() - 1; i++) {
            int suc = i + 1;
            if (cardList.get(i).eqaul(cardList.get(suc))) {
                this.isMoved = true;
                cardList.get(i).setNumber(cardList.get(i).getNumber() * 2);
                cardList.remove(suc);
                MainActivity.getMainActivity().addScore(cardList.get(i).getNumber());
                updateBest();
            }
        }
    }

    private boolean isOver() {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (cardMap[r][c].getNumber() <= 0 ||
                        (r > 0 && cardMap[r][c].eqaul(cardMap[r-1][c])) ||
                        (r < ROW - 1 && cardMap[r][c].eqaul(cardMap[r+1][c])) ||
                        (c > 0 && cardMap[r][c].eqaul(cardMap[r][c-1])) ||
                        (c < COL - 1 && cardMap[r][c].eqaul(cardMap[r][c+1]))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void gameOver() {
        new AlertDialog.Builder(getContext()).setTitle(R.string.app_name).setMessage(R.string.text_gameover)
                .setPositiveButton(R.string.button_restart,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                }).setNegativeButton(R.string.button_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }

    private void updateBest() {
        int bestScore, score;
        SharedPreferences sp = getContext().getSharedPreferences(MainActivity.getMainActivity().SP_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        score = MainActivity.getMainActivity().getScore();
        bestScore = sp.getInt(MainActivity.getMainActivity().BEST_KEY, 0);
        if (score > bestScore) {
            editor.putInt(MainActivity.getMainActivity().BEST_KEY, score);
            MainActivity.getMainActivity().setBest(score);
            editor.commit();
        }
    }
}
