package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ScrollCaptureCallback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.AnswerViewModel;
import com.example.javatea_client.viewModels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class AddAnswerFragment extends Fragment {

    private AnswerViewModel answerViewModel;
    private UserViewModel userViewModel;
    private Javatea javatea;

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

        TextView questionHeader = view.findViewById(R.id.questionHeader);
        ScrollView questionFrame = view.findViewById(R.id.questionFrame);
        TextInputEditText questionBody = view.findViewById(R.id.questionBody);
        TextView answerHeader = view.findViewById(R.id.answerHeader);
        ScrollView answerFrame = view.findViewById(R.id.answerFrame);
        TextInputEditText answerBody = view.findViewById(R.id.answerBody);
        TextView previewHeader = view.findViewById(R.id.previewHeader);
        View previewDivider = view.findViewById(R.id.previewDivider);
        ScrollView previewFrame = view.findViewById(R.id.previewFrame);
        TextInputEditText previewBody = view.findViewById(R.id.previewBody);
        View loadingOverlay = view.findViewById(R.id.loadingOverlay);


    }
}