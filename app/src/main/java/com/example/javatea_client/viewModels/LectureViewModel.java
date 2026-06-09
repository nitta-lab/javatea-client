package com.example.javatea_client.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.resources.LectureResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LectureViewModel extends ViewModel {

    private final Retrofit retrofit;
    private final LectureResource lectureResource;

    private final MutableLiveData<Lecture> lecture = new MutableLiveData<>();
    private final MutableLiveData<String> lectureId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LectureViewModel() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.lectureResource = retrofit.create(LectureResource.class);
    }

    public LiveData<Lecture> getLecture() {
        return lecture;
    }

    public LiveData<String> getLectureId() {
        return lectureId;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void createLecture(String name, int grade, String semester,
                              int frame, String day, int period) {
        loading.setValue(true);

        lectureResource.createLecture(name, grade, semester, frame, day, period)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        loading.setValue(false);

                        if (response.isSuccessful()) {
                            lectureId.setValue(response.body());
                        } else {
                            error.setValue("授業作成失敗: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        loading.setValue(false);
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }

    public void loadLecture(String id) {
        loading.setValue(true);

        lectureResource.getLecture(id)
                .enqueue(new Callback<Lecture>() {
                    @Override
                    public void onResponse(Call<Lecture> call, Response<Lecture> response) {
                        loading.setValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            lecture.setValue(response.body());
                        } else {
                            error.setValue("授業取得失敗: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Lecture> call, Throwable t) {
                        loading.setValue(false);
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }
}