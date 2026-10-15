
package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;

import java.util.ArrayList;
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

    private final ArrayList<String> addedTags = new ArrayList<>();

    // =========================================
    // View
    // =========================================

    private TextView categoryLabel;

    private EditText tagEditText;

    private LinearLayout addedTagText;

    private Button categoryButton1;
    private Button categoryButton2;
    private Button categoryButton3;

    private Button addTagButton;

    private Button searchButton;

    private Javatea javatea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setContentView(R.layout.activity_search);

        // =========================================
        // アプリケーション情報
        // =========================================

        javatea = (Javatea) getApplication();

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

        categories = new String[]{
                "全般",
                javatea.getUniversity(),
                "その他の大学"
        };

        updateCategory();
        updateAddedTags();

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

        // =========================================
        // 表示をクリア
        // =========================================

        addedTagText.removeAllViews();

        // =========================================
        // 見出しを表示
        // =========================================

        TextView titleText = new TextView(this);

        titleText.setText("追加済みのタグ：");
        titleText.setTextSize(14);
        titleText.setGravity(Gravity.CENTER_VERTICAL);

        addedTagText.addView(titleText);

        // =========================================
        // タグがない場合
        // =========================================

        if (addedTags.isEmpty()) {
            return;
        }

        // =========================================
        // タグごとに表示
        // =========================================

        for (int i = 0; i < addedTags.size(); i++) {

            // タグと削除ボタンを横並びにする
            LinearLayout tagRow = new LinearLayout(this);

            tagRow.setOrientation(LinearLayout.HORIZONTAL);
            tagRow.setGravity(Gravity.CENTER_VERTICAL);

            // =========================================
            // タグ名
            // =========================================

            TextView tagText = new TextView(this);

            tagText.setText("・" + addedTags.get(i));
            tagText.setTextSize(14);
            tagText.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams tagParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    );

            tagRow.addView(tagText, tagParams);

            // =========================================
            // 削除ボタン
            // =========================================

            Button deleteButton = new Button(this);

            deleteButton.setText("×");
            deleteButton.setTextSize(20);

            deleteButton.setMinWidth(0);
            deleteButton.setMinHeight(0);
            deleteButton.setMinimumHeight(0);
            deleteButton.setMinimumWidth(0);

            deleteButton.setPadding(0, 0, 0, 0);

            // 削除対象のタグを取得
            String tag = addedTags.get(i);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 選択したタグだけを削除
                    addedTags.remove(tag);

                    // タグ表示を更新
                    updateAddedTags();

                    Toast.makeText(
                            SearchActivity.this,
                            "タグを削除しました",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            tagRow.addView(deleteButton);

            // =========================================
            // タグの行を追加
            // =========================================

            addedTagText.addView(tagRow);
        }
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
        Intent intent = new Intent(this, SearchResultActivity.class);
        if (categoryIndex == 0) {
            intent.putExtra("EXTRA_SEARCH_TYPE", "GENERAL");
        } else {
            intent.putExtra("EXTRA_SEARCH_TYPE", "UNIVERSITY");
            intent.putExtra("EXTRA_UNIV_ID", javatea.getUnivId());
        }
        intent.putExtra("EXTRA_KEYWORDS", addedTags);
        startActivity(intent);
    }
}
