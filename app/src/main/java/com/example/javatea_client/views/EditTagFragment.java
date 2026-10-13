package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.javatea_client.R;

import java.util.ArrayList;

public class EditTagFragment extends Fragment {

    private ArrayList<String> tagList = new ArrayList<>();

    public EditTagFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //親Activityから大学IDを取得
        LectureListActivity activity = (LectureListActivity) requireActivity();
        EditText addTagText = view.findViewById(R.id.addTagText);
        LinearLayout allTagRow = view.findViewById(R.id.allTagRow);

        //1度追加しているときは、追加したタグ欄に表示しておく。
        tagList = activity.getTag();
        if (!tagList.isEmpty()){
            for (String i : tagList){
                //タグ1行分のLinerLayoutを作る
                LinearLayout tagRow = new LinearLayout(requireContext());
                tagRow.setOrientation(LinearLayout.HORIZONTAL);

                //タグ名を表示する
                TextView tagText = new TextView(requireContext());
                tagText.setText(i);
                tagText.setTextSize(16);

                //タグ名の部分を横いっぱいに広げる
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                tagRow.addView(tagText,textParams);

                //xボタン
                Button deleteButton = new Button(requireContext());
                deleteButton.setText("x");
                tagRow.addView(deleteButton);

                //作った1行をallTagRowに追加
                allTagRow.addView(tagRow);
                addTagText.setText("");

                //xボタンを押したら削除する
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagList.remove(i);
                        allTagRow.removeView(tagRow);
                    }
                });
            }
        }

        //追加するボタンを押したときの処理
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = addTagText.getText().toString();

                //空欄なら追加しない
                if (tagName.isEmpty()){
                    addTagText.setError("タグ名を入力してください");
                    addTagText.setText("");
                    return;
                }

                //重複している場合は追加しない
                if (tagList.contains(tagName)){
                    addTagText.setError("同じタグ名を2つ以上追加することはできません");
                    addTagText.setText("");
                    return;
                }

                //3つ以上タグがある場合は追加しない
                if (tagList.size() == 3){
                    addTagText.setError("タグ名は3つまでしか追加できません");
                    addTagText.setText("");
                    return;
                }

                tagList.add(tagName);

                //タグ1行分のLinerLayoutを作る
                LinearLayout tagRow = new LinearLayout(requireContext());
                tagRow.setOrientation(LinearLayout.HORIZONTAL);

                //タグ名を表示する
                TextView tagText = new TextView(requireContext());
                tagText.setText(tagName);
                tagText.setTextSize(16);

                //タグ名の部分を横いっぱいに広げる
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                tagRow.addView(tagText,textParams);

                //xボタン
                Button deleteButton = new Button(requireContext());
                deleteButton.setText("x");
                tagRow.addView(deleteButton);

                //作った1行をallTagRowに追加
                allTagRow.addView(tagRow);
                addTagText.setText("");

                //xボタンを押したら削除する
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagList.remove(tagName);
                        allTagRow.removeView(tagRow);
                    }
                });

            }
        });

        //確定ボタン→activityにタグ情報を送る→画面遷移
        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setTag(tagList);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AddQuestionFragment())
                        .commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_tag, container, false);
    }
}