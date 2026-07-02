package com.example.javatea_client.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.resources.LectureResource;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LectureViewModel extends ViewModel {

    // Lectureに関するAPIを呼び出すためのResource
    private final LectureResource lectureResource;

    // 取得した授業1件を保存する
    private final MutableLiveData<Lecture> lecture =
            new MutableLiveData<>();

    // 新規作成した授業IDを保存する
    private final MutableLiveData<String> lectureId =
            new MutableLiveData<>();


    // 通信中かどうかを保存する
    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    // エラーメッセージを保存する
    private final MutableLiveData<String> error =
            new MutableLiveData<>();


    // コンストラクタ
    public LectureViewModel() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        lectureResource = retrofit.create(LectureResource.class);
    }



    // Viewから監視するためのgetter


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



    // 授業を新規作成する


    public void createLecture(
            String name,
            int grade,
            String semester,
            int frame,
            String day,
            int period
    ) {

        loading.setValue(true);
        error.setValue(null);

        lectureResource.createLecture(name, grade, semester, frame, day, period
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                loading.setValue(false);

                if (response.isSuccessful()
                        && response.body() != null) {

                    // バックエンドから返された授業IDを保存
                    lectureId.setValue(response.body());

                } else {
                    error.setValue("授業作成失敗: " + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                loading.setValue(false);

                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }



    // 授業IDから授業1件を取得する


    public void loadLecture(String Id) {

        loading.setValue(true);
        error.setValue(null);

        lectureResource.getLecture(Id).enqueue(new Callback<Lecture>() {

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