package com.example.javatea_client.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javatea_client.R;

public class UniversityFragment extends Fragment {

    private Button btnLecture;
    private Button btnSchoolLife;

    public UniversityFragment() {
        // 必須
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_university, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLecture = view.findViewById(R.id.btnLecture);
        btnSchoolLife = view.findViewById(R.id.btnSchoolLife);

        btnLecture.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FacultySelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnSchoolLife.setOnClickListener(v -> {
            // ここは後で学校生活用の画面に進ませる
        });
    }
}