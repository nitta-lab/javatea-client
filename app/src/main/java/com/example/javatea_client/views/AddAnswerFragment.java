package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

public class AddAnswerFragment extends Fragment {

    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;
    private UserViewModel userViewModel;
    private String userId;
    private String token;
    private String name;
    private Question displayQuestion;
    private static final String TAG = "AddAnswerFragment";

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

        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        answerViewModel = new ViewModelProvider(requireActivity()).get(AnswerViewModel.class);
        View question = view.findViewById(R.id.questionLayout);
        View answer = view.findViewById(R.id.answerLayout);
        View preview = view.findViewById(R.id.previewLayout);
        TextView questionTextView = question.findViewById(R.id.questionBody);
        EditText answerEditText = answer.findViewById(R.id.answerBody);
        TextView previewTextView = preview.findViewById(R.id.previewBody);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button postButton = view.findViewById(R.id.postButton);
        Button editButton = view.findViewById(R.id.editButton);
        View loadingOverlay = view.findViewById(R.id.loadingOverlay);
        LectureListActivity activity = (LectureListActivity) requireActivity(); //Viewmodelやxmlとの接続

        String qid = activity.getQid();
        Javatea app = (Javatea) requireActivity().getApplication();
        userId = app.getUserId();
        token = app.getToken();
        name = userViewModel.getName(userId, token);

        answerViewModel.getAnswer().observe(getViewLifecycleOwner(), new Observer<Answer>() {
            @Override
            public void onChanged(Answer answer) {
                loadingOverlay.setVisibility(View.GONE); //多重送信防止用レイアウトを非表示

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ViewQuestionFragment())
                        .commit(); //画面推移
            }
        });

        answerViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                loadingOverlay.setVisibility(View.GONE); //多重送信防止用レイアウトを非表示

                Log.d("error",s);
                Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show(); //エラーメッセージ
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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewTextView.setText(answerEditText.getText().toString());
                question.setVisibility(view.INVISIBLE);
                answer.setVisibility(view.INVISIBLE);
                confirmButton.setVisibility(view.INVISIBLE);
                preview.setVisibility(view.VISIBLE);
                editButton.setVisibility(view.VISIBLE);
                postButton.setVisibility(view.VISIBLE); //確認画面へ推移
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.setVisibility(view.VISIBLE);
                answer.setVisibility(view.VISIBLE);
                confirmButton.setVisibility(view.VISIBLE);
                preview.setVisibility(view.INVISIBLE);
                editButton.setVisibility(view.INVISIBLE);
                postButton.setVisibility(view.INVISIBLE); //編集画面へ推移
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingOverlay.setVisibility(View.VISIBLE); //多重送信防止レイアウトの表示
                answerViewModel.createAnswer(qid, userId, previewTextView.toString(), token, name); //回答を作成
            }
        });
    }
}