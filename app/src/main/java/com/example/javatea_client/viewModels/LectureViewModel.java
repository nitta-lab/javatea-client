package com.example.javatea_client.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tampopo_client.models.Lecture;
import com.example.tampopo_client.resources.LectureResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LectureViewModel extends ViewModel {

    private final LectureResource lectureResource;

    private final MutableLiveData<Lecture> lecture =
            new MutableLiveData<>();

    public LectureViewModel(LectureResource lectureResource) {
        this.lectureResource = lectureResource;
    }

    public LiveData<Lecture> getLectureLiveData() {
        return lecture;
    }

    public void loadLecture(String lectureId) {

        lectureResource.getLecture(lectureId)
                .enqueue(new Callback<Lecture>() {

                    @Override
                    public void onResponse(
                            Call<Lecture> call,
                            Response<Lecture> response) {

                        if(response.isSuccessful()
                                && response.body() != null) {

                            lecture.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Lecture> call,
                            Throwable t) {

                    }
                });
    }
}