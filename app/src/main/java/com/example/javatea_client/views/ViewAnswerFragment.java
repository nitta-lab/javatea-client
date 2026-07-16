package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;
import com.example.javatea_client.viewModels.AnswerViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

public class ViewAnswerFragment extends Fragment {

    private AnswerViewModel answerViewModel;
    private String userId;
    private String token;
    private Answer displayAnswer;
    private TextView answererTextView;
    private TextView answerBodyTextView;

    public ViewAnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_answer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //AnswerViewModelの取得
        answerViewModel = new ViewModelProvider(requireActivity()).get(AnswerViewModel.class);

        LectureListActivity activity = (LectureListActivity) requireActivity();

        //前の画面から情報を取得
        String qid = getArguments().getString("qid");
        String aid = getArguments().getString("aid");

        //Javateaから情報を取得
        Javatea javatea = (Javatea) requireActivity().getApplication();
        userId = javatea.getUserId();
        token = javatea.getToken();

        //指定aidのAnswerを取得する
        answerViewModel.loadAnswer(qid,aid,userId,token);

        answerViewModel.getAnswer().observe(getViewLifecycleOwner(), new Observer<Answer>() {
            @Override
            public void onChanged(Answer answer) {
                displayAnswer = answer;
                setAnswerer(view);
            }
        });
        setAnswerer(view);

        Button backButton = view.findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //画面遷移
                //getParentFragmentManager().beginTransaction().replace(R.id.fragment_container)
            }
        });
    }

    private void setAnswerer(View view){
        answererTextView = view.findViewById(R.id.answer);
        if(displayAnswer == null){
            answererTextView.setText("");
            answerBodyTextView.setText("");
        }else{
            String answerer = displayAnswer.getUid();
            answererTextView.setText(answerer + "さんの回答");
            answerBodyTextView.setText("Answer : \n" + displayAnswer.getBody());
        }
    }
}