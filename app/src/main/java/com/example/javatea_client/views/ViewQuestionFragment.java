package com.example.javatea_client.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Answer;
import com.example.javatea_client.viewModels.AnswerViewModel;
import com.example.javatea_client.viewModels.QuestionViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionFragment extends Fragment {

    //変数宣言
    private TextView tvTitle;
    private TextView tvQuestionContent;
    private RecyclerView answerList;
    private Button btnAnswer;
    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;


    //getter追加

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //意味調べる
        super.onViewCreated(view, savedInstanceState);

        //xmlとの接続
        tvTitle = view.findViewById(R.id.tvTitle);
        tvQuestionContent = view.findViewById(R.id.tvQuestionContent);
        answerList = view.findViewById(R.id.answer_list);
        btnAnswer = view.findViewById(R.id.btnAnswer);

        //回答一覧を縦に並べるための設定
        answerList.setLayoutManager(new LinearLayoutManager(requireContext())
        );

        //LectureListActivityとの連携
        LectureListActivity activity = (LectureListActivity) requireActivity();

        //qid取得
        String qid = activity.getQid();

        //サーバー通信に必要なuid,token取得
        //javatea取得してuid,とtoken取りだし
        Javatea javatea = (Javatea) requireActivity().getApplication();

        String uid = javatea.getUserId();
        String token = javatea.getToken();


        //ViewModel取得
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);

        answerViewModel = new ViewModelProvider(requireActivity()).get(AnswerViewModel.class);


        //quesitonを監視するobserve() question ->で値が動いたら｛｝の中を実行
        questionViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {

                    tvTitle.setText(question.getTitle());
                    tvQuestionContent.setText(question.getBody());

                }
        );

        //質問取得
        questionViewModel.getQuestion(qid, uid, token);


        //解答一覧を監視する
        answerViewModel.getAnswers().observe(getViewLifecycleOwner(), answers -> {

                    List<Answer> answersData = new ArrayList<>(answers.values());

                    AnswerAdapter adapter = new AnswerAdapter(answersData, null, answer -> {

                        //押された回答のaidを取得
                        String aid = answer.getAid();

                        //LectureListActivityにaidを保存
                        activity.setAid(aid);

                        //ViewAnswerFragmentへ遷移
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewAnswerFragment()).addToBackStack(null).commit();

                    }
                    );

                    answerList.setAdapter(adapter);


                }
        );

        //サーバへ取得の依頼
        answerViewModel.loadAnswers(qid, uid, token);


        //回答するボタンを押したとき
        btnAnswer.setOnClickListener(v -> {

            //AddAnswerFragmentへ遷移
            //requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddAnswerFragment()).addToBackStack(null).commit();
        });

    }

}