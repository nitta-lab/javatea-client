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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.QuestionViewModel;

import java.util.ArrayList;

public class AddQuestionFragment extends Fragment {

    private QuestionViewModel questionViewModel;
    private CategoryViewModel categoryViewModel;

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        Javatea javatea = (Javatea) requireActivity().getApplication();

        //作成場所のパス
        LectureListActivity activity = (LectureListActivity) requireActivity();
        String workSpaceTvText = activity.getTvCategory();
        TextView CreatePlaceText = view.findViewById(R.id.CreatePlaceText);
        CreatePlaceText.setText(workSpaceTvText);

        ArrayList<String> tags = activity.getTag();

        String tagText = String.join(", ",tags);


        TextView tagTextFragment = view.findViewById(R.id.TagText);
        tagTextFragment.setText(tagText);

        //閲覧者制限
        Spinner ViewerSpinner = view.findViewById(R.id.ViewerSpinner);
        String[] viewerlist = {"選択してください", "誰でも", "同じ学校", "同じ学部"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, viewerlist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ViewerSpinner.setAdapter(adapter1);

        //回答者制限
        Spinner AnswerSpinner = view.findViewById(R.id.AnswerSpinner);
        String[] answerList = {"選択してください", "誰でも", "同じ学校", "同じ学部"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, answerList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        AnswerSpinner.setAdapter(adapter2);

        //タグ編集(TagFragment)画面遷移
        tagTextFragment.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EditTagFragment())
                    .addToBackStack(null)//情報を渡す
                    .commit();
                }
        );

        String uid = javatea.getUserId();
        String token = javatea.getToken();

        //公開するボタン
        TextView nextButton = view.findViewById(R.id.NextButton);
        nextButton.setOnClickListener(v -> {

            //Title,Questionを取得
            EditText titleTextFragment = view.findViewById(R.id.TitleText);
            EditText questionTextFragment = view.findViewById(R.id.QuestionText);

            //タイトル入力
            String titleText = titleTextFragment.getText().toString();
            if(titleText.isEmpty()) {
                Toast.makeText(getContext(),"タイトルを入力して下さい", Toast.LENGTH_SHORT).show();
                return;
            }

            //Question入力
            String questionText = questionTextFragment.getText().toString();
            if(questionText.isEmpty()) {
                Toast.makeText(getContext(),"質問を入力してください",Toast.LENGTH_SHORT).show();
                return;
            }

            //閲覧者制限選択チェック
            if(ViewerSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(getContext(),"閲覧者制限を選択してください",Toast.LENGTH_SHORT).show();
                return;
            }

            //回答者制限選択チェック
            if(AnswerSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(getContext(),"回答者制限を選択してください",Toast.LENGTH_SHORT).show();
                return;
            }

            String viewPermission = (String)ViewerSpinner.getSelectedItem();
            String resPermission = (String)AnswerSpinner.getSelectedItem();
            String lectureId = activity.getLectureId();

            questionViewModel.getQuestionId().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String qid) {
                    activity.setTag(new ArrayList<>());
                    //categoryResouceのメソッドと同じ名前を使う
                    String univId = activity.getUnivId();
                    String facultyName = activity.getFacultyName();
                    String departmentName = activity.getDepartmentName();
                    if (lectureId.equals("全般")){
                        //全般に送る
                        categoryViewModel.addGeneralQuestion(qid);
                    }
                    else if (lectureId.equals("学校生活")){
                        //学校生活に送る
                        categoryViewModel.addUniversityGeneralQuestion(univId,qid);
                    }
                    else{
                        String lectureListType = activity.getLectureListType();
                        if (lectureListType.equals("general_university")){
                            //大学まで
                            categoryViewModel.addUniversityLectureQuestion(univId,lectureId,qid);
                        }
                        else if (lectureListType.equals("general_faculty")){
                            //学部まで
                            categoryViewModel.addFacultyQuestion(univId,facultyName,lectureId,qid);
                        }
                        else{
                            //学科まで
                            categoryViewModel.addDepartmentQuestion(univId,facultyName,departmentName,lectureId,qid);
                        }
                    }
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new QuestionSelectFragment())
                            .commit();
                }
            });
            questionViewModel.createQuestion(titleText,questionText,uid,tags,viewPermission,resPermission,lectureId,token);

        });
    }
}

//タイトルとQuestionが消える
//公開するボタン押した後に質問が追加されている状態にする
//情報が消える〇