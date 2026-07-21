package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;

import java.util.ArrayList;
import java.util.List;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class LectureListActivity extends AppCompatActivity {

    private TextView tvCategory;

    // 現在の階層
    private List<String> categoryPath = new ArrayList<>();
    private List<String> categoryPathType = new ArrayList<>();
    //大学名を保持する変数
    private String univId = "";
    private String facultyName = "";
    private String departmentName = "";
    private String lectureListType = "";
    private String lectureId = "";
    private String qid = "";
    private String aid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecture_list);

        Navigation.setup(this);
        ModeBar.setup(this, "Question一覧");

        tvCategory = findViewById(R.id.tvCategory);

        updateCategoryText();
        //前回開いていた画面を開くようにする
        Javatea javatea = (Javatea) this.getApplication();
        javatea.setView("LectureList");

        Intent intent = getIntent();
        String timetable = intent.getStringExtra("timetable");
        if (timetable != null && timetable.equals("timetable")){
            setUnivId(javatea.getUnivId());
            addCategory(javatea.getUniversity(),"大学");
            addCategory("授業","【授業】");
            lectureId = intent.getStringExtra("Lecture-id");
            if(intent.getStringExtra("facultyName") != null) {
                facultyName = intent.getStringExtra("facultyName");
                addCategory(facultyName,"学部");
                if(intent.getStringExtra("departmentName") != null) {
                    departmentName = intent.getStringExtra("departmentName");
                    addCategory(departmentName,"学科");
                    setLectureListType("department");
                } else {
                    setLectureListType("general_faculty");
                    addCategory("学部全般","【学部全般】");
                }
            } else {
                setLectureListType("general_university");
                addCategory("大学全般","【大学全般】");
            }
            addCategory(intent.getStringExtra("LectureName"),"授業");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuestionSelectFragment())
                    .commit();
            return;
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (!categoryPath.isEmpty()) {
                    // カテゴリを1つ戻す
                    removeLastCategory();

                    // Fragmentが戻れるならFragmentを戻す
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        // FragmentがなければActivityを終了
                        finish();
                    }

                } else {
                    // カテゴリが空なら通常通り終了
                    finish();
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GeneralFragment())
                .commit();
    }
    public String getLectureListType() {
        return lectureListType;
    }

    public void setLectureListType(String lectureListType) {
        this.lectureListType = lectureListType;
    }

    //大学名を取得する
    public String getUnivId() {
        return univId;
    }

    // 大学名を保存する
    public void setUnivId(String univId) {
        this.univId = univId;
    }

    //学部名を取得する
    public String getFacultyName() {
        return facultyName;
    }

    // 学部名を保存する
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    //学科名を取得する
    public String getDepartmentName() {
        return departmentName;
    }

    //学科名を保存する
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    //授業IDを取得する
    public String getLectureId() { return lectureId; }

    //授業IDを保存する
    public void setLectureId(String lectureId) { this.lectureId = lectureId; }

    //Qidを取得する
    public String getQid() { return qid; }

    //Qidを保存する
    public void setQid() { this.qid = qid; }

    //Aidを取得する
    public String getAid() { return aid; }

    //Aidを保存する
    public void setAid() { this.aid = aid; }

    // 階層を取得する
    public List<String> getCategoryPath() {
        return categoryPath;
    }

    //階層を1つ追加
    public void addCategory(String categoryName, String categoryType) {
        Log.d("categoryName",categoryName + " " + categoryType);
        categoryPath.add(categoryName);
        categoryPathType.add(categoryType);
        updateCategoryText();
    }

    //表示更新
    private void updateCategoryText() {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("カテゴリ：");

        for (int i = 0; i < categoryPath.size(); i++) {

            if (i > 0) {
                builder.append(" > ");
            }

            final int index = i;
            String name = categoryPath.get(i);

            int start = builder.length();
            builder.append(name);
            int end = builder.length();

            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    backToCategory(index);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvCategory.setText(builder);
        tvCategory.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // 一つ戻る
    public void removeLastCategory() {

        if (!categoryPath.isEmpty()) {
            categoryPath.remove(categoryPath.size() - 1);
            categoryPathType.remove(categoryPathType.size() - 1);
        }

        updateCategoryText();
    }

    private void backToCategory(int index) {

        while (categoryPathType.size() > index + 1) {
            removeLastCategory();
        }

        switch (categoryPathType.get(categoryPathType.size()-1)) {

            case "全般":
                setLectureId("全般");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new QuestionSelectFragment())
                        .commit();
                break;

            case "【大学全般】":
                // 学科
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LectureSelectFragment())
                        .commit();
                break;

            case "大学":
                // 大学
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new UniversityFragment())
                        .commit();
                break;

            case "【学校生活】":
                // 授業
                setLectureId("学校生活");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new QuestionSelectFragment())
                        .commit();
                break;

            case "【授業】":
                // 授業
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FacultySelectFragment())
                        .commit();
                break;

            case "【学部全般】":
                // 学科
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LectureSelectFragment())
                        .commit();
                break;

            case "学部":
                // 学部
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new DepartmentSelectFragment())
                        .commit();
                break;

            case "学科":
                // 学科
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LectureSelectFragment())
                        .commit();
                break;

            case "授業":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new QuestionSelectFragment())
                        .commit();
                break;
        }
    }
}