package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.LectureViewModel;
import com.example.javatea_client.viewModels.TimetableViewModel;

import java.util.List;

public class OtherLecturesActivity extends AppCompatActivity {
    TimetableViewModel timetableViewModel;
    LinearLayout otherLectures;
    String userId;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_other_lectures);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        //前の画面から年度をもらってくる。
        int year = intent.getIntExtra("year",0);
        Log.d("year", String.valueOf(year));
        //ModeBarのセットアップ
        ModeBar.setup(this,"時間割");
        //Navigationのセットアップ
        Navigation.setup(this);

//        Javatea javatea = (Javatea) getApplication();
        userId = "test01";
        token = "696a3b79-8d35-4a26-a9cb-39def76fbc28";

        //viewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        //エラー表示
        timetableViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    Log.d("Error",s);
                    Toast.makeText(OtherLecturesActivity.this,s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        timetableViewModel.loadTimetableLectures(userId,year,token);
        timetableViewModel.getLectures().observe(this, new Observer<List<Lecture>>() {
            @Override
            public void onChanged(List<Lecture> strings) {
                Log.d("change","onChanged");
                for(Lecture lecture : strings){
                    String name = lecture.getName();
                    Integer grade = lecture.getGrade();
                    String semester = lecture.getSemester();
                    Integer frame = lecture.getFrame();//コマ数
                    String day = lecture.getDay();
                    Integer period = lecture.getPeriod();
                    if(semester.equals("その他")){
                        TextView lectureTextView = createLectureTextView(name);
                        lectureTextView.setText(name);
                        lectureTextView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Intent intent = new Intent(OtherLecturesActivity.this,SetTimetableActivity.class);
                                intent.putExtra("year",year);
                                startActivity(intent);
                                return true;
                            }
                        });
                        otherLectures.addView(lectureTextView);
                    }
                }
            }
        });
        otherLectures = findViewById(R.id.otherLectures);
    }
    private TextView createLectureTextView(String name){
        TextView lecture = new TextView(this);
        lecture.setBackgroundResource(R.drawable.cell_border);
        lecture.setTextSize(20);
        lecture.setGravity(Gravity.CENTER);
        return lecture;
    }
    private float startY;
    private static final float SWIPE_THRESHOLD = 300f;
    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        // if(!isDecidedYear){
        // return super.dispatchTouchEvent(ev);
        // }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY(); break;
            case MotionEvent.ACTION_UP:
                float endY = ev.getY();
                float diffY = endY - startY; // 画面上部から下方向へスワイプ
                if (diffY >= SWIPE_THRESHOLD) {
                    Intent intent = new Intent(this, TimetableActivity.class);
                    startActivity(intent);
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}