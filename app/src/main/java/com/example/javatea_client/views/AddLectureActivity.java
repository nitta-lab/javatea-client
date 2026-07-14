package com.example.javatea_client.views;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.LectureViewModel;


import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.LectureViewModel;

public class AddLectureActivity extends AppCompatActivity {

    LectureViewModel lectureViewModel;
    private String univId;
    private  String facultyName;
    private  String departmentName;

    private String day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lectureViewModel = new LectureViewModel();
        lectureViewModel = new ViewModelProvider(this).get(LectureViewModel.class);
        lectureViewModel.getFinishAddLecture().observe(this, finishAddLecture -> {
            // nullかfalseはreturn
            if(finishAddLecture == null || !finishAddLecture) {
                return;
            }
            // trueなら合図出す
            setResult(RESULT_OK);

            // 画面閉じる
            finish();
        });

        Javatea javatea = (Javatea) getApplication();
        javatea.setView("AddLecture");
        univId = javatea.getUnivId();
        facultyName = javatea.getFaculty();
        departmentName = javatea.getDepartment();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_lecture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent reIntent = getIntent();
        day = reIntent.getStringExtra("day");
        int period = reIntent.getIntExtra("period",1);
        int year = reIntent.getIntExtra("year",1);
        String semester = reIntent.getStringExtra("semester");
        Navigation.setup(this);
        ModeBar.setup(this, "時間割設定");


        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        //コマ数選択(プルダウン)
        String[] classeslist = {"1コマ","2コマ","3コマ"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(AddLectureActivity.this, android.R.layout.simple_spinner_item,classeslist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        //学年選択(プルダウン)
        String[] gradelist = {"1年次以上","2年次以上","3年次以上","4年次以上"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(AddLectureActivity.this, android.R.layout.simple_spinner_item,gradelist);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


        //前期後期その他の切り替え
        RadioButton FirstButton = findViewById(R.id.FirstButton);
        RadioButton AllyearButton = findViewById(R.id.AllyearButton);
        if("前期".equals(semester)){
            FirstButton.setText("前期");
            AllyearButton.setEnabled(true);
        }else if("後期".equals(semester)){
            FirstButton.setText("後期");
            AllyearButton.setEnabled(true);
        }else if("その他".equals(semester)){
            FirstButton.setText("その他");
            FirstButton.setChecked(true);
            day = "日";
            AllyearButton.setEnabled(false);
        }


        //追加ボタン(SetTimeTableActivityに画面遷移)
        //データはCategoryViewModel/LectureViewModel
        Button AddButton = (Button) findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RadioGroup RadioGroupMajor = findViewById(R.id.RadioGroupMajor);
                RadioGroup RadioButtonSemester = findViewById(R.id.RadioGroupSemester);

                int majorId = RadioGroupMajor.getCheckedRadioButtonId();
                int semesterId = RadioButtonSemester.getCheckedRadioButtonId();

                Log.d("addLecture", "majorId: " + majorId);
                Log.d("addLecture", "semesterId: " + semesterId);

                if(majorId == -1){
                    Toast.makeText(AddLectureActivity.this,"授業科目を選択してください",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (semesterId == -1) {
                    Toast.makeText(AddLectureActivity.this, "履修期間を選択してください", Toast.LENGTH_SHORT).show();
                    return;
                }


                RadioButton selectedMajor = findViewById(majorId);
                String type = selectedMajor.getText().toString();
                Log.d("addLecture", "type確認：" + type);

                RadioButton selectedsemester = findViewById(semesterId);
                String semester = selectedsemester.getText().toString();


                //String classes = spinner1.getSelectedItem().toString();
                //String grade = spinner2.getSelectedItem().toString();

                //授業名入力
                //入力しているかチェック
                EditText lectureEditText = findViewById(R.id.LectureEditText);
                String name = lectureEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    lectureEditText.setError("入力してください");
                    lectureEditText.requestFocus();
                    return;
                }


                int classes = spinner1.getSelectedItemPosition() + 1;
                int grade = spinner2.getSelectedItemPosition() + 1;



                Log.d("AddLectureActivity", "名前" + name + "曜日" + day + "時限" + period );
                lectureViewModel.startAddLecture(name, grade, semester, classes, day, period, univId, facultyName, departmentName, type);
//                lectureViewModel.startAddLecture("最適化", 2, "前期", 1, "水", 3, "univ-id1", "知能情報学部", "知能情報学科", "faculty");



            }

        });

        //×ボタン(SetTimeTableActivityに画面遷移)
        ImageView CloseButton =  (ImageView) findViewById(R.id.CloseButton);
        CloseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                finish();
            }
        });
    }

}