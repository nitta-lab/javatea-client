package com.example.javatea_client.views;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

import com.example.javatea_client.R;

public class ModeBar {

    public static void setup(Activity activity, String text) {
        TextView barText = activity.findViewById(R.id.modebar);
        barText.setText(text);
        barText.setTypeface(null, Typeface.BOLD);
    }

}