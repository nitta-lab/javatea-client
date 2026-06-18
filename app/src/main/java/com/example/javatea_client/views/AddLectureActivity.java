package com.example.javatea_client.views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javatea_client.R;

public class AddLectureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_lecture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Navigation.setup(this);
        ModeBar.setup(this, "時間割設定");

        EditText LectureEditText = findViewById(R.id.LectureEditText);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);


        //×ボタン(画面遷移)
        //ImageButton CloseButton =  findViewById(R.id.CloseButton);
        //CloseButton.setOnClickListener(v ->{

               //}
                //);

        //追加ボタン(場面遷移)
        //Button AddButton = findViewById(R.id.AddButton);


        //コマ数選択(プルダウン)
        String[] classlist = {"1コマ","2コマ","3コマ"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,classlist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);


        //学年選択(プルダウン)
        String[] gradelist = {"1年次以上","2年次以上","3年次以上","4年次以上"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,gradelist);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        //授業名入力
        String inputText = LectureEditText.getText().toString();
        if (inputText.isEmpty()) {
            LectureEditText.setError("入力してください");
            return;
        }
    }
}