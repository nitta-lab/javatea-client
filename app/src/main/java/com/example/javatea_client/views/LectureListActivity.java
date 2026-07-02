package com.example.javatea_client.views;

import android.os.Bundle;
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
    private final List<String> categoryPath = new ArrayList<>();
    //大学名を保持する変数
    private String univId = "";
    private String facultyName = "";
    private String departmentName = "";
    private String lectureListType = "";

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

        // カテゴリ表示にも追加
        addCategory(univId);
    }

    //学部名を取得する
    public String getFacultyName() {
        return facultyName;
    }

    // 学部名を保存する
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;

        // カテゴリ表示にも追加
        addCategory(facultyName);
    }

    //学科名を取得する
    public String getDepartmentName() {
        return departmentName;
    }

    // 学科名を保存する
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;

        // カテゴリ表示にも追加
        addCategory(departmentName);
    }

    // 階層を取得する
    public List<String> getCategoryPath() {
        return categoryPath;
    }

    //階層を1つ追加
    public void addCategory(String categoryName) {
        categoryPath.add(categoryName);
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
        }

        updateCategoryText();
    }

    private void backToCategory(int index) {

        switch (index) {

            case 0:
                // 「甲南大学」を押した
                while (categoryPath.size() > 1) {
                    removeLastCategory();
                }

                // 授業・学校生活を表示する画面
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new UniversityFragment())
                        .commit();
                break;

            case 1:
                // 「知能情報学部」を押した
                while (categoryPath.size() > 2) {
                    removeLastCategory();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FacultySelectFragment())
                        .commit();
                break;

            case 2:
                // 「知能情報学科」を押した
                while (categoryPath.size() > 3) {
                    removeLastCategory();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new DepartmentSelectFragment())
                        .commit();
                break;
        }
    }
}