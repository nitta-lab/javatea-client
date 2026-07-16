package com.example.javatea_client.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.javatea_client.viewModels.QuestionViewModel;

public class ViewQuestionFragment extends Fragment {


    private TextView tvTitle;
    private TextView tvQuestionContent;
    private RecyclerView answerList;
    private Button btnAnswer;

    // 質問データを取得するViewModel
    private QuestionViewModel questionViewModel;

    public ViewQuestionFragment() {
        // Fragmentには引数なしコンストラクタが必要


}