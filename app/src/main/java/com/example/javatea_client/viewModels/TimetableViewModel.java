package com.example.javatea_client.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TimetableViewModel extends ViewModel {

    // 年度一覧を保持
    private final MutableLiveData<List<Integer>> yearsLiveData =
            new MutableLiveData<>();

    // 授業ID一覧を保持
    private final MutableLiveData<List<String>> lectureIdsLiveData =
            new MutableLiveData<>();

    //年度一覧を取得
    public LiveData<List<Integer>> getYearsLiveData() {
        return yearsLiveData;
    }

    //授業ID一覧を取得
    public LiveData<List<String>> getLectureIdsLiveData() {
        return lectureIdsLiveData;
    }

    //年度一覧を設定
    public void setYears(List<Integer> years) {
        yearsLiveData.setValue(years);
    }

    //授業ID一覧を設定
    public void setLectureIds(List<String> lectureIds) {
        lectureIdsLiveData.setValue(lectureIds);
    }
}