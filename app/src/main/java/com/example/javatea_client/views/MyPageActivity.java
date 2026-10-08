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
import com.example.javatea_client.viewModels.UserViewModel;

//BAG:ログアウトボタンを押しても時間割画面へ画面遷移をしてしまう(値を消す処理は正常)

public class MyPageActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    Javatea javaTea;

    private static final String TAG = "MyPageActivity"; //デバッグ用


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

        //JavaTeaの初期化
        javaTea = (Javatea)getApplication();
        javaTea.setView("MyPageActivity");


        //画面テキストとの対応を設定
        TextView nikName = findViewById(R.id.nickname_text);
        TextView universityText = findViewById(R.id.university_text);
        TextView facultyText = findViewById(R.id.faculty_text);
        TextView departmentText = findViewById(R.id.department_text);
        TextView gradeText = findViewById(R.id.grade_text);

        nikName.setText(javaTea.getName());
        universityText.setText(javaTea.getUniversity());
        facultyText.setText(javaTea.getFaculty());
        departmentText.setText(javaTea.getDepartment());
        gradeText.setText(javaTea.getGrade());



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