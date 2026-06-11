package com.example.javatea_client.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.resources.TimetableResource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TimetableViewModel extends ViewModel {

    private final Retrofit retrofit;
    private final TimetableResource timetableResource;
    // 年度一覧
    private final MutableLiveData<List<Integer>> years = new MutableLiveData<>();
    // 授業ID一覧
    private final MutableLiveData<List<String>> lectureIds = new MutableLiveData<>();
    // エラー
    private final MutableLiveData<String> error = new MutableLiveData<>();

    //コンストラクタ
    public TimetableViewModel() {
        this.retrofit = new Retrofit.Builder()
                //.baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea/")
                .baseUrl("http://160.247.112.119:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.timetableResource = retrofit.create(TimetableResource.class);
    }

    //年度一覧取得
    public LiveData<List<Integer>> getYears() {
        return years;
    }

    //授業ID一覧取得
    public LiveData<List<String>> getLectureIds() {
        return lectureIds;
    }

    //エラー取得
    public LiveData<String> getError() {
        return error;
    }

    //登録年度一覧取得
    public void loadTimetableYears(String uid, String token) {
        timetableResource.getYears(uid, token)
                .enqueue(new Callback<List<Integer>>() {
                    @Override
                    public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            years.setValue(response.body());
                        } else {
                            if (response.code() == 401) {
                                error.setValue("未認証です");
                            } else if (response.code() == 404) {
                                error.setValue("ユーザが存在しません");
                            } else if (response.code() == 500) {
                                error.setValue("サーバ内部エラー");
                            } else {
                                error.setValue("年度取得失敗: " + response.code());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Integer>> call, Throwable t) {
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }

    //指定年度の授業一覧取得
    public void loadTimetableLectures(String uid, int year, String token) {
        timetableResource
                .getLectures(uid, year, token)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            lectureIds.setValue(response.body());
                        } else {
                            if (response.code() == 401) {
                                error.setValue("未認証です");
                            }
                            else if (response.code() == 404) {
                                error.setValue("ユーザまたは年度が存在しません");
                            }
                            else if (response.code() == 500) {
                                error.setValue("サーバ内部エラー");
                            }
                            else {error.setValue("授業取得失敗: " + response.code());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }

    // 年度追加
    public void addYear(String uid, int year, String token) {
        timetableResource
                .addYear(uid, year, token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 正常終了
                        } else {
                            if (response.code() == 401) {
                                error.setValue("未認証です");
                            }
                            else if (response.code() == 404) {
                                error.setValue("ユーザまたは年度が存在しません");
                            }
                            else if (response.code() == 500) {
                                error.setValue("サーバ内部エラー");
                            }
                            else {
                                error.setValue("年度追加失敗: " + response.code());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }

    // 授業追加
    public void addLecture(String uid, int year, String lectureId, String token) {
        timetableResource
                .addLecture(uid, year, lectureId, token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 正常終了
                        } else {
                            if (response.code() == 401) {
                                error.setValue("未認証です");
                            }
                            else if (response.code() == 404) {
                                error.setValue("ユーザまたは年度が存在しません");
                            }
                            else if (response.code() == 500) {
                                error.setValue("サーバ内部エラー");
                            }
                            else {
                                error.setValue("授業追加失敗: " + response.code());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }

    // 授業削除
    public void removeLecture(String uid, int year, String lectureId, String token) {
        timetableResource
                .removeLecture(uid, year, lectureId, token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 正常終了
                        } else {
                            if (response.code() == 401) {
                                error.setValue("未認証です");
                            }
                            else if (response.code() == 404) {
                                error.setValue("ユーザまたは年度が存在しません");
                            }
                            else {
                                error.setValue("授業削除失敗: " + response.code());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        error.setValue("通信エラー: " + t.getMessage());
                    }
                });
    }
}