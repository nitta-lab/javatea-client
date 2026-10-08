package com.example.javatea_client.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.QuestionViewModel;

public class AnswerDetailActivity extends AppCompatActivity {

    // AnswerListActivityから受け取る情報
    private String qid;
    private String aid;
    private String answerName;
    private String answerBody;

    // ViewModel
    private QuestionViewModel questionViewModel;

    // 画面の部品
    private TextView tvAnswerName;
    private TextView tvAnswerBody;
    private Button btnBestAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer_detail);

        // 下のナビゲーションを設定
        Navigation.setup(this);

        // AnswerListActivityから情報を受け取る
        qid = getIntent().getStringExtra("qid");
        aid = getIntent().getStringExtra("aid");
        answerName = getIntent().getStringExtra("answerName");
        answerBody = getIntent().getStringExtra("answerBody");

        // ViewModelを作る
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        // XMLと接続
        tvAnswerName = findViewById(R.id.tvAnswerName);
        tvAnswerBody = findViewById(R.id.tvAnswerBody);
        btnBestAnswer = findViewById(R.id.btnBestAnswer);

        // 回答者名を表示
        tvAnswerName.setText(answerName + "さんの回答");

        // 回答本文を表示
        tvAnswerBody.setText(answerBody);

        // Questionの変更を監視
        questionViewModel.getCurrentQuestion().observe(this, question -> {
            if (question != null) {

                Toast.makeText(
                        this,
                        "ベストアンサーに設定しました",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });

        // ベストアンサーボタン
        btnBestAnswer.setOnClickListener(v -> {

            // この質問のこの回答をベストアンサーに設定
            questionViewModel.setBestAnswer(qid, aid);
        });
    }
}