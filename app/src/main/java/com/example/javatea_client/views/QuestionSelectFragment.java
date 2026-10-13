package com.example.javatea_client.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.CategoryViewModel;

import java.util.Set;

public class QuestionSelectFragment extends Fragment {

    private static final String TAG = "QuestionSelect";

    private LinearLayout layoutQuestionList;
    private CategoryViewModel categoryViewModel;
    private Javatea javatea;

    public QuestionSelectFragment() {
        // 必須
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_question_select,
                container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // 選択された授業IDときいている階層を取得
        LectureListActivity activity = (LectureListActivity) requireActivity();
        String lectureId = activity.getLectureId();
        String univId = activity.getUnivId();
        String facultyName = activity.getFacultyName();
        String departmentName = activity.getDepartmentName();
        String lectureListType = activity.getLectureListType();

        layoutQuestionList = view.findViewById(R.id.layoutQuestionList);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        // 質問系のLiveDataは1本化されたのでobserveは1回だけでよい
        categoryViewModel.getQuestions().observe(getViewLifecycleOwner(), new Observer<Set<Question>>() {
            @Override
            public void onChanged(Set<Question> questions) {
                renderQuestions(questions, activity);
            }
        });

        Button questionButton = view.findViewById(R.id.btnBottom);
        createQuestionButton(questionButton);

        javatea = (Javatea) activity.getApplication();

        Log.d(TAG, "lectureId = " + lectureId + ", lectureListType = " + lectureListType);

        if ("全般".equals(lectureId)) {
            // 全般の質問一覧(階層に関係なく全体で共通)
            categoryViewModel.generalQuestions(javatea.getUserId(), javatea.getToken());
            return;
        }

        if ("学校生活".equals(lectureId)) {
            // 学校生活に関する質問一覧(階層に関係なく大学単位で固定)
            categoryViewModel.universityGeneralQuestions(univId, javatea.getUserId(), javatea.getToken());
            return;
        }

        // 実際の授業の質問一覧を、どの階層(大学/学部/学科)で選ばれたかに応じて取得
        switch (lectureListType) {
            case "general_university":
                categoryViewModel.universityQuestions(univId, lectureId, javatea.getUserId(), javatea.getToken());
                break;

            case "general_faculty":
                categoryViewModel.facultyQuestions(univId, facultyName, lectureId, javatea.getUserId(), javatea.getToken());
                break;

            case "department":
                Log.w(TAG, "javatea.getUserId" + javatea.getUserId());
                Log.w(TAG, "javatea.getToken" + javatea.getToken());
                categoryViewModel.departmentQuestions(univId, facultyName, departmentName, lectureId, javatea.getUserId(), javatea.getToken());
                break;

            default:
                Log.w(TAG, "未知のlectureListType: " + lectureListType);
                break;
        }
    }

    // 質問一覧を描画する(getQuestions()のobserveから呼ばれる)
    private void renderQuestions(Set<Question> questions, LectureListActivity activity) {
        layoutQuestionList.removeAllViews();

        if (questions == null) {
            return;
        }

        for (Question question : questions) {
            createAnswerButton(question, activity);
        }
    }

    /**
     * 質問ボタン生成
     */
    private void createAnswerButton(Question question, LectureListActivity activity) {

        Button button = (Button) LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.item_question_button,
                        layoutQuestionList,
                        false
                );

        button.setText(question.getTitle());

        button.setOnClickListener(v -> {

            // TODO:
            Log.d("Qid", question.getQid());
            activity.setQid(question.getQid());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ViewQuestionFragment())
                    .addToBackStack(null)
                    .commit();
        });

        layoutQuestionList.addView(button);
    }

    private void createQuestionButton(Button questionButton) {
        questionButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddQuestionFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}