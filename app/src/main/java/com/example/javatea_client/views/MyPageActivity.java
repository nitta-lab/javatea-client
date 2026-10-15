package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

//BAG:ログアウトボタンを押しても時間割画面へ画面遷移をしてしまう(値を消す処理は正常)

public class MyPageActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    CategoryViewModel categoryViewModel;
    Javatea javaTea;

    String univId = "";
    TextView nikName;
    TextView universityText;
    TextView facultyText;
    TextView departmentText;
    TextView gradeText;


    private static final String TAG = "MyPageActivity"; //デバッグ用

    private void setUpObservers(){

        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                nikName.setText(user.getName());
                facultyText.setText(user.getFaculty());
                departmentText.setText(user.getDepartment());
                gradeText.setText(Integer.toString(user.getGrade()));
                Log.d(TAG, "ユーザ情報をset");
            }
        });

        userViewModel.getUniversity().observe(this, univId -> {
            if (univId != null) {
                this.univId = univId;
                Log.d(TAG, "ユーザの大学Idを取得");

                categoryViewModel.getUnivInfo(univId);

            }
        });

        categoryViewModel.getCurrentUniversity().observe(this, universityName -> {
            if (universityName != null) {
                universityText.setText(universityName.getName());
                Log.d(TAG, "ユーザの大学名を取得");

            }
        });

    }


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
        ModeBar.setup(this, "マイページ"); //ModeBarを設定

        // ViewModelの初期化
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        //JavaTeaの初期化
        javaTea = (Javatea)getApplication();

        String javateaUid = javaTea.getUserId();
        String javateaToken = javaTea.getToken();


        //画面テキストとの対応を設定
        nikName = findViewById(R.id.nickname_text);
        universityText = findViewById(R.id.university_text);
        facultyText = findViewById(R.id.faculty_text);
        departmentText = findViewById(R.id.department_text);
        gradeText = findViewById(R.id.grade_text);

        universityText.setText(javaTea.getUniversity());

        setUpObservers();
        userViewModel.getUser(javateaUid);
        userViewModel.getUniversityId(javateaUid, javateaToken);


        //ログアウトボタン
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                javaTea.setPassword("");
                javaTea.setToken("");

                //画面遷移
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });

        Log.d(TAG, "MyPageに遷移成功");
    }
}