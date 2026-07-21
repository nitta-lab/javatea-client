package com.example.javatea_client.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.QuestionViewModel;

public class ViewQuestionFragment extends Fragment {

    //変数宣言
    private TextView tvTitle;
    private TextView tvQuestionContent;
    private RecyclerView answerList;
    private Button btnAnswer;
    private QuestionViewModel questionViewModel;

    //getter追加

    @Override
    public  void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //意味調べる
        super.onViewCreated(view, savedInstanceState);

        //xmlとの接続
        tvTitle = view.findViewById(R.id.tvTitle);
        tvQuestionContent = view.findViewById(R.id.tvQuestionContent);
        answerList = view.findViewById(R.id.answer_list);
        btnAnswer = view.findViewById(R.id.btnAnswer);

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

        //quesitonを監視するobserve() question ->で値が動いたら｛｝の中を実行
        questionViewModel.getQuestionLiveData().observe(getViewLifecycleOwner(), question -> {

            tvTitle.setText(question.getTitle());


             }
        );






    }


}