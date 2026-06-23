package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.TimetableViewModel;

import java.util.ArrayList;
import java.util.List;

public class SetTimetableActivity extends AppCompatActivity {

    //使用するViewModelを記述
    TimetableViewModel timetableViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_timetable);
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent reIntent = getIntent();
        // TimetableActivityから、曜日と時間を取得
        String day = reIntent.getStringExtra("day");
        int period = reIntent.getIntExtra("period", 1);
        int year = reIntent.getIntExtra("year", 1);
        int lectureId = reIntent.getIntExtra("lectureId", 1);
        String semester = reIntent.getStringExtra("semester");

        // 他のActivityから画面を取得
        Navigation.setup(this); //Navigationクラスを動かす
        ModeBar.setup(this, "時間割設定"); //ModeBarを設定

        //各ウィジェット動作処理
        //閉じるボタン
        Button closeButton = findViewById(R.id.close_lecture_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        //科目追加ボタン(後でID変更必須 現在は時間割画面に遷移)
        Button addButton = findViewById(R.id.add_lecture_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                //科目追加画面に曜日と時間を渡す
                intent.putExtra("day",day);
                intent.putExtra("period",period);
                intent.putExtra("year", year);
                intent.putExtra("semester", semester);
                startActivity(intent);
            }
        });

        //"設定しない"ボタン
        Button cancelButton = findViewById(R.id.cancel_lecture_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        // ダミーデータ（動作確認用）
        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(new Lecture("プログラミング演習 I", 1, "前期", 1, "月", 1));
        lectureList.add(new Lecture("オブジェクト指向プログラミング", 2, "前期", 2, "火", 2));
        lectureList.add(new Lecture("データベース", 3, "後期", 3, "水", 3));
        lectureList.add(new Lecture("データベース", 3, "後期", 3, "水", 3));
        lectureList.add(new Lecture("データベース", 3, "後期", 3, "水", 3));
        lectureList.add(new Lecture("データベース", 3, "後期", 3, "水", 3));
        lectureList.add(new Lecture("データベース", 3, "後期", 3, "水", 3));

        RecyclerView recyclerView = findViewById(R.id.lecture_name_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LectureAdapter(lectureList));

    }
}