package com.example.javatea_client.views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.TimetableViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

public class MyPageActivity extends AppCompatActivity {

    UserViewModel userViewModel;

    private static final String TAG = "MyPageActivity"; //デバッグ用

    // UserViewModelからのデータを入れるフィールド
    private String name;

    private void setUpObservers(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 他のActivityから画面を取得
        Navigation.setup(this); //Navigationクラスを動かす
        ModeBar.setup(this, "時間割設定"); //ModeBarを設定

        // ViewModelの初期化
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

//        //ユーザ情報の取得
//        Javatea javaTea = (Javatea) getApplication();
//        javaTea.setView("MyPage");
//        //Javateaから取得する情報を入れるフィールド
//        String userId = javaTea.getUserId();
//        String token = javaTea.getToken();
//        String univId = javaTea.getUnivId();
//        String facultyName = javaTea.getFaculty();
//        String departmentName = javaTea.getDepartment();
//        String grade = javaTea.getGrade();

        String univId = "甲南大学";
        String facultyName = "知能情報学部";
        String departmentName = "知能情報学科";
        String grade = "2";


        setUpObservers();

        //ニックネームを代入


        //大学名を代入
        TextView universityText = findViewById(R.id.university_text);
        universityText.setText(univId);

        //学部名を代入
        TextView facultyText = findViewById(R.id.faculty_text);
        facultyText.setText(facultyName);

        //学科名を代入
        TextView departmentText = findViewById(R.id.department_text);
        departmentText.setText(departmentName);

        //学年を代入
        TextView gradeText = findViewById(R.id.grade_text);
        gradeText.setText(grade);

        //ログアウトボタン
    }
}