package com.example.javatea_client.views;


import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javatea_client.R;

public class Navigation {
    //Navigationのインスタンスを作る必要なく呼び出せるようにする
    public static void setup(AppCompatActivity activity){
        ImageButton timetableButton = (ImageButton) activity.findViewById(R.id.timetable_button);
        timetableButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.startActivity(intent);
            }
        });

        ImageButton qaButton = (ImageButton) activity.findViewById(R.id.qa_button);
        qaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.startActivity(intent);
            }
        });

        ImageButton searchButton = (ImageButton) activity.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.startActivity(intent);
            }
        });

        ImageButton notificationButton = (ImageButton) activity.findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.startActivity(intent);
            }
        });

        ImageButton myPageButton = (ImageButton) activity.findViewById(R.id.myPage_button);
        myPageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.startActivity(intent);
            }
        });
    }

}