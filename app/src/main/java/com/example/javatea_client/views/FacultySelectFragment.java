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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

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
        categoryViewModel =
                new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        // 親Activityから大学IDを取得
        LectureListActivity activity =
                (LectureListActivity) requireActivity();

        String univId = activity.getUnivId();
        Log.d("FacultySelect", "univId = " + univId);

        // 学部一覧取得
        categoryViewModel.getFaculty(univId);

        // 学部一覧を監視
        categoryViewModel.getCurrentFaculty().observe(
                getViewLifecycleOwner(),
                new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> currentFaculties) {

                        // 一度ボタンを全部削除
                        layoutFacultyList.removeAllViews();
                        Log.d("kawa1", "univId = ");
                        if (currentFaculties == null) {
                            return;
                        }
                        Log.d("kawa2", "univId = ");

                        // 学部数だけボタン生成
                        for (String facultyName : currentFaculties) {
                            createFacultyButton(facultyName);
                        }
                    }
                });

        // 「大学全般」ボタン
        btnUniversityGeneral.setOnClickListener(v -> {

            activity.addCategory("大学全般");

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            new DepartmentSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * 学部ボタンを1つ生成する
     */
    private void createFacultyButton(String facultyName) {

        Button button = new Button(requireContext());

        button.setText(facultyName);

        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        button.setOnClickListener(v -> {

            LectureListActivity activity =
                    (LectureListActivity) requireActivity();

            // カテゴリ表示更新
            activity.addCategory(facultyName);

            // （後でDepartmentSelectFragmentで使うため）
             activity.setFacultyName(facultyName);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            new DepartmentSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        layoutFacultyList.addView(button);
    }
}