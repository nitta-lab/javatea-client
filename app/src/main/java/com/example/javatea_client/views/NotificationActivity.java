package com.example.javatea_client.views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    // ユーザー情報
    private String uid;
    private String token;

    // ViewModel
    private UserViewModel userViewModel;

    // タブ
    private TextView tvAnswerNotificationTab;
    private TextView tvBestAnswerNotificationTab;

    // 通知一覧
    private List<Question> questionsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Question> bestAnswersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用するXML
        setContentView(R.layout.activity_notification);

        // ユーザー情報を取得
        Javatea javaTea = (Javatea) getApplication();
        uid = javaTea.getUserId();
        token = javaTea.getToken();

        // ViewModelを作る
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Navigationのセットアップ
        Navigation.setup(this);

        // タブを取得
        tvAnswerNotificationTab = findViewById(R.id.tvAnswerNotificationTab);
        tvBestAnswerNotificationTab = findViewById(R.id.tvBestAnswerNotificationTab);

        // 通知一覧の生成
        recyclerView = findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NotificationAdapter(questionsList));

        // 「あなたへの回答」をpぶざーぶ
        userViewModel.getQuestions().observe(this, questions -> {
            if (questions != null) {
                questionsList.clear();
                questionsList.addAll(questions);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        // 「ベストアンサー通知」をオブザーブ
        userViewModel.getBestAnswers().observe(this, bestAnswers -> {
            if (bestAnswers != null) {
                bestAnswersList.clear();
                bestAnswersList.addAll(bestAnswers);
            }
        });

        // 自分が質問したQuestion一覧を取得
        userViewModel.getUserQuestions(uid, token);

        // 自分がベストアンサーに選ばれたQuestion一覧を取得
        userViewModel.getUserBestAnswers(uid, token);

        // 「あなたへの回答」を押したとき
        tvAnswerNotificationTab.setOnClickListener(v -> {
            tvAnswerNotificationTab.setBackgroundColor(0xFFD8D2E3);
            tvBestAnswerNotificationTab.setBackgroundColor(0xFFB8B3BE);

            // あなたへの回答の一覧を表示
            recyclerView.setAdapter(new NotificationAdapter(questionsList));
        });

        // 「ベストアンサー通知」を押したとき
        tvBestAnswerNotificationTab.setOnClickListener(v -> {
            tvBestAnswerNotificationTab.setBackgroundColor(0xFFD8D2E3);
            tvAnswerNotificationTab.setBackgroundColor(0xFFB8B3BE);

            // ベストアンサー通知の一覧を表示
            recyclerView.setAdapter(new NotificationAdapter(bestAnswersList));
        });
    }
}