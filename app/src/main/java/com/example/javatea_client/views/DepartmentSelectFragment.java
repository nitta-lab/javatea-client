package com.example.javatea_client.views;

import android.os.Bundle;
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

public class DepartmentSelectFragment extends Fragment {

    CategoryViewModel categoryViewModel;
    private Button btnFacultyGeneral;
    private LinearLayout layoutDepartmentList;

    public DepartmentSelectFragment() {
        // 必須
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_department_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        btnFacultyGeneral = view.findViewById(R.id.btnFacultyGeneral);
        layoutDepartmentList = view.findViewById(R.id.layoutDepartmentList);

        LectureListActivity activity = (LectureListActivity) requireActivity();
        String univId = activity.getUnivId();
        String facultyName = activity.getFacultyName();

        categoryViewModel.getDepartments(univId,facultyName);
        categoryViewModel.getCurrentDepartment().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> departments) {
                // 一度リセット
                layoutDepartmentList.removeAllViews();

                // ボタン生成
                for (String department: departments) {
                    createDepartmentButton(department);
                }
            }
        });

        // 学部全般ボタン
        btnFacultyGeneral.setOnClickListener(v -> {
            activity.addCategory("学部全般");
            activity.setLectureListType("general_faculty");
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LectureSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void createDepartmentButton(String departmentName) {

        Button button = new Button(requireContext());

        button.setText(departmentName);

        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        button.setOnClickListener(v -> {
            LectureListActivity activity = (LectureListActivity) requireActivity();
            activity.setDepartmentName(departmentName);
            activity.addCategory(departmentName);
            activity.setLectureListType("department");

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LectureSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });
        layoutDepartmentList.addView(button);
    }
}