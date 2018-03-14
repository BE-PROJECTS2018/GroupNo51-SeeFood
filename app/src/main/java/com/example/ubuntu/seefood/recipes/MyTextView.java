package com.example.ubuntu.seefood.recipes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by jayesh on 11/3/18.
 */

public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
        setFont();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AlanisHand.ttf");
        setTypeface(font, Typeface.NORMAL);
    }

}
