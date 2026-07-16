package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        answerViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("error",s);
                Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });

        LectureListActivity activity = (LectureListActivity) requireActivity();

        //親アクティビティから情報を取得
        String qid = activity.getQid();
        String aid = activity.getAid();

        //Javateaから情報を取得
        Javatea javatea = (Javatea) requireActivity().getApplication();
        userId = javatea.getUserId();
        token = javatea.getToken();

        if(!(qid.isEmpty()||aid.isEmpty())){
            //指定aidのAnswerを取得する
            answerViewModel.loadAnswer(qid,aid,userId,token);
        }

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
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, new viewQuestionFragment())
//                        .commit();
            }
        });
    }

    private void setAnswerer(View view){
        answererTextView = view.findViewById(R.id.answer_text);
        answerBodyTextView = view.findViewById(R.id.answer_body);
        answererTextView.setMovementMethod(new ScrollingMovementMethod());//スクロールできるようにする。
        if(displayAnswer == null){
            answererTextView.setText("選択されてません");
            answerBodyTextView.setText("選択されてません");
        }else{
            String answerer = displayAnswer.getUid();
            answererTextView.setText(answerer + "さんの回答");
            answerBodyTextView.setText("Answer : \n" + displayAnswer.getBody());
        }
    }
}