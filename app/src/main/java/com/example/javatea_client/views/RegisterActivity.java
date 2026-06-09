package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.javatea_client.R;

public class RegisterActivity extends AppCompatActivity {
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

    //大学一覧
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
                // 今は仮実装
                Toast.makeText(
                        this,
                        "大学追加画面へ",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            TextView universityText =
                    findViewById(R.id.universityText);

            universityText.setText(selectedUniversity);
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
        Navigation.setup(this);
        ModeBar.setup(this, "はじめに");
        //大学選択
        TextView universityText = findViewById(R.id.universityText);

        universityText.setOnClickListener(v -> {
            showKanaDialog();
        });
    }
}