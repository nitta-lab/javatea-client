
package com.example.javatea_client.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.University;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;
    private boolean flag = false;
    private String selectedUniversityId; //今選択されてる大学IDを保存する変数
    private String selectedUniversityName; //今選択されてる大学を保存する変数
    private String univId;

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
    private void showUniversitySelectionDialog(String[] universityArray, Collection<University> currentUniversities){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("大学を選択してください");

        builder.setItems(universityArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedUniversityInfo = universityArray[which]; //大学名とカナのセット

                int index = selectedUniversityInfo.lastIndexOf("(");

                selectedUniversityName = selectedUniversityInfo.substring(0, index);

                String selectedUniversityKana = selectedUniversityInfo.substring(index + 1, selectedUniversityInfo.length() - 1);

                for (University university : currentUniversities) {
                    if (university.getName().equals(selectedUniversityName)&&university.getKana().equals(selectedUniversityKana)) {
                        selectedUniversityId = university.getId();
                        break;
                    }
                }
                categories[2] = selectedUniversityName;
                updateCategory();
            }
        });

        builder.show();
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

                String[] universityArray = universityList.toArray(new String[0]);
                showUniversitySelectionDialog(universityArray, currentUniversities);
            }
        });
    }



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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setupObservers();

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

                //大学選択
                showKanaSelectionDialog();
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
        if(categoryIndex == 1) {
            univId = javatea.getUnivId();
        } else if(categoryIndex == 2) {
            univId = selectedUniversityId;
        }
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

//        if (addedTags.isEmpty()) {
//
//            Toast.makeText(
//                    SearchActivity.this,
//                    "検索するタグを追加してください",
//                    Toast.LENGTH_SHORT
//            ).show();
//
//            return;
//        }

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
