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

public class FacultySelectFragment extends Fragment {

    private Button btnUniversityGeneral;
    private Button btnLetters;
    private Button btnScience;
    private Button btnIntelligence;

    public FacultySelectFragment() {
        // 必須
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faculty_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnUniversityGeneral = view.findViewById(R.id.btnUniversityGeneral);
        btnLetters = view.findViewById(R.id.btnLetters);
        btnScience = view.findViewById(R.id.btnScience);
        btnIntelligence = view.findViewById(R.id.btnIntelligence);

        btnIntelligence.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DepartmentSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}