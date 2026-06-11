package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.javatea_client.R;

public class RegisterActivity extends AppCompatActivity {
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

            TextView universityText =
                findViewById(R.id.universityText);

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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener(v -> {

                String universityName =
                    nameEdit.getText().toString().trim();

                String universityKana =
                    kanaEdit.getText().toString().trim();

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
                //同じ大学名と読み仮名が登録されようとしていないか。
                //読み仮名がひらがなで入力されているか。

                Toast.makeText(
                    this,
                    "登録しました",
                    Toast.LENGTH_SHORT
                ).show();

                dialog.dismiss();
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
        Navigation.setup(this);
        ModeBar.setup(this, "はじめに");
        //大学選択
        TextView universityPullDownText = findViewById(R.id.universityPullDownText);

        universityPullDownText.setOnClickListener(v -> {
            showKanaDialog();
        });
    }
}