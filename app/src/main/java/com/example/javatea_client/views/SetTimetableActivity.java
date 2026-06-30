package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.TimetableViewModel;

import java.util.ArrayList;
import java.util.List;

public class SetTimetableActivity extends AppCompatActivity {

    //使用するViewModel
    TimetableViewModel timetableViewModel;
    CategoryViewModel categoryViewModel;

    private static final String TAG = "SetTimetableActivity"; //log(デバッグ)用のTAG宣言


    //Javateaクラスからデータを受け取るための変数
    private String userId;
    private String token;
    private String university;
    private String faculty;
    private String department;

    List<Lecture> lecturesList = new ArrayList<>(); //授業情報のリスト
    RecyclerView recyclerView; //RecyclerViewのフィールドを宣言

    //Observe
    private void setupObservers() {

        // 検索結果の取得
        categoryViewModel.getSearchLectureResults().observe(this, filteredLectures -> {
            if(filteredLectures != null) {
                Log.d(TAG, "個人用の授業一覧を受信：" + filteredLectures.size());
                //リストの更新
                lecturesList.clear(); //過去のリストの内容を削除
                lecturesList.addAll(filteredLectures);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            for(Lecture lecture : filteredLectures) {
                Log.d(TAG, "授業名：" + lecture.getName());
            }

        });

        // リストの科目を選択した後(timetable更新)を検知して画面遷移
        timetableViewModel.getLectures().observe(this, new Observer<List<Lecture>>() {
            @Override
            public void onChanged(List<Lecture> lectureList) {
                if (lectureList != null) {
                    Intent intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                    startActivity(intent);
                }
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_timetable);
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
        String semester = reIntent.getStringExtra("semester");

        // 他のActivityから画面を取得
        Navigation.setup(this); //Navigationクラスを動かす
        ModeBar.setup(this, "時間割設定"); //ModeBarを設定

        //ViewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        //ユーザ情報の取得
        Javatea javaTea = (Javatea) getApplication();
        javaTea.setView("SetTimetable");
        userId = javaTea.getUserId();
        token = javaTea.getToken();
        university = javaTea.getUniversity();
        faculty = javaTea.getFaculty();
        department = javaTea.getDepartment();


        // Javateaクラスから取ってくるであろう所属大学、所属学部、所属学科を使って通信を呼ぶ(今は甲南大学を入れてる状態)
        categoryViewModel.loadUniversityLectures(university);
        categoryViewModel.loadFacultyLectures(university, faculty);
        categoryViewModel.loadDepartmentLectures(university, faculty, department);
        // Timetableから受けた検索項目をViewModelに送信(periodだけint型です)
        categoryViewModel.searchLectures(semester, day, period);

        //リストの生成
        recyclerView = findViewById(R.id.lecture_name_list); //RecyclerViewにidを紐づけ(lecture_name_listはxmlファイル内)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LectureAdapter(lecturesList, timetableViewModel, userId, token, year)); //Adapterにこの画面の情報と科目の情報を渡す



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
                Intent intent = new Intent(SetTimetableActivity.this, AddLectureActivity.class);
                //科目追加画面に曜日と時間を渡す
                intent.putExtra("day",day);
                intent.putExtra("period",period);
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
    }
}