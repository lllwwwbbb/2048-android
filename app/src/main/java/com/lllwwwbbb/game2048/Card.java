package com.lllwwwbbb.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lllwwwbbb on 16-8-14.
 */
public class Card extends FrameLayout {
    private int number = 0;
    private TextView tvNumber;

    private final float TEXTSIZE = 32;
    private final int BGCOLOR = 0x33FFFFFF;
    private final int MARGIN = 10;

    public Card(Context context) {
        super(context);

        tvNumber = new TextView(getContext());
        tvNumber.setTextSize(this.TEXTSIZE);
        tvNumber.setGravity(Gravity.CENTER);
        tvNumber.setBackgroundColor(this.BGCOLOR);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(this.MARGIN, this.MARGIN, 0, 0);
        addView(tvNumber, lp);

        setNumber(0);
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
        if (number > 0) {
            tvNumber.setText(number + "");
        } else {
            tvNumber.setText("");
        }
        switch (number) {
            case 0:
                tvNumber.setBackgroundColor(BGCOLOR);
                break;
            case 2:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2));
                tvNumber.setTextColor(getResources().getColor(R.color.fg2));
                break;
            case 4:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg4));
                tvNumber.setTextColor(getResources().getColor(R.color.fg4));
                break;
            case 8:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg8));
                tvNumber.setTextColor(getResources().getColor(R.color.fg8));
                break;
            case 16:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg16));
                tvNumber.setTextColor(getResources().getColor(R.color.fg16));
                break;
            case 32:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg32));
                tvNumber.setTextColor(getResources().getColor(R.color.fg32));
                break;
            case 64:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg64));
                tvNumber.setTextColor(getResources().getColor(R.color.fg64));
                break;
            case 128:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg128));
                tvNumber.setTextColor(getResources().getColor(R.color.fg128));
                break;
            case 256:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg256));
                tvNumber.setTextColor(getResources().getColor(R.color.fg256));
                break;
            case 512:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg512));
                tvNumber.setTextColor(getResources().getColor(R.color.fg512));
                break;
            case 1024:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg1024));
                tvNumber.setTextColor(getResources().getColor(R.color.fg1024));
                break;
            case 2048:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2048));
                tvNumber.setTextColor(getResources().getColor(R.color.fg2048));
                break;
            default:
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg_super));
                tvNumber.setTextColor(getResources().getColor(R.color.fg_super));
        }
    }

    public boolean eqaul(Card card) {
        return this.getNumber() == card.getNumber();
    }
}
