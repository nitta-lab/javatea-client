package com.example.javatea_client.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.javatea_client.R;

public class RegisterActivity extends AppCompatActivity {
    private boolean flag = false;
    //〇行のボタン(これは必須)
    private void showKanaDialog() {
        String[] kanaGroups = {
            //"選択してください",//これを選択したときは、別の処理を書きたい
            "あ行",
            "か行",
            "さ行",
            "た行",
            "な行",
            "は行",
            "ま行",
            "や行",
            "ら行",
            "わ行"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //builder.setTitle("選択してください");

        builder.setItems(kanaGroups, (dialog, which) -> {
            String selectedKana = kanaGroups[which];

            showUniversityDialog(selectedKana);
        });

        builder.show();
    }

    //大学一覧(大学名の部分はviewmodelsの方から持ってくることになるはず)
    private void showUniversityDialog(String kana) {
        //viewmodelsの方から持ってくる
        String[] universities;

        switch (kana) {
            case "あ行":
                universities = new String[]{
                    "愛媛大学",
                    "青山学院大学",
                    "会津大学",
                    "+大学を追加する"
                };
                break;

            case "か行":
                universities = new String[]{
                    "香川大学",
                    "鹿児島大学",
                    "関西大学",
                    "+大学を追加する"
                };
                break;

            case "さ行":
                universities = new String[]{
                    "埼玉大学",
                    "滋賀大学",
                    "静岡大学",
                    "+大学を追加する"
                };
                break;

            default:
                universities = new String[]{
                    "+大学を追加する"
                };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("大学を選択してください");

        builder.setItems(universities, (dialog, which) -> {

            String selectedUniversity = universities[which];

            if (selectedUniversity.equals("+大学を追加する")){
                showAddUniversityDialog();
                return;
            }

            TextView universityText = findViewById(R.id.universityText);

            universityText.setText(selectedUniversity);
        });

        builder.show();
    }

    //大学の新規登録
    private void showAddUniversityDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("大学名");
        nameEdit.setSingleLine(true);

        EditText kanaEdit = new EditText(this);
        kanaEdit.setHint("よみがな");
        kanaEdit.setSingleLine(true);

        layout.addView(nameEdit);
        layout.addView(kanaEdit);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("大学を追加")
            .setView(layout)
            .setPositiveButton("登録", null)
            .setNegativeButton("キャンセル", null)
            .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String universityName = nameEdit.getText().toString().trim();
            String universityKana = kanaEdit.getText().toString().trim();

            if (universityName.isEmpty() && universityKana.isEmpty()) {
                nameEdit.setError("大学名を入力してください");
                kanaEdit.setError("よみがなを入力してください");
                return;
            }

            if (universityName.isEmpty()) {
                nameEdit.setError("大学名を入力してください");
                return;
            }

            if (universityKana.isEmpty()) {
                kanaEdit.setError("よみがなを入力してください");
                return;
            }
            //最終チェックはもう少し必要かも。
            //同じ大学名と読み仮名が登録されようとしていないか。(無視)
            //読み仮名がひらがなで入力されているか。
            Toast.makeText(this, "登録しました", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });
    }

    //学部一覧(学部名の部分はviewmodelsの方から持ってくることになる)
    private void showFacultyDialog() {
        //viewmodelsの方から持ってくる
        String[] faculties = {
            "工学部",
            "経済学部",
            "教育学部",
            "+学部を追加する"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学部を選択してください");

        builder.setItems(faculties, (dialog, which) -> {

            String selectedFaculty = faculties[which];

            if (selectedFaculty.equals("+学部を追加する")){
                showAddFacultyDialog();
                return;
            }

            TextView facultyText = findViewById(R.id.facultyText);
            facultyText.setText(selectedFaculty);
        });

        builder.show();
    }

    //学部の新規登録
    private void showAddFacultyDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("学部名");
        nameEdit.setSingleLine(true);

        layout.addView(nameEdit);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("学部を追加")
            .setView(layout)
            .setPositiveButton("登録", null)
            .setNegativeButton("キャンセル", null)
            .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            String facultyName = nameEdit.getText().toString().trim();

            if (facultyName.isEmpty()) {
                nameEdit.setError("学部名を入力してください");
                return;
            }

            Toast.makeText(this, "登録しました", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });
    }

    //学科一覧(学科名の部分はviewmodelsの方から持ってくることになる)
    private void showDepartmentDialog() {
        //viewmodelsの方から持ってくる
        String[] departments = {
            "工学科",
            "経済学科",
            "教育学科",
            "学科なし",
            "+学科を追加する"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学科を選択してください");

        builder.setItems(departments, (dialog, which) -> {

            String selectedDepartment = departments[which];

            if (selectedDepartment.equals("+学科を追加する")){
                showAddDepartmentDialog();
                return;
            }

            TextView departmentText = findViewById(R.id.departmentText);
            departmentText.setText(selectedDepartment);
        });

        builder.show();
    }

    //学科の新規登録
    private void showAddDepartmentDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("学科名");
        nameEdit.setSingleLine(true);

        layout.addView(nameEdit);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("学科を追加")
            .setView(layout)
            .setPositiveButton("登録", null)
            .setNegativeButton("キャンセル", null)
            .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            String departmentName = nameEdit.getText().toString().trim();

            if (departmentName.isEmpty()) {
                nameEdit.setError("学科名を入力してください");
                return;
            }

            Toast.makeText(this, "登録しました", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });
    }

    //学年一覧
    private void showGradeDialog() {
        String[] grades = {
            "1",
            "2",
            "3",
            "4",
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学年を選択してください");

        builder.setItems(grades, (dialog, which) -> {

            String selectedGrade = grades[which];

            TextView gradeText = findViewById(R.id.gradeText);
            gradeText.setText(selectedGrade);
        });

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Navigation.setup(this);
        ModeBar.setup(this, "はじめに");

        //大学選択
        TextView universityPullDownText = findViewById(R.id.universityPullDownText);

        universityPullDownText.setOnClickListener(v -> {
            showKanaDialog();
        });

        //学部選択
        TextView facultyPullDownText = findViewById(R.id.facultyPullDownText);

        facultyPullDownText.setOnClickListener(v -> {
            TextView universityText = findViewById(R.id.universityText);
            String selectedUniversity = universityText.getText().toString();

            if (selectedUniversity.equals("選択してください")){
                Toast.makeText(this, "先に大学を選択してください", Toast.LENGTH_SHORT).show();
                return;
            }
            showFacultyDialog();
        });

        //学科選択
        TextView departmentPullDownText = findViewById(R.id.departmentPullDownText);

        departmentPullDownText.setOnClickListener(v -> {
            TextView facultyText = findViewById(R.id.facultyText);
            String selectedfaculty = facultyText.getText().toString();

            if (selectedfaculty.equals("選択してください")){
                Toast.makeText(this, "先に学部を選択してください", Toast.LENGTH_SHORT).show();
                return;
            }
            showDepartmentDialog();
        });

        //学年選択
        TextView gradePullDownText = findViewById(R.id.gradePullDownText);

        gradePullDownText.setOnClickListener(v -> {
            showGradeDialog();
        });

        //修正ボタン
        Button fixButton = findViewById(R.id.fixButton);
        //修正ボタンを押したときの処理
        fixButton.setOnClickListener(v -> {
            fixButton.setVisibility(View.INVISIBLE);
        });

        fixButton.setVisibility(View.INVISIBLE);//ボタンを見えなくする

        //確定ボタン
        Button confirmButton = findViewById(R.id.confirmButton);
        //確定ボタンを押したときの処理
        confirmButton.setOnClickListener(v -> {
            TextView description = findViewById(R.id.description);
            //1度押したとき
            if(!flag){
                String text = "確定してもよろしいでしょうか？\n\n※後から変更できません。";
                SpannableString spannable = new SpannableString(text);
                spannable.setSpan(
                        new ForegroundColorSpan(Color.RED),
                        15, text.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                description.setText(spannable);
                fixButton.setVisibility(View.VISIBLE);
                flag = true;
            //2度押したとき(画面遷移)
            }else{
                Intent intent = new Intent(RegisterActivity.this, TimetableActivity.class);
                startActivity(intent);
            }

        });
    }
}