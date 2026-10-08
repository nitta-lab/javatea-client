package com.example.javatea_client.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;

import java.util.*;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";


    // =========================================
    // カテゴリ
    // =========================================

    private String[] categories;

    // 現在選択しているカテゴリ
    private int categoryIndex = 0;


    // =========================================
    // 追加済みタグ
    // =========================================

    private final List<String> addedTags = new ArrayList<>();


    // =========================================
    // View
    // =========================================

    private TextView categoryLabel;

    private EditText tagEditText;

    private TextView addedTagText;

    private Button categoryButton1;
    private Button categoryButton2;
    private Button categoryButton3;

    private Button addTagButton;

    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);


        // =========================================
        // アプリケーション情報
        // =========================================

        Javatea javatea = (Javatea) getApplication();

        javatea.setView("Search");


        // =========================================
        // Navigation / ModeBar
        // =========================================

        Navigation.setup(this);

        ModeBar.setup(this, "検索する");


        // =========================================
        // View取得
        // =========================================

        categoryLabel = findViewById(R.id.CategoryLabel);

        tagEditText = findViewById(R.id.TagEditText);

        addedTagText = findViewById(R.id.AddedTagText);


        categoryButton1 = findViewById(R.id.CategoryButton1);

        categoryButton2 = findViewById(R.id.CategoryButton2);
        categoryButton2.setText(javatea.getUniversity());

        categoryButton3 = findViewById(R.id.CategoryButton3);


        addTagButton = findViewById(R.id.AddTagButton);

        searchButton = findViewById(R.id.SearchButton);


        // =========================================
        // 初期状態
        // =========================================

        categories = new String[]{"全般",
                javatea.getUniversity(),
                "その他の大学"};
        updateCategory();


        // =========================================
        // 全般
        // =========================================

        categoryButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryIndex = 0;

                updateCategory();
            }
        });


        // =========================================
        // 甲南大学
        // =========================================

        categoryButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryIndex = 1;

                updateCategory();
            }
        });


        // =========================================
        // その他の大学
        // =========================================

        categoryButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryIndex = 2;

                updateCategory();
            }
        });


        // =========================================
        // タグ追加
        // =========================================

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTag();
            }
        });


        // =========================================
        // 検索
        // =========================================

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search();
            }
        });
    }


    /**
     * カテゴリ表示を更新
     */
    private void updateCategory() {

        String category = categories[categoryIndex];


        // 「カテゴリ：（全般）」のように表示
        categoryLabel.setText(
                "カテゴリ：（" + category + "）"
        );


        Log.d(
                TAG,
                "現在のカテゴリ：" + category
        );
    }


    /**
     * タグを追加
     */
    private void addTag() {

        // =========================================
        // 入力されたタグを取得
        // =========================================

        String tag = tagEditText
                .getText()
                .toString()
                .trim();


        // =========================================
        // 空文字チェック
        // =========================================

        if (tag.isEmpty()) {

            tagEditText.setError("タグを入力してください");

            tagEditText.requestFocus();

            return;
        }


        // =========================================
        // 重複チェック
        // =========================================

        if (addedTags.contains(tag)) {

            Toast.makeText(
                    SearchActivity.this,
                    "このタグはすでに追加されています",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =========================================
        // タグを追加
        // =========================================

        addedTags.add(tag);


        // =========================================
        // 画面更新
        // =========================================

        updateAddedTags();


        // =========================================
        // 入力欄を空にする
        // =========================================

        tagEditText.setText("");


        Toast.makeText(
                SearchActivity.this,
                "タグを追加しました",
                Toast.LENGTH_SHORT
        ).show();
    }


    /**
     * 追加済みタグを表示
     */
    private void updateAddedTags() {

        if (addedTags.isEmpty()) {

            addedTagText.setText("追加済みのタグ：");

            return;
        }


        StringBuilder builder = new StringBuilder();

        builder.append("追加済みのタグ：\n");


        for (int i = 0; i < addedTags.size(); i++) {

            builder.append("・");

            builder.append(addedTags.get(i));


            if (i < addedTags.size() - 1) {

                builder.append("\n");
            }
        }


        addedTagText.setText(
                builder.toString()
        );
    }


    /**
     * 検索
     */
    private void search() {

        // =========================================
        // 現在のカテゴリ
        // =========================================

        String category = categories[categoryIndex];


        // =========================================
        // タグがない場合
        // =========================================

        if (addedTags.isEmpty()) {

            Toast.makeText(
                    SearchActivity.this,
                    "検索するタグを追加してください",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =========================================
        // ログ
        // =========================================

        Log.d(
                TAG,
                "検索開始"
        );

        Log.d(
                TAG,
                "カテゴリ：" + category
        );


        for (String tag : addedTags) {

            Log.d(
                    TAG,
                    "タグ：" + tag
            );
        }


        // =========================================
        // TODO
        // 検索処理
        // =========================================

        /*
         * ここにSearchViewModelなどを使った
         * サーバーへの検索処理を入れる。
         *
         * 例：
         *
         * searchViewModel.startSearch(
         *     category,
         *     addedTags
         * );
         */


        Toast.makeText(
                SearchActivity.this,
                "「" + category + "」で検索します",
                Toast.LENGTH_SHORT
        ).show();
    }
}