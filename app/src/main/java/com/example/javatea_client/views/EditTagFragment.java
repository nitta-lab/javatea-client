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

        //追加するボタンを押したときの処理
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = addTagText.getText().toString();
                tagList.add(tagName);

                TextView tagText = new TextView(requireContext());
                tagText.setText(tagName);
                tagText.setTextSize(16);

                allTagRow.addView(tagText);

                //xボタンも追加するように
            }
        });
        //xボタンを押したら削除する

        //確定ボタン→activityにタグ情報を送る→画面遷移


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_tag, container, false);
    }
}