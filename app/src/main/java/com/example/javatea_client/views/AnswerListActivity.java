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
import com.example.javatea_client.viewModels.AnswerViewModel;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class AnswerListActivity extends AppCompatActivity {

    // NotificationAdapterから受け取る情報
    private String qid;
    private String title;
    private String bestAnswerAid;

    // ユーザー情報
    private String uid;
    private String token;

    // ViewModel
    private AnswerViewModel answerViewModel;

    // 画面の部品
    private TextView tvQuestionTitle;
    private RecyclerView recyclerView;

    // 回答一覧
    private final List<Answer> answerList = new ArrayList<>();

    // RecyclerViewで使用するAdapter
    private AnswerAdapter answerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer_list);

        // 下のナビゲーションを設定
        Navigation.setup(this);

        // NotificationAdapterから情報を受け取る
        qid = getIntent().getStringExtra("qid");
        title = getIntent().getStringExtra("title");
        bestAnswerAid = getIntent().getStringExtra("bestAnswerAid");

        // ユーザー情報を取得
        Javatea javaTea = (Javatea) getApplication();
        uid = javaTea.getUserId();
        token = javaTea.getToken();

        // ViewModelを作る
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);

        // XMLと接続
        tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
        recyclerView = findViewById(R.id.answerList);

        // 質問タイトルを表示
        tvQuestionTitle.setText("Q：" + title);

        // RecyclerViewを縦方向の一覧にする
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 既存のAnswerAdapterを作る
        answerAdapter = new AnswerAdapter(
                answerList,
                bestAnswerAid,
                answer -> {

                    Intent intent = new Intent(
                            AnswerListActivity.this,
                            AnswerDetailActivity.class
                    );

                    // どの質問か
                    intent.putExtra("qid", qid);

                    // どの回答か
                    intent.putExtra("aid", answer.getAid());

                    // 回答者名
                    intent.putExtra("answerName", answer.getName());

                    // 回答本文
                    intent.putExtra("answerBody", answer.getBody());

                    startActivity(intent);
                }
        );

        // RecyclerViewにAdapterを設定
        recyclerView.setAdapter(answerAdapter);

        // 回答一覧を監視
        answerViewModel.getAnswers().observe(this, answers -> {
            if (answers != null) {

                // 古い回答一覧を消す
                answerList.clear();

                // HashMapのAnswerをListに入れる
                answerList.addAll(answers.values());

                // RecyclerViewを更新
                answerAdapter.notifyDataSetChanged();
            }
        });

        // この質問の回答一覧を取得
        answerViewModel.loadAnswers(qid, uid, token);
    }
}