package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.AnswerViewModel;
import com.example.javatea_client.viewModels.QuestionViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddAnswerFragment extends Fragment {

    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;
    private UserViewModel userViewModel;
    private String userId;
    private String token;
    private String name;
    private Question displayQuestion;
    private static final String TAG = "AddAnswerFragment";
    private ExecutorService executor;
    private Handler mainHandler;

    public AddAnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_answer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentDebug", "AddAnswerFragmentのonViewCreatedが呼ばれました");

        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        answerViewModel = new ViewModelProvider(requireActivity()).get(AnswerViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        View questionLayout = view.findViewById(R.id.questionLayout);
        View answerLayout = view.findViewById(R.id.answerLayout);
        View previewLayout = view.findViewById(R.id.previewLayout);
        TextView questionTextView = questionLayout.findViewById(R.id.questionBody);
        EditText answerEditText = answerLayout.findViewById(R.id.answerBody);
        TextView previewTextView = previewLayout.findViewById(R.id.previewBody);
        Button backButton = view.findViewById(R.id.backButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button postButton = view.findViewById(R.id.postButton);
        Button editButton = view.findViewById(R.id.editButton);
        View loadingOverlay = view.findViewById(R.id.loadingOverlay);
        LectureListActivity activity = (LectureListActivity) requireActivity(); //Viewmodelやxmlとの接続

        String qid = activity.getQid();
        Javatea app = (Javatea) requireActivity().getApplication();
        userId = app.getUserId();
        token = app.getToken();
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String fetchedName = userViewModel.getName(userId, token);

                mainHandler.post(() -> {
                    if (!isAdded()) return;

                    name = fetchedName;

                    if (fetchedName == null || fetchedName.isBlank()) {
                        Toast.makeText(
                                requireContext(),
                                "ユーザ名を取得できませんでした。",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (!isAdded()) return;

                    Toast.makeText(
                            requireContext(),
                            "ユーザ名の取得中にエラーが発生しました。",
                            Toast.LENGTH_SHORT
                    ).show();

                    Log.e("AddAnswerFragment", "getName failed", e);
                });
            }
        });

        answerViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("error",s);
                Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show(); //エラーメッセージ
            }
        });

        answerViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> { //読み込み中であれば多重送信を防止
            if (loading) {
                loadingOverlay.setVisibility(View.VISIBLE);
            }
            else {
                loadingOverlay.setVisibility(View.GONE);
            }
        });

        answerViewModel.getCreateSuccess().observe(getViewLifecycleOwner(), success -> { //質問の作成が成功した場合画面遷移
            if (Boolean.TRUE.equals(success)) {
                answerViewModel.resetCreateSuccess();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new ViewQuestionFragment())
                        .commit();
            }
        });

        if (!qid.isEmpty()){
            questionViewModel.getQuestion(qid, userId, token); //qidが空でなければquestionを呼び出し
        }

        questionViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                displayQuestion = question;
                if(displayQuestion == null) {
                    questionTextView.setText("選択されていません。");
                }
                else {
                    questionTextView.setText(question.getBody()); //質問本文を取得
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new ViewQuestionFragment())
                        .commit();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewTextView.setText(answerEditText.getText().toString());
                questionLayout.setVisibility(view.INVISIBLE);
                answerLayout.setVisibility(view.INVISIBLE);
                confirmButton.setVisibility(view.INVISIBLE);
                previewLayout.setVisibility(view.VISIBLE);
                editButton.setVisibility(view.VISIBLE);
                postButton.setVisibility(view.VISIBLE); //確認画面へ推移
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionLayout.setVisibility(view.VISIBLE);
                answerLayout.setVisibility(view.VISIBLE);
                confirmButton.setVisibility(view.VISIBLE);
                previewLayout.setVisibility(view.INVISIBLE);
                editButton.setVisibility(view.INVISIBLE);
                postButton.setVisibility(view.INVISIBLE); //編集画面へ推移
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = previewTextView.getText().toString();

                if (body.isBlank()) {
                    String errorMessage = "回答本文を入力してください。";
                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingOverlay.setVisibility(View.VISIBLE); //多重送信防止レイアウトの表示
                    answerViewModel.createAnswer(qid, userId, body, token, name); //回答を作成
                }
            }
        });
    }
}