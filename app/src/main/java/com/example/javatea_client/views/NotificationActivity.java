package com.example.javatea_client.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.Set;

public class NotificationActivity extends AppCompatActivity {

    // ユーザー情報
    private String userId;
    private String token;

    // ViewModel
    private UserViewModel userViewModel;

    // 「あなたへの回答」のタブ
    private TextView tvAnswerNotificationTab;

    // 「ベストアンサー通知」のタブ
    private TextView tvBestAnswerNotificationTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NotificationActivityで表示するXML
        setContentView(R.layout.activity_notification);

        // ユーザー情報の取得
        Javatea javaTea = (Javatea) getApplication();

        userId = javaTea.getUserId();
        token = javaTea.getToken();

        // ViewModelの初期化
        userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        // Navigationのセットアップ
        Navigation.setup(this);


        // タブを取得
        tvAnswerNotificationTab = findViewById(R.id.tvAnswerNotificationTab);

        tvBestAnswerNotificationTab = findViewById(R.id.tvBestAnswerNotificationTab);



        // 質問一覧を取得
        userViewModel.getUserQuestions(userId, token);


        // 質問一覧が更新されたら実行
        userViewModel.getQuestions().observe(this, new Observer<Set<Question>>() {

                    @Override
                    public void onChanged(Set<Question> questions) {

                        if (questions != null) {

                            // Questionを1件ずつ取り出す
                            for (Question question : questions) {

                                Log.d("Notification", "質問：" + question.getTitle());
                            }
                        }
                    }
                }
        );


        // ベストアンサー一覧を取得
        userViewModel.getUserBestAnswers(userId, token);


        // ベストアンサー一覧が更新されたら実行
        userViewModel.getBestAnswers().observe(this, new Observer<Set<Question>>() {

                    @Override
                    public void onChanged(Set<Question> bestAnswers) {

                        if (bestAnswers != null) {

                            // Questionを1件ずつ取り出す
                            for (Question question : bestAnswers) {

                                Log.d("BestAnswer", "質問：" + question.getTitle()
                                );
                            }
                        }
                    }
                }
        );


        // タブのクリック処理

        // 「あなたへの回答」を押したとき
        tvAnswerNotificationTab.setOnClickListener(v -> {

            // 選択中
            tvAnswerNotificationTab.setBackgroundColor(0xFFD8D2E3);

            // 選択されていない
            tvBestAnswerNotificationTab.setBackgroundColor(0xFFB8B3BE);
        });


        // 「ベストアンサー通知」を押したとき
        tvBestAnswerNotificationTab.setOnClickListener(v -> {

            // 選択中
            tvBestAnswerNotificationTab.setBackgroundColor(0xFFD8D2E3);

            // 選択されていない
            tvAnswerNotificationTab.setBackgroundColor(0xFFB8B3BE);
        });
    }
}