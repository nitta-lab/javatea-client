package com.example.javatea_client.views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;

import java.util.ArrayList;
import java.util.List;

public class LectureListActivity extends AppCompatActivity {

    private TextView tvCategory;

    // 現在の階層
    private final List<String> categoryPath = new ArrayList<>();

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
    }

    //階層を1つ追加
    public void addCategory(String categoryName) {
        categoryPath.add(categoryName);
        updateCategoryText();
    }

    //表示更新
    private void updateCategoryText() {

        StringBuilder text = new StringBuilder("カテゴリ：");

        for (int i = 0; i < categoryPath.size(); i++) {

            if (i > 0) {
                text.append(" > ");
            }

            text.append(categoryPath.get(i));
        }

        tvCategory.setText(text.toString());
    }

    public void removeLastCategory() {

        if (!categoryPath.isEmpty()) {
            categoryPath.remove(categoryPath.size() - 1);
        }

        updateCategoryText();
    }
}