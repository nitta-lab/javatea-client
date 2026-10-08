package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.example.javatea_client.viewModels.LectureViewModel;
import com.example.javatea_client.viewModels.QuestionViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;


public class AddQuestionFragment extends Fragment {

    private QuestionViewModel questionViewModel;

    private LectureViewModel lectureViewModel;

    private Javatea javatea;



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
        String[] answewlist = {"選択してください", "誰でも", "同じ学校", "同じ学部"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, answewlist);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        AnswerSpinner.setAdapter(adapter2);

        //タグ編集(TagFragment)画面遷移
        tagTextFragment.setOnClickListener(v -> {
                    EditTagFragment editTagFragment = new EditTagFragment();

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.QuestionLinearLayout, editTagFragment)
                            .addToBackStack(null)
                            .commit();
                }
        );


        //Title,Questionを取得
        EditText titletextfragment = view.findViewById(R.id.TitleText);
        EditText questiontextfragment = view.findViewById(R.id.QuestionText);

        String uid = javatea.getUserId();
        String token = javatea.getToken();

        //公開するボタン
        Button nextButton = view.findViewById(R.id.NextButton);
        nextButton.setOnClickListener(v -> {

            //タイトル入力
            String titleText = titletextfragment.getText().toString();
            if (titleText.isEmpty()) {
                Toast.makeText(getContext(),"タイトルを入力して下さい", Toast.LENGTH_SHORT).show();
                return;
            }

            //Question入力
            String questionText = questiontextfragment.getText().toString();
            if(questionText.isEmpty()) {
                Toast.makeText(getContext(),"質問を入力してください",Toast.LENGTH_SHORT).show();
                return;
            }

            //閲覧者制限選択チェック
            if(ViewerSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(getContext(),"閲覧者制限を選択してください",Toast.LENGTH_SHORT).show();
                return;
            }

            //解答者制限選択チェック
            if(AnswerSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(getContext(),"解答者制限を選択してください",Toast.LENGTH_SHORT).show();
            }

            String viewPermission = (String)ViewerSpinner.getSelectedItem();
            String resPermission = (String)AnswerSpinner.getSelectedItem();
            String lectureId = activity.getLectureId();

            //
            questionViewModel.createQuestion(titleText,questionText,uid,tags,viewPermission,resPermission,lectureId,token);

            QuestionSelectFragment questionSelectFragment = new QuestionSelectFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.QuestionLinearLayout, questionSelectFragment)
                    .addToBackStack(null)
                    .commit();

            //タグ入力チェック
//            String tagtext = tagtextfragment.getText().toString();
//            if(tagtext.isEmpty()){
//                tagtext = null;
//            }
        });
    }
}

