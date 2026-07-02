package com.example.javatea_client.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.University;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.*;


// Commit&Pushする前にManifestとCategoryViewModelを元に戻すのを忘れないように


public class RegisterActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;
    private static final String TAG = "RegisterActivity";
    private boolean flag = false;
    private String selectedUniversityId; //今選択されてる大学IDを保存する変数
    private String selectedUniversityName; //今選択されてる大学を保存する変数
    private String selectedFacultyName; //今選択されてる学部を保存する変数
    private String selectedDepartmentName; //今選択されてる学科を保存する変数
    private String selectedGrade; //今選択されてる学年を保存する変数
    private String uid;
    private String token;

    //〇行のボタン
    private void showKanaSelectionDialog() {
        String[] kanaGroups = {"ア行", "カ行", "サ行", "タ行", "ナ行", "ハ行", "マ行", "ヤ行", "ラ行", "ワ行", "ン"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(Arrays.copyOfRange(kanaGroups, 0, kanaGroups.length - 1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedKana = kanaGroups[which];

                for(int i=0;i<kanaGroups.length-1;i++) {
                    if (selectedKana.charAt(0) == kanaGroups[i].charAt(0)) {
                        categoryViewModel.getAllUnivId(kanaGroups[i].substring(0,1), kanaGroups[i + 1].substring(0,1));
                        Log.d(TAG, kanaGroups[i] + "の大学の取得を開始");
                        break;
                    }
                }
            }
        });
        builder.show();
    }

    //大学一覧
    private void showUniversitySelectionDialog(String[] universityArray,Collection<University> currentUniversities){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("大学を選択してください");

        builder.setItems(universityArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedUniversityInfo = universityArray[which]; //大学名とカナのセット

                if(selectedUniversityInfo.equals("+大学を追加する")){
                    Log.d(TAG, "追加ボタンが押されました。");
                    showAddUniversityDialog();
                    return;
                }
                //大学が選択されたら、学部・学科は「選択してください」という表示に戻す。
                TextView facultyText = findViewById(R.id.facultyText);
                TextView departmentText = findViewById(R.id.departmentText);
                facultyText.setText("選択してください");
                departmentText.setText("選択してください");
                selectedFacultyName = "";

                int index = selectedUniversityInfo.lastIndexOf("(");

                selectedUniversityName = selectedUniversityInfo.substring(0, index);

                String selectedUniversityKana = selectedUniversityInfo.substring(index + 1, selectedUniversityInfo.length() - 1);

                for (University university : currentUniversities) {
                    if (university.getName().equals(selectedUniversityName)&&university.getKana().equals(selectedUniversityKana)) {
                        selectedUniversityId = university.getId();
                        break;
                    }
                }

                TextView universityText = findViewById(R.id.universityText);
                universityText.setText(selectedUniversityName);
            }
        });

        builder.show();
    }

    //大学の新規登録
    private void showAddUniversityDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("大学名　　　　例：〇〇大学");
        nameEdit.setSingleLine(true);

        EditText kanaEdit = new EditText(this);
        kanaEdit.setHint("フリガナ　　　例：〇〇ダイガク");
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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String universityName = nameEdit.getText().toString().trim();
                String universityKana = kanaEdit.getText().toString().trim();

                if (universityName.isEmpty() && universityKana.isEmpty()) {
                    nameEdit.setError("大学名を入力してください");
                    kanaEdit.setError("フリガナを入力してください");
                    return;
                }

                if (universityName.isEmpty()) {
                    nameEdit.setError("大学名を入力してください");
                    return;
                }

                if (universityKana.isEmpty()) {
                    kanaEdit.setError("フリガナを入力してください");
                    return;
                }

                if(!universityName.endsWith("大学")) {
                    universityName = universityName + "大学";
                }

                if(!universityKana.endsWith("ダイガク")) {
                    universityKana = universityKana + "ダイガク";
                }

                //読み仮名がひらがなで入力されているか。
                if(!universityKana.matches("^[\\u30A1-\\u30FC]+$")) {
                    Toast.makeText(RegisterActivity.this, "フリガナは全角カタカナで入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }

                //大学を追加する
                categoryViewModel.postNewUnivId(universityName,universityKana);
                Log.d(TAG, "大学IDの作成に成功");
                Toast.makeText(RegisterActivity.this, "大学を登録しました", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    //学部一覧
    private void showFacultySelectionDialog(String[] facultyArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学部を選択してください");

        builder.setItems(facultyArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFacultyName = facultyArray[which];
                //学部が選択されたら、学科は「選択してください」という表示に戻す。
                TextView departmentText = findViewById(R.id.departmentText);
                departmentText.setText("選択してください");

                if (selectedFacultyName.equals("+学部を追加する")){
                    Log.d(TAG, "追加ボタンが押されました。");
                    showAddFacultyDialog();
                } else {
                    TextView facultyText = findViewById(R.id.facultyText);
                    facultyText.setText(selectedFacultyName);
                }
            }
        });

        builder.show();
    }

    //学部の新規登録
    private void showAddFacultyDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("学部名　　　　例：〇〇学部");
        nameEdit.setSingleLine(true);

        layout.addView(nameEdit);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("学部を追加")
                .setView(layout)
                .setPositiveButton("登録", null)
                .setNegativeButton("キャンセル", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facultyName = nameEdit.getText().toString().trim();

                if (facultyName.isEmpty()) {
                    nameEdit.setError("学部名を入力してください");
                    return;
                }

                //学部追加
                categoryViewModel.addFaculty(selectedUniversityId,facultyName);
                Toast.makeText(RegisterActivity.this, "学部を登録しました", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    //学科一覧
    private void showDepartmentSelectionDialog(String[] departmentArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学科を選択してください");

        builder.setItems(departmentArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDepartmentName = departmentArray[which];

                if (selectedDepartmentName.equals("+学科を追加する")){
                    showAddDepartmentDialog();
                } else {
                    TextView departmentText = findViewById(R.id.departmentText);
                    departmentText.setText(selectedDepartmentName);
                }
            }
        });

        builder.show();
    }

    //学科の新規登録
    private void showAddDepartmentDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        nameEdit.setHint("学科名　　　　例：〇〇学科");
        nameEdit.setSingleLine(true);

        layout.addView(nameEdit);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("学科を追加")
                .setView(layout)
                .setPositiveButton("登録", null)
                .setNegativeButton("キャンセル", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName = nameEdit.getText().toString().trim();

                if (departmentName.isEmpty()) {
                    nameEdit.setError("学科名を入力してください");
                    return;
                }

                //学科追加
                categoryViewModel.addDepartment(selectedUniversityId,selectedFacultyName,departmentName);
                Toast.makeText(RegisterActivity.this, "学科を登録しました", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    //学年一覧
    private void showGradeDialog() {
        String[] grades = {"1", "2", "3", "4"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("学年を選択してください");
        builder.setItems(grades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedGrade = grades[which];

                TextView gradeText = findViewById(R.id.gradeText);
                gradeText.setText(selectedGrade);
            }
        });

        builder.show();
    }

    //確定ボタンを押したときの処理
    private void showConfirmScreen(){
        setInputEnabled(false,"");
        TextView description = findViewById(R.id.description);
        //1度押したとき
        if(!flag){
            String text = "確定してもよろしいでしょうか？\n\n※後から変更できません。";
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 15, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            description.setText(spannable);
            Button fixButton = findViewById(R.id.fixButton);
            fixButton.setVisibility(View.VISIBLE);
            flag = true;
        }else{ //2度押したとき(画面遷移)
            //user情報の登録
            userViewModel.setUniversity(uid,selectedUniversityName,token);
            userViewModel.setFaculty(uid,selectedFacultyName,token);
            if (!selectedDepartmentName.equals("学科なし")){
                userViewModel.setDepartment(uid,selectedDepartmentName,token);
            }
            userViewModel.setGrade(uid,Integer.parseInt(selectedGrade),token);

            //画面遷移
            Intent intent = new Intent(RegisterActivity.this, TimetableActivity.class);
            if ((userViewModel.getUniversity(uid,token) != null) && (userViewModel.getFaculty(uid,token) != null) && (userViewModel.getGrade(uid,token) != null)){
                if (selectedDepartmentName.equals("学科なし")) {
                    Toast.makeText(RegisterActivity.this, "ユーザー情報の登録が完了しました。", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else if (userViewModel.getDepartment(uid,token) != null) {
                    Toast.makeText(RegisterActivity.this, "ユーザー情報の登録が完了しました。", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "ユーザー情報の登録ができませんでした。もう一度やり直してください。", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "ユーザー情報の登録ができませんでした。もう一度やり直してください。", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //修正ボタンを押したときの処理
    private void returnToInputScreen(){
        TextView description = findViewById(R.id.description);

        description.setText("あなたの　　　　　　　　　　　　　　\n大学・学部・学科・学年を\n　　　　　　　　　　入力してください");

        setInputEnabled(true,"▼");
        Button fixButton = findViewById(R.id.fixButton);

        fixButton.setVisibility(View.INVISIBLE);//ボタンを見えなくする

        flag = false;

    }

    //確定ボタンを押したときの登録画面のクリック無効化
    private void setInputEnabled(boolean enabled,String text){
        TextView universityPullDownText = findViewById(R.id.universityPullDownText);
        findViewById(R.id.universityPullDownText).setEnabled(enabled);
        universityPullDownText.setText(text);

        TextView facultyPullDownText = findViewById(R.id.facultyPullDownText);
        findViewById(R.id.facultyPullDownText).setEnabled(enabled);
        facultyPullDownText.setText(text);

        TextView departmentPullDownText = findViewById(R.id.departmentPullDownText);
        findViewById(R.id.departmentPullDownText).setEnabled(enabled);
        departmentPullDownText.setText(text);

        TextView gradePullDownText = findViewById(R.id.gradePullDownText);
        findViewById(R.id.gradePullDownText).setEnabled(enabled);
        gradePullDownText.setText(text);

    }

    private void setupObservers() {
        // categoryViewModelの今の大学一覧の状態をobserve
        categoryViewModel.getCurrentUniversities().observe(this, new Observer<Collection<University>>() {
            @Override
            public void onChanged(Collection<University> currentUniversities) {
                if (currentUniversities == null) {
                    return;
                }
                Log.d(TAG, "全大学情報一覧を受信：" + currentUniversities.size() + "件");

                List<String> universityList = new ArrayList<>();

                for (University university : currentUniversities) {
                    Log.d(TAG, "大学名：" + university.getName());
                    universityList.add(university.getName() + "(" + university.getKana() + ")");
                }

                universityList.add("+大学を追加する");

                String[] universityArray = universityList.toArray(new String[0]);
                showUniversitySelectionDialog(universityArray,currentUniversities);
            }
        });

        // 今の学部一覧の状態をobserve
        categoryViewModel.getCurrentFaculty().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> currentFaculty) {
                if(currentFaculty == null) {
                    return;
                }
                Log.d(TAG, "学部一覧を受信：" + currentFaculty.size() + "件");

                List<String> facultyList = new ArrayList<>(currentFaculty);

                facultyList.add("+学部を追加する");

                String[] facultyArray = facultyList.toArray(new String[0]);
                showFacultySelectionDialog(facultyArray);
            }
        });

        // 今の学科一覧の状態をobserve
        categoryViewModel.getCurrentDepartment().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> currentDepartment) {
                if(currentDepartment == null) {
                    return;
                }
                Log.d(TAG, "学科一覧を受信：" + currentDepartment.size() + "件");

                List<String> departmentList = new ArrayList<>(currentDepartment);

                departmentList.add("学科なし");
                departmentList.add("+学科を追加する");

                String[] departmentArray = departmentList.toArray(new String[0]);
                showDepartmentSelectionDialog(departmentArray);
            }
        });
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

        Javatea javatea = (Javatea) this.getApplication();
        javatea.setView("Register");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setupObservers();

        uid = javatea.getUserId();
        token = javatea.getToken();


        // 戻るボタンの処理を遮断
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        //上のバー
        ModeBar.setup(this, "はじめに");

        //大学選択
        TextView universityPullDownText = findViewById(R.id.universityPullDownText);

        universityPullDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKanaSelectionDialog();
            }
        });

        //学部選択
        TextView facultyPullDownText = findViewById(R.id.facultyPullDownText);

        facultyPullDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView universityText = findViewById(R.id.universityText);
                String selectedUniversity = universityText.getText().toString();

                if (selectedUniversity.equals("選択してください")){
                    Toast.makeText(RegisterActivity.this, "先に大学を選択してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                categoryViewModel.getFaculty(selectedUniversityId);
            }
        });

        //学科選択
        TextView departmentPullDownText = findViewById(R.id.departmentPullDownText);

        departmentPullDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView facultyText = findViewById(R.id.facultyText);
                String selectedFaculty = facultyText.getText().toString();

                if (selectedFaculty.equals("選択してください")){
                    Toast.makeText(RegisterActivity.this, "先に学部を選択してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                categoryViewModel.getDepartments(selectedUniversityId,selectedFacultyName);
            }
        });

        //学年選択
        TextView gradePullDownText = findViewById(R.id.gradePullDownText);

        gradePullDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGradeDialog();
            }
        });

        //修正ボタンを押したときの処理
        Button fixButton = findViewById(R.id.fixButton);
        fixButton.setVisibility(View.INVISIBLE);//登録時は見えないようにしておく

        fixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToInputScreen();
            }
        });

        //確定ボタンを押したときの処理
        Button confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView universityText = findViewById(R.id.universityText);
                TextView facultyText = findViewById(R.id.facultyText);
                TextView departmentText = findViewById(R.id.departmentText);
                TextView gradeText = findViewById(R.id.gradeText);
                String selectedUniversity = universityText.getText().toString();
                String selectedFaculty = facultyText.getText().toString();
                String selectedDepartment = departmentText.getText().toString();
                String selectedGrade = gradeText.getText().toString();

                if (selectedUniversity.equals("選択してください") || selectedFaculty.equals("選択してください") || selectedDepartment.equals("選択してください") || selectedGrade.equals("選択してください")){
                    Toast.makeText(RegisterActivity.this, "未選択の項目があります", Toast.LENGTH_SHORT).show();
                    return;
                }
                showConfirmScreen();
            }
        });
    }
}