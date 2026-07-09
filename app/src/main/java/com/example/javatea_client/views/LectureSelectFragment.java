package com.example.javatea_client.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.javatea_client.R;
import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.CategoryViewModel;

import java.util.Collection;
import java.util.List;

public class LectureSelectFragment extends Fragment {

    CategoryViewModel categoryViewModel;
    private LinearLayout layoutLectureList;

    public LectureSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_select, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        layoutLectureList = view.findViewById(R.id.layoutLectureList);

        LectureListActivity activity = (LectureListActivity) requireActivity();
        String univId = activity.getUnivId();
        String facultyName = activity.getFacultyName();
        String departmentName = activity.getDepartmentName();
        String lectureListType = activity.getLectureListType();

        switch(lectureListType) {
            case "general_university":
                categoryViewModel.universityLectures(univId);
                break;
            case "general_faculty":
                categoryViewModel.facultyLectures(univId, facultyName);
                break;
            case "department":
                categoryViewModel.departmentLectures(univId, facultyName, departmentName);
                break;
            default:
                break;
        }

        categoryViewModel.getUnivLectures().observe(getViewLifecycleOwner(), new Observer<Collection<Lecture>>() {
            @Override
            public void onChanged(Collection<Lecture> lectures) {
                // 一度リセット
                layoutLectureList.removeAllViews();

                // ボタン生成
                for (Lecture lecture: lectures) {
                    createLectureButton(lecture);
                }
            }
        });

        categoryViewModel.getFacLectures().observe(getViewLifecycleOwner(), new Observer<Collection<Lecture>>() {
            @Override
            public void onChanged(Collection<Lecture> lectures) {
                // 一度リセット
                layoutLectureList.removeAllViews();

                // ボタン生成
                for (Lecture lecture: lectures) {
                    createLectureButton(lecture);
                }
            }
        });

        categoryViewModel.getDepartLectures().observe(getViewLifecycleOwner(), new Observer<Collection<Lecture>>() {
            @Override
            public void onChanged(Collection<Lecture> lectures) {
                // 一度リセット
                layoutLectureList.removeAllViews();

                // ボタン生成
                for (Lecture lecture: lectures) {
                    createLectureButton(lecture);
                }
            }
        });
    }

    // 授業一覧ボタンを生成
    private void createLectureButton(Lecture lecture) {

        // item_lecture_button.xmlからボタン生成
        Button button = (Button) LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.item_lecture_button,
                        layoutLectureList,
                        false
                );

        String lectureName = lecture.getName();

        // 授業名を表示
        button.setText(lectureName);

        button.setOnClickListener(v -> {

            LectureListActivity activity =
                    (LectureListActivity) requireActivity();

            activity.setDepartmentName(lectureName);
            activity.addCategory(lectureName);

            // 次の画面へ遷移する場合はここに追加
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new QuestionSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        layoutLectureList.addView(button);
    }
}