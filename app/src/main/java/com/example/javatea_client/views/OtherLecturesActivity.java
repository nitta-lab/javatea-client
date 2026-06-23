package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    LectureViewModel lectureViewModel;
    LinearLayout otherLectures;
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
        //ModeBarのセットアップ
        ModeBar.setup(this,"時間割");
        //Navigationのセットアップ
        Navigation.setup(this);

        Javatea javatea = (Javatea) getApplication();
        String userId = javatea.getUserId();
        String token = javatea.getToken();

        //viewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
//        lectureViewModel = new ViewModelProvider(this).get(LectureViewModel.class);
        timetableViewModel.loadTimetableLectures(userId,year,token);
        timetableViewModel.getLectures().observe(this, new Observer<List<Lecture>>() {
            @Override
            public void onChanged(List<Lecture> strings) {
                for(Lecture lectureId : strings){
                    //Lecture lecture = lectureViewModel.getLecture(lectureId);
                    String name = "基礎英語";
                    Integer grade = 2;
                    String semester = "前期";
                    Integer frame = 2;//コマ数
                    String day = "水";
                    Integer period = 2;
                    if(semester.equals("その他")){
                        TextView lecture = createLectureTextView(name);
                        lecture.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Intent intent = new Intent(OtherLecturesActivity.this,SetTimetableActivity.class);
                                intent.putExtra("year",year);
                                startActivity(intent);
                                return true;
                            }
                        });
                        otherLectures.addView(lecture);
                    }
                }
            }
        });
        otherLectures = findViewById(R.id.otherLectures);
        TextView lecture = new TextView(this);
        lecture.setBackgroundResource(R.drawable.cell_border);
        lecture.setTextSize(20);
        lecture.setGravity(Gravity.CENTER);
        otherLectures.addView(lecture);
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