package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class SetTimetableActivity extends AppCompatActivity {

    //使用するViewModel
    TimetableViewModel timetableViewModel;
    CategoryViewModel categoryViewModel;

    private static final String TAG = "SetTimetableActivity"; //log(デバッグ)用のTAG宣言


    //Javateaクラスからデータを受け取るための変数
    private String userId;
    private String token;
    private String univId;
    private String facultyName;
    private String departmentName;
    private String grade;
    private int intGrade;

    List<Lecture> lecturesList = new ArrayList<>(); //授業情報のリスト
    RecyclerView recyclerView; //RecyclerViewのフィールドを宣言

    //Timetableから受け取るフィールドを受け取る変数を宣言
    private String day;
    private int period;
    private int year;
    private String semester;
    private String lectureId;

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
        timetableViewModel.getTimetable().observe(this, new Observer<TreeMap<Integer, HashSet<Lecture>>>() {
            @Override
            public void onChanged(TreeMap<Integer, HashSet<Lecture>> timetable) {
                if (timetable != null) {
                    Intent intent;
                    if(semester.equals("その他")){
                        intent = new Intent(SetTimetableActivity.this, OtherLecturesActivity.class);
                    }else{
                        intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                    }
                    intent.putExtra("year",year);
                    intent.putExtra("semester",semester);
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
        day = reIntent.getStringExtra("day");
        period = reIntent.getIntExtra("period", 1);
        year = reIntent.getIntExtra("year", 1);
        semester = reIntent.getStringExtra("semester");
        lectureId = reIntent.getStringExtra("lectureId");
        Log.d(TAG, "day:"+day+"period:"+period+"year:"+year+"semester:"+semester);

        // 他のActivityから画面を取得
        Navigation.setup(this); //Navigationクラスを動かす
        ModeBar.setup(this, "マイページ"); //ModeBarを設定

        //ViewModelの初期化
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        //ユーザ情報の取得
        Javatea javaTea = (Javatea) getApplication();
        javaTea.setView("SetTimetable");
        userId = javaTea.getUserId();
        token = javaTea.getToken();
        univId = javaTea.getUnivId();
        facultyName = javaTea.getFaculty();
        departmentName = javaTea.getDepartment();
        grade = javaTea.getGrade();
        intGrade = Integer.parseInt(grade);
//        university = "univ-id1";
//        faculty = "知能情報学部";
//        department = "知能情報学科";

        setupObservers(); //Observe実行
        //画面遷移時に検索開始
        categoryViewModel.callSearchLectures(univId, facultyName, departmentName, semester, day, period, intGrade);
        Log.d(TAG, "userId:"+userId+", token:"+token+", university:"+univId+", faculty:"+facultyName+", department:"+departmentName);

        //リストの生成
        recyclerView = findViewById(R.id.lecture_name_list); //RecyclerViewにidを紐づけ(lecture_name_listはxmlファイル内)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LectureAdapter(lecturesList, timetableViewModel, userId, token, year)); //Adapterにこの画面の情報と科目の情報を渡す
        Log.d(TAG, "検索結果: "+lecturesList);

        //各ウィジェット動作処理
        //閉じるボタン
        Button closeButton = findViewById(R.id.close_lecture_button);
        closeButton.setOnClickListener(new View.OnClickListener() { //クリック待機
            public void onClick(View v) { //クリックされたとき
                finish();
            }
        });

        //科目追加ボタン
        Button addButton = findViewById(R.id.add_lecture_button);
        addButton.setOnClickListener(new View.OnClickListener() { //クリック待機
            public void onClick(View v) { //クリックされたとき
                Intent intent = new Intent(SetTimetableActivity.this, AddLectureActivity.class);
                //科目追加画面に曜日と時間を渡す
                intent.putExtra("day",day);
                intent.putExtra("period",period);
                intent.putExtra("semester", semester);

                startActivityForResult(intent, 200); //AddLectureActivityから返ってきたことを示すフラグ(startActivityForResultは現在は非推奨)
            }
        });

        //"削除"ボタン
        Button cancelButton = findViewById(R.id.cancel_lecture_button);
        // lectureIdがnullなら押せなくする
        if(lectureId != null) {
            cancelButton.setEnabled(true);
        } else {
            cancelButton.setEnabled(false);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() { //クリック待機
            public void onClick(View v) { //クリックされたとき
                if(lectureId != null) { //すでに授業が入っていた時
                    timetableViewModel.removeLecture(userId, year, lectureId, token);
                    Intent intent;
                    if(semester.equals("その他")){
                        intent = new Intent(SetTimetableActivity.this, OtherLecturesActivity.class);
                    }else{
                        intent = new Intent(SetTimetableActivity.this, TimetableActivity.class);
                    }
                    intent.putExtra("year",year);
                    intent.putExtra("semester",semester);
                    startActivity(intent);
//                    finish();
                }
            }
        });
    }

    //AddLectureActivityから帰ってきた後に再検索
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200) {
            if(resultCode == RESULT_OK) {
                Log.d(TAG, "科目追加成功、再検索をします");

                // ViewModelに検索の通信する
                categoryViewModel.callSearchLectures(univId, facultyName, departmentName, semester, day, period, intGrade);

            } else if(resultCode == RESULT_CANCELED) {
                Log.d(TAG, "科目追加キャンセル");
            }
        }
    }
}