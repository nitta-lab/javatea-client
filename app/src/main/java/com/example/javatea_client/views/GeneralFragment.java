package com.example.javatea_client.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider; // 追加

import com.example.javatea_client.Javatea;
import com.example.javatea_client.R;
import com.example.javatea_client.models.University;
import com.example.javatea_client.models.User;
import com.example.javatea_client.viewModels.CategoryViewModel;
import com.example.javatea_client.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeneralFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;
    private Javatea javatea;
    private static final String TAG = "GeneralFragment";

    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LectureListActivity activity = (LectureListActivity) requireActivity();
        javatea = (Javatea) activity.getApplication();
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        categoryViewModel.getCurrentUniversities().observe(getViewLifecycleOwner(), new Observer<Collection<University>>() {
            @Override
            public void onChanged(Collection<University> currentUniversities) {
                if (currentUniversities == null) {
                    return;
                }
                Log.d(TAG, "全大学情報一覧を受信：" + currentUniversities.size() + "件");

                List<String> universityList = new ArrayList<>();
                for (University university : currentUniversities) {
                    Log.d(TAG, "大学名：" + university.getName());
                    universityList.add(university.getName() + "(" + university.getKana() + ")");
                }

                String[] universityArray = universityList.toArray(new String[0]);
                showUniversitySelectionDialog(universityArray, currentUniversities);
            }
        });

        /*
            各ボタンの紐付け
         */

        // 全般ボタン
        view.findViewById(R.id.btnGeneral).setOnClickListener(v -> {
            activity.addCategory("全般", "【全般】");
            activity.setLectureId("全般");
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new QuestionSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // 自分の所属大学のボタン
        Button btnMyUniversity = view.findViewById(R.id.btnMyUniversity);
        btnMyUniversity.setText(javatea.getUniversity());
        btnMyUniversity.setOnClickListener(v -> {
            activity.setUnivId(javatea.getUnivId());
            activity.addCategory(javatea.getUniversity(), "大学");
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UniversityFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // 大学カナ文字検索ボタン
        view.findViewById(R.id.btn_a).setOnClickListener(v -> fetchUniversityList("ア行"));
        view.findViewById(R.id.btn_ka).setOnClickListener(v -> fetchUniversityList("カ行"));
        view.findViewById(R.id.btn_sa).setOnClickListener(v -> fetchUniversityList("サ行"));
        view.findViewById(R.id.btn_ta).setOnClickListener(v -> fetchUniversityList("タ行"));
        view.findViewById(R.id.btn_na).setOnClickListener(v -> fetchUniversityList("ナ行"));
        view.findViewById(R.id.btn_ha).setOnClickListener(v -> fetchUniversityList("ハ行"));
        view.findViewById(R.id.btn_ma).setOnClickListener(v -> fetchUniversityList("マ行"));
        view.findViewById(R.id.btn_ya).setOnClickListener(v -> fetchUniversityList("ヤ行"));
        view.findViewById(R.id.btn_ra).setOnClickListener(v -> fetchUniversityList("ラ行"));
        view.findViewById(R.id.btn_wa).setOnClickListener(v -> fetchUniversityList("ワ行"));
    }

    private void fetchUniversityList(String kana) {
        if (categoryViewModel == null) {
            Log.e(TAG, "categoryViewModel が初期化されていません");
            return;
        }

        switch (kana) {
            case "ア行": categoryViewModel.getAllUnivId("ア", "カ"); break;
            case "カ行": categoryViewModel.getAllUnivId("カ", "サ"); break;
            case "サ行": categoryViewModel.getAllUnivId("サ", "タ"); break;
            case "タ行": categoryViewModel.getAllUnivId("タ", "ナ"); break;
            case "ナ行": categoryViewModel.getAllUnivId("ナ", "ハ"); break;
            case "ハ行": categoryViewModel.getAllUnivId("ハ", "マ"); break;
            case "マ行": categoryViewModel.getAllUnivId("マ", "ヤ"); break;
            case "ヤ行": categoryViewModel.getAllUnivId("ヤ", "ラ"); break;
            case "ラ行": categoryViewModel.getAllUnivId("ラ", "ワ"); break;
            case "ワ行": categoryViewModel.getAllUnivId("ワ", "ン"); break;
        }
        Log.d(TAG, kana + "の大学の取得を開始");
    }

    private void showUniversitySelectionDialog(String[] universityArray, Collection<University> currentUniversities){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("大学を選択してください");

        builder.setItems(universityArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedUniversityInfo = universityArray[which];
                int index = selectedUniversityInfo.lastIndexOf("(");
                String selectedUniversityName = selectedUniversityInfo.substring(0, index);
                String selectedUniversityKana = selectedUniversityInfo.substring(index + 1, selectedUniversityInfo.length() - 1);

                for (University university : currentUniversities) {
                    if (university.getName().equals(selectedUniversityName) && university.getKana().equals(selectedUniversityKana)) {
                        // LectureListActivityを取得し、操作
                        LectureListActivity activity = (LectureListActivity) requireActivity();
                        activity.setUnivId(university.getId());
                        activity.addCategory(university.getName(), "大学");
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new UniversityFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                    }
                }
            }
        });
        builder.show();
    }
}