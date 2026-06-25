package com.example.javatea_client.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.CategoryViewModel;

public class GeneralFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private static final String TAG = "GeneralFragment"; // ※Flagmentになっていたのを直しました！

    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // レイアウトをインフレート（生成）
        return inflater.inflate(R.layout.fragment_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModelの初期化処理がここかonCreateにある想定です
        // categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // 各ボタンをXMLから紐付け、クリック時の処理を設定
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

    // 大学一覧取得（元の showKanaSelectionDialog から名前を分かりやすく変更しました）
    private void fetchUniversityList(String kana) {
        if (categoryViewModel == null) {
            Log.e(TAG, "categoryViewModel が初期化されていません");
            return;
        }

        switch (kana) {
            case "ア行":
                categoryViewModel.getAllUnivId("ア", "カ");
                Log.d(TAG, "ア行の大学の取得を開始");
                break;
            case "カ行":
                categoryViewModel.getAllUnivId("カ", "サ");
                Log.d(TAG, "カ行の大学の取得を開始");
                break;
            case "サ行":
                categoryViewModel.getAllUnivId("サ", "タ");
                Log.d(TAG, "サ行の大学の取得を開始");
                break;
            case "タ行":
                categoryViewModel.getAllUnivId("タ", "ナ");
                Log.d(TAG, "タ行の大学の取得を開始");
                break;
            case "ナ行":
                categoryViewModel.getAllUnivId("ナ", "ハ");
                Log.d(TAG, "ナ行の大学の取得を開始");
                break;
            case "ハ行":
                categoryViewModel.getAllUnivId("ハ", "マ");
                Log.d(TAG, "ハ行の大学の取得を開始");
                break;
            case "マ行":
                categoryViewModel.getAllUnivId("マ", "ヤ");
                Log.d(TAG, "マ行の大学の取得を開始");
                break;
            case "ヤ行":
                categoryViewModel.getAllUnivId("ヤ", "ラ");
                Log.d(TAG, "ヤ行の大学の取得を開始");
                break;
            case "ラ行":
                categoryViewModel.getAllUnivId("ラ", "ワ");
                Log.d(TAG, "ラ行大学の取得を開始");
                break;
            case "ワ行":
                categoryViewModel.getAllUnivId("ワ", "ン");
                Log.d(TAG, "ワ行の大学の取得を開始");
                break;
        }
    }
}