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
import com.example.javatea_client.viewModels.CategoryViewModel;

import java.util.List;

public class FacultySelectFragment extends Fragment {

    private CategoryViewModel categoryViewModel;

    private Button btnUniversityGeneral;
    private LinearLayout layoutFacultyList;

    public FacultySelectFragment() {
        // 必須
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faculty_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Viewの取得
        btnUniversityGeneral = view.findViewById(R.id.btnUniversityGeneral);
        layoutFacultyList = view.findViewById(R.id.layoutFacultyList);

        // ViewModel取得
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        // 親Activityから大学IDを取得
        LectureListActivity activity = (LectureListActivity) requireActivity();
        String univId = activity.getUnivId();
        Log.d("FacultySelect", "univId = " + univId);

        // 学部一覧取得
        categoryViewModel.getFaculty(univId);

        // 学部一覧を監視
        categoryViewModel.getCurrentFaculty().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> currentFaculties) {

                // 一度ボタンを全部削除
                layoutFacultyList.removeAllViews();
                if (currentFaculties == null) {
                    return;
                }

                // 学部数だけボタン生成
                for (String facultyName : currentFaculties) {
                    createFacultyButton(facultyName);
                }
            }
        });

        // 「大学全般」ボタン
        btnUniversityGeneral.setOnClickListener(v -> {
            activity.addCategory("大学全般", "【大学全般】");
            activity.setLectureListType("general_university");

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LectureSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * 学部ボタンを1つ生成する
     */
    private void createFacultyButton(String facultyName) {

        // item_faculty_button.xmlからボタンを生成
        Button button = (Button) LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.item_faculty_button,
                        layoutFacultyList,
                        false
                );

        // ボタンに学部名を設定
        button.setText(facultyName);

        // ボタン押下時の処理
        button.setOnClickListener(v -> {

            LectureListActivity activity =
                    (LectureListActivity) requireActivity();

            // 次画面で使用する学部名を保存
            activity.setFacultyName(facultyName);

            // カテゴリ表示更新
            activity.addCategory(facultyName, "学部");

            // 学科選択画面へ
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            new DepartmentSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // 画面へ追加
        layoutFacultyList.addView(button);
    }

}