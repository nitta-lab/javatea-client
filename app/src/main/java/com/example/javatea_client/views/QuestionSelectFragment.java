//package com.example.javatea_client.views;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.javatea_client.R;
//import com.example.javatea_client.viewModels.CategoryViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class QuestionSelectFragment extends Fragment {
//
//    private LinearLayout layoutQuestionList;
//    // 後で使用
//    private CategoryViewModel categoryViewModel;
//
//    public QuestionSelectFragment() {
//        // 必須
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_question_select,
//                container,
//                false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view,
//                              @Nullable Bundle savedInstanceState) {
//
//        super.onViewCreated(view, savedInstanceState);
//
//        layoutQuestionList = view.findViewById(R.id.layoutQuestionList);
//
////        // 選択された授業IDを取得
////        LectureListActivity activity = (LectureListActivity) requireActivity();
////        String lectureId = activity.getLectureId();
////
////        // 確認用（あとで削除）
////        Log.d("QuestionSelect", "lectureId = " + lectureId);
////
////        switch (lectureId) {
////            case "全般":
////                // 全般の質問一覧を取得
////                // categoryViewModel.loadGeneralQuestions();
////                break;
////            case "学校生活":
////                // 学校生活の質問一覧を取得
////                // categoryViewModel.loadSchoolLifeQuestions();
////                break;
////            default:
////                // 授業の質問一覧を取得
////                // categoryViewModel.loadLectureQuestions(lectureId);
////                break;
////        }
//        // 選択された授業IDときいている階層を取得
//        LectureListActivity activity = (LectureListActivity) requireActivity();
//        String lectureId = activity.getLectureId();
//        String univId = activity.getUnivId();
//        String facultyName = activity.getFacultyName();
//        String departmentName = activity.getDepartmentName();
//        String lectureListType = activity.getLectureListType();
//
//        if ("学校生活".equals(lectureId)) {
//            // 学校生活に関する質問一覧を取得(階層に関係なく大学単位で固定)
//            categoryViewModel.universityGeneralQuestions(univId);
//        } else {
//            // 「全般」も実際の授業も、どの階層(大学/学部/学科)で選ばれたかで呼ぶメソッドが変わる
//            switch (lectureListType) {
//                case "大学全般":
//                    // 大学全般の質問一覧を取得(lectureIdが"全般"ならその大学の全般質問、
//                    // 実際の授業IDならその大学特有の授業の質問)
//                    categoryViewModel.universityQuestions(univId, lectureId);
//                    break;
//
//                case "":
//                    // 学部全般 or 学部特有の授業の質問一覧を取得
//                    categoryViewModel.facultyQuestions(univId, facultyName, lectureId);
//                    break;
//
//                case "department":
//                    // 学科全般 or 学科特有の授業の質問一覧を取得
//                    categoryViewModel.departmentQuestions(univId, facultyName, departmentName, lectureId);
//                    break;
//
//                default:
//                    Log.w("QuestionSelect", "未知のlectureListType: " + lectureListType);
//                    break;
//            }
//        }
//
//        // 仮データ（categoryViewModel完成後に削除予定）
//        List<String> questions = new ArrayList<>();
//
//        questions.add("この授業のテストは難しいですか？");
//        questions.add("レポートは毎週ありますか？");
//        questions.add("出席は厳しいですか？");
//        questions.add("おすすめの教科書はありますか？");
//
//        // QuestionViewModel完成後は
//        // questionViewModel.getQuestions().observe(
//        //      getViewLifecycleOwner(), questions -> {
//        //          layoutQuestionList.removeAllViews();
//        //          for (Question question : questions) {
//        //              createQuestionButton(question.getTitle());
//        //          }
//        //      });
//
//        for (String question : questions) {
//            createQuestionButton(question);
//        }
//    }
//
//    /**
//     * 質問ボタン生成
//     */
//    private void createQuestionButton(String questionTitle) {
//
//        Button button = (Button) LayoutInflater.from(requireContext())
//                .inflate(
//                        R.layout.item_question_button,
//                        layoutQuestionList,
//                        false
//                );
//
//        button.setText(questionTitle);
//
//        button.setOnClickListener(v -> {
//
//            // TODO:
//            // QuestionDetailFragmentへ遷移
//            // activity.setQuestionId(question.getQid());
//
//        });
//
//        layoutQuestionList.addView(button);
//    }
//}








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

import com.example.javatea_client.R;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.viewModels.CategoryViewModel;

import java.util.Set;

public class QuestionSelectFragment extends Fragment {

    private static final String TAG = "QuestionSelect";

    private LinearLayout layoutQuestionList;
    private CategoryViewModel categoryViewModel;

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

        layoutQuestionList = view.findViewById(R.id.layoutQuestionList);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        // 質問系のLiveDataは1本化されたのでobserveは1回だけでよい
        categoryViewModel.getQuestions().observe(getViewLifecycleOwner(), new Observer<Set<Question>>() {
            @Override
            public void onChanged(Set<Question> questions) {
                renderQuestions(questions);
            }
        });

        // 選択された授業IDときいている階層を取得
        LectureListActivity activity = (LectureListActivity) requireActivity();
        String lectureId = activity.getLectureId();
        String univId = activity.getUnivId();
        String facultyName = activity.getFacultyName();
        String departmentName = activity.getDepartmentName();
        String lectureListType = activity.getLectureListType();

        Log.d(TAG, "lectureId = " + lectureId + ", lectureListType = " + lectureListType);

        if ("全般".equals(lectureId)) {
            // 全般の質問一覧(階層に関係なく全体で共通)
            categoryViewModel.generalQuestions();
            return;
        }

        if ("学校生活".equals(lectureId)) {
            // 学校生活に関する質問一覧(階層に関係なく大学単位で固定)
            categoryViewModel.universityGeneralQuestions(univId);
            return;
        }

        // 実際の授業の質問一覧を、どの階層(大学/学部/学科)で選ばれたかに応じて取得
        switch (lectureListType) {
            case "general_university":
                categoryViewModel.universityQuestions(univId, lectureId);
                break;

            case "general_faculty":
                categoryViewModel.facultyQuestions(univId, facultyName, lectureId);
                break;

            case "department":
                categoryViewModel.departmentQuestions(univId, facultyName, departmentName, lectureId);
                break;

            default:
                Log.w(TAG, "未知のlectureListType: " + lectureListType);
                break;
        }
    }

    // 質問一覧を描画する(getQuestions()のobserveから呼ばれる)
    private void renderQuestions(Set<Question> questions) {
        layoutQuestionList.removeAllViews();

        if (questions == null) {
            return;
        }

        for (Question question : questions) {
            createQuestionButton(question);
        }
    }

    /**
     * 質問ボタン生成
     */
    private void createQuestionButton(Question question) {

        Button button = (Button) LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.item_question_button,
                        layoutQuestionList,
                        false
                );

        button.setText(question.getTitle());

        button.setOnClickListener(v -> {

            // TODO:
            // QuestionDetailFragmentへ遷移
            // activity.setQuestionId(question.getQid());

        });

        layoutQuestionList.addView(button);
    }
}