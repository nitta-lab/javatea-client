package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.AnswerViewModel;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.QuestionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    private TextView tvStatus;
    private Button btnSearchHome;
    private RecyclerView rvSearchResultList;

    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;
    private CategoryViewModel categoryViewModel;

    // Intent の Key 定義
    public static final String EXTRA_SEARCH_TYPE = "EXTRA_SEARCH_TYPE"; // "UNIVERSITY" or "GENERAL"
    public static final String EXTRA_UNIV_ID = "EXTRA_UNIV_ID";
    public static final String EXTRA_KEYWORDS = "EXTRA_KEYWORDS"; // ArrayList<String>

    private QuestionAdapter questionAdapter; // 質問一覧用 Adapter
    private final List<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);

        //Navigationのセットアップ
        Navigation.setup(this);
        ModeBar.setup(this, "検索結果");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. XMLのViewコンポーネントを紐付け (findViewById)
        // ※ XML内の ID に合わせて指定しています
        tvStatus = findViewById(R.id.textView4);
        btnSearchHome = findViewById(R.id.button);
        rvSearchResultList = findViewById(R.id.search_result_recycler_view); // ※XMLのRecyclerViewにIDを追加してください

//        // 2. RecyclerViewの設定
        if (rvSearchResultList != null) {
            rvSearchResultList.setLayoutManager(new LinearLayoutManager(this));

//            // 追加(以下9行)
            List<Question> dummyList = new ArrayList<>();
//            Question q1 = new Question();
//            q1.setTitle("Javaの配列とArrayListの違いについて");
//            q1.setQid("qid11");
//            dummyList.add(q1);
//            Question q2 = new Question();
//            q2.setTitle("Java");
//            dummyList.add(q2);
//            Question q3 = new Question();
//            q3.setTitle("JavaTea");
//            dummyList.add(q3);

            questionAdapter = new QuestionAdapter(questionList, question -> {
                // タップされた質問のデータ（question）からIDやタイトルを取り出す
                String qid = question.getQid(); // 質問IDを取得するメソッド（モデルに合わせて変更してください）

                // 遷移先の画面（例: QuestionDetailActivity）へインテントを発行
                Intent intent = new Intent(SearchResultActivity.this, LectureListActivity.class);

                // 必要に応じてデータを次の画面に渡す
                intent.putExtra("navigation_target", "ViewQuestion");
                intent.putExtra("qid", qid);

                // 画面を起動
                startActivity(intent);
            });


            rvSearchResultList.setAdapter(questionAdapter);
        }

        // 追加
//        if (tvStatus != null) {
//            tvStatus.setText("検索結果");
//        }

        // 2. RecyclerView と Adapter の設定
//        rvSearchResultList.setLayoutManager(new LinearLayoutManager(this));
//        questionAdapter = new QuestionAdapter(questionList, question -> {
//            // 質問項目がタップされた時の画面遷移処理（例: 質問詳細画面へ）
//             Intent intent = new Intent(SearchResultActivity.this, LectureListActivity.class);
//             intent.putExtra("qid", question.getQid());
//             startActivity(intent);
//        });
//        rvSearchResultList.setAdapter(questionAdapter);

        // 3. 「検索ホームへ」ボタンのクリック処理
        if (btnSearchHome != null) {
            btnSearchHome.setOnClickListener(v -> {
                // 検索ホーム画面（例: SearchHomeActivity）へ遷移する場合
                Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                startActivity(intent);
                finish(); // 現在の画面を閉じる場合
            });
        }



        // 4. Intentから遷移パラメータ（qid等）の取得
        String qid = getIntent().getStringExtra("qid");

        // 6. ViewModelのセットアップ
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // 7. データ監視 (Observe)
        questionViewModel.getCurrentQuestion().observe(this, question -> {
            if (question != null && tvStatus != null) {
                // 必要に応じてテキストの更新
                 tvStatus.setText(question.getTitle());
            }
        });

        answerViewModel.getAnswers().observe(this, answers -> {
            if (answers != null && rvSearchResultList != null) {
                List<Answer> answersData = new ArrayList<>(answers.values());
                AnswerAdapter adapter = new AnswerAdapter(answersData, null, answer -> {
                    String aid = answer.getAid();
                    // 項目タップ時の処理
                });
                rvSearchResultList.setAdapter(adapter);
            }
        });

        // 5. LiveData の監視（Observe）を onCreate 内に直接記述
        categoryViewModel.getUniversityQuestions().observe(this, questions -> {
            if (questions != null) {
                questionList.clear();
                questionList.addAll(questions);
                questionAdapter.notifyDataSetChanged();
                if (tvStatus != null) {
                    tvStatus.setText("検索結果: " + questions.size() + "件");
                }
            }
        });

        categoryViewModel.getUniversityKeyWordsQuestions().observe(this, questions -> {
            if (questions != null) {
                questionList.clear();
                questionList.addAll(questions);
                questionAdapter.notifyDataSetChanged();
                if (tvStatus != null) {
                    tvStatus.setText("検索結果: " + questions.size() + "件");
                }
            }
        });

        categoryViewModel.getGeneralQuestions().observe(this, questions -> {
            if (questions != null) {
                questionList.clear();
                questionList.addAll(questions);
                questionAdapter.notifyDataSetChanged();
                if (tvStatus != null) {
                    tvStatus.setText("検索結果: " + questions.size() + "件");
                }
            }
        });

        categoryViewModel.getGeneralKeyWordsQuestions().observe(this, questions -> {
            if (questions != null) {
                questionList.clear();
                questionList.addAll(questions);
                questionAdapter.notifyDataSetChanged();
                if (tvStatus != null) {
                    tvStatus.setText("検索結果: " + questions.size() + "件");
                }
            }
        });


        //  通信関連なのでコメントアウト
        // 5. Applicationから認証情報の取得
        Javatea javatea = (Javatea) getApplication();
        String uid = javatea.getUserId();
        String token = javatea.getToken();

        Intent intent = getIntent();
        String searchType = intent.getStringExtra(EXTRA_SEARCH_TYPE);
        String univId = intent.getStringExtra(EXTRA_UNIV_ID);
        ArrayList<String> keywords = intent.getStringArrayListExtra(EXTRA_KEYWORDS);

        boolean hasKeywords = (keywords != null && !keywords.isEmpty());

        if("UNIVERSITY".equals(searchType)) {
            if(hasKeywords) {
                categoryViewModel.setUniversityKeyWordsQuestions(univId, uid, token, keywords);
            } else {
                categoryViewModel.setUniversityQuestions(univId, uid, token);
            }
        } else {
            if(hasKeywords) {
                categoryViewModel.setGeneralKeyWordsQuestions(uid, token, keywords);
            } else {
                categoryViewModel.setGeneralQuestions(uid, token);
            }
        }



        // 8. データの取得呼び出し
        if (qid != null) {
            questionViewModel.getQuestion(qid, uid, token);
        }

//        // ダミーデータ用
//        List<Question> dummyList = new ArrayList<>();
//
//        // ※ Question クラスのメソッド（setTitle, setBody など）に合わせて変更してください
//        Question q1 = new Question();
//        q1.setTitle("Javaの配列とArrayListの違いについて");
//        q1.setBody("初心者です。使い分けがよく分かりません。");
//        q1.setQid("qid12");
//        q1.setTags(new ArrayList<String>(List.of("ロボティクス","テスト")));
//
//
//        Question q2 = new Question();
//        q2.setTitle("RecyclerViewが表示されません");
//        q2.setBody("Adapterを設定したのに画面が表示されません。");
//        q1.setQid("qid13");
//        q1.setTags(new ArrayList<String>(List.of("ロボティクス","テスト")));
//
//        dummyList.add(q1);
//        dummyList.add(q2);
//
//        // リストに反映して表示を更新
//        questionList.clear();
//        questionList.addAll(dummyList);
//        questionAdapter.notifyDataSetChanged();
//
//        if (tvStatus != null) {
//            tvStatus.setText("検索結果: " + dummyList.size() + "件（テスト）");
//        }


    }

    private void updateQuestionList(Set<Question> questions) {
        if (questions != null) {
            questionList.clear();
            questionList.addAll(questions);
            questionAdapter.notifyDataSetChanged();

            if (tvStatus != null) {
                tvStatus.setText("検索結果: " + questions.size() + "件");
            }
        }
    }
}