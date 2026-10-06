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

public class MyPageActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    Javatea javaTea;

    private static final String TAG = "MyPageActivity"; //デバッグ用

    // UserViewModelからのデータを入れるフィールド
    private TextView nikName;
    private TextView universityText;
    private TextView facultyText;
    private TextView departmentText;

    private TextView gradeText;

    private void setUpObservers(){

        //User情報を取得し、情報を更新
        userViewModel.getUser().observe(this, user -> {

            if(user != null){
                Log.d(TAG, "ユーザ情報取得成功");
            } else {
                Log.d(TAG, "ユーザ情報取得失敗");
                return ;
            }

            nikName.setText(getString(R.string.nikName_format,user.getName()));
            universityText.setText(getString(R.string.university_format, user.getUniversity()));
            facultyText.setText(getString(R.string.faculty_format,user.getFaculty()));
            departmentText.setText(getString(R.string.department_format,user.getDepartment()));
            gradeText.setText(getString(R.string.grade_format, user.getGrade()));

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
        ModeBar.setup(this, "時間割設定"); //ModeBarを設定

        // ViewModelの初期化
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        javaTea = (Javatea)getApplication();
        javaTea.setView("MyPageActivity");

        setUpObservers();

        //画面テキストとの対応を設定
        nikName = findViewById(R.id.nickname_text);
        universityText = findViewById(R.id.university_text);
        facultyText = findViewById(R.id.faculty_text);
        departmentText = findViewById(R.id.department_text);
        gradeText = findViewById(R.id.grade_text);


        //ログアウトボタン
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                javaTea.setPassword("");
                javaTea.setToken("");

                //画面遷移
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }
}