
package com.example.javatea_client.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.resources.AnswerResource;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NotificationActivity extends AppCompatActivity {

    // ユーザー情報
    private String uid;
    private String token;

    // ViewModel
    private UserViewModel userViewModel;

    // 回答取得API
    private AnswerResource answerResource;

    // タブ
    private TextView tvAnswerNotificationTab;
    private TextView tvBestAnswerNotificationTab;

    // 通知一覧
    private List<Question> questionsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Question> bestAnswersList = new ArrayList<>();

    // 質問者のIDと名前を保存
    private final Map<String, String> userNames = new HashMap<>();

    // 質問IDと回答数を保存
    private final Map<String, Integer> answerCounts = new HashMap<>();

    // 同じユーザーの名前を重複して取得しないため
    private final Set<String> loadingUserNames = new HashSet<>();

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
        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        // 回答取得APIを準備
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        answerResource = retrofit.create(AnswerResource.class);

        // Navigationのセットアップ
        Navigation.setup(this);

        // タブを取得
        tvAnswerNotificationTab = findViewById(R.id.tvAnswerNotificationTab);
        tvBestAnswerNotificationTab = findViewById(R.id.tvBestAnswerNotificationTab);

        // 通知一覧の生成
        recyclerView = findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 通常の回答通知を表示
        recyclerView.setAdapter(
                new NotificationAdapter(
                        questionsList,
                        false,
                        userNames,
                        answerCounts
                )
        );

        // 「あなたへの回答」をオブザーブ
        userViewModel.getQuestions().observe(this, questions -> {
            if (questions != null) {

                questionsList.clear();
                answerCounts.clear();

                for (Question question : questions) {

                    // ベストアンサーが未設定の質問だけ追加
                    if (question.getBestAnswerAid() == null ||
                            question.getBestAnswerAid().isEmpty()) {

                        questionsList.add(question);

                        // この質問の回答数を取得
                        loadAnswerCount(question.getQid());
                    }
                }

                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        // 「ベストアンサー通知」をオブザーブ
        userViewModel.getBestAnswers().observe(this, bestAnswers -> {
            if (bestAnswers != null) {

                bestAnswersList.clear();
                bestAnswersList.addAll(bestAnswers);

                // 質問者の名前を取得
                for (Question question : bestAnswersList) {
                    loadUserName(question.getUid());
                }

                // 通知一覧を更新
                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
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
            recyclerView.setAdapter(
                    new NotificationAdapter(
                            questionsList,
                            false,
                            userNames,
                            answerCounts
                    )
            );
        });

        // 「ベストアンサー通知」を押したとき
        tvBestAnswerNotificationTab.setOnClickListener(v -> {

            tvBestAnswerNotificationTab.setBackgroundColor(0xFFD8D2E3);
            tvAnswerNotificationTab.setBackgroundColor(0xFFB8B3BE);

            // ベストアンサー通知の一覧を表示
            recyclerView.setAdapter(
                    new NotificationAdapter(
                            bestAnswersList,
                            true,
                            userNames,
                            answerCounts
                    )
            );
        });
    }

    // 質問ごとの回答数を取得するメソッド
    private void loadAnswerCount(String qid) {

        if (qid == null || qid.isEmpty()) return;

        answerResource.getAnswers(qid, uid, token)
                .enqueue(new Callback<HashMap<String, Answer>>() {

                    @Override
                    public void onResponse(
                            Call<HashMap<String, Answer>> call,
                            Response<HashMap<String, Answer>> response) {

                        if (response.isSuccessful() &&
                                response.body() != null) {

                            // 回答一覧の件数を取得
                            int count = response.body().size();

                            // 質問IDと回答数を保存
                            answerCounts.put(qid, count);

                            // 通知一覧を更新
                            if (recyclerView.getAdapter() != null) {
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                        } else {
                            Log.w(
                                    "NotificationActivity",
                                    "回答数取得失敗: " + response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<HashMap<String, Answer>> call,
                            Throwable t) {

                        Log.e(
                                "NotificationActivity",
                                "回答数取得エラー",
                                t
                        );
                    }
                });
    }

    // 質問者の名前を取得するメソッド
    private void loadUserName(String targetUid) {

        if (targetUid == null || targetUid.isEmpty()) return;

        if (userNames.containsKey(targetUid) ||
                loadingUserNames.contains(targetUid)) return;

        loadingUserNames.add(targetUid);

        new Thread(() -> {

            String name = null;

            try {
                name = userViewModel.getName(targetUid, token);
            } catch (RuntimeException e) {
                Log.e(
                        "NotificationActivity",
                        "名前取得エラー",
                        e
                );
            }

            String result = name;

            runOnUiThread(() -> {

                loadingUserNames.remove(targetUid);

                if (result != null) {

                    userNames.put(targetUid, result);

                    if (recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            });

        }).start();
    }
}
