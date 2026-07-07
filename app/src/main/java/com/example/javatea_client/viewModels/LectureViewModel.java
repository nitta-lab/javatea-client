package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.resources.CategoryResource;
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
//    private final MutableLiveData<String> lectureId =
//            new MutableLiveData<>();

    // lectureIdはLiveDataにする必要ないと思う
    private String lectureId;




    // エラーメッセージを保存する
    private final MutableLiveData<String> error =
            new MutableLiveData<>();



    // 追加の宣言

    // こっちでカテゴリーリソース呼んでもらう(ViewModelは呼べんけど、こっちは多分いける)
    private final CategoryResource categoryResource;

    // 本来ならVoidだが、閉じるタイミングを掴むためにBoolean使ってる
    private final MutableLiveData<Boolean> finishAddLecture = new MutableLiveData<>();

    // log(デバッグ)用のTAG宣言
    private static final String TAG = "LectureViewModel";




    // コンストラクタ
    public LectureViewModel() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        lectureResource = retrofit.create(LectureResource.class);


        // ここでカテゴリーリソースも作っておく
        categoryResource = retrofit.create(CategoryResource.class);
    }



    // Viewから監視するためのgetter


    public LiveData<Lecture> getLecture() {
        return lecture;
    }

//    public LiveData<String> getLectureId() {
//        return lectureId;
//    }




    public LiveData<String> getError() {
        return error;
    }




    // さっき宣言したやつをgetter
    public LiveData<Boolean> getFinishAddLecture() {
        return finishAddLecture;
    }


    // 保存ボタンが押されたときの通信を呼ぶメソッド
    // 授業を新規作成した後、カテゴリーに足すイメージ(必要な情報が増えるのでそこが追加されてます)
    public void startAddLecture(
            String name,
            int grade,
            String semester,
            int frame,
            String day,
            int period,


            // ここは新規作成後必要な情報
            String univId,
            String facultyName,
            String departmentName,


            // ここはActivityの専門で選んだやつを保存しておく(universityかfacultyかdepartmentになると思う、仮に数字やったらもしかしたらintとかに直さなあかんかも)
            String type

    ) {

        error.setValue(null);

        lectureResource.createLecture(name, grade, semester, frame, day, period
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                if (response.isSuccessful()
                        && response.body() != null) {


                    // バックエンドから返された授業IDを保存
//                    lectureId.setValue(response.body());
                    lectureId = response.body();
                    Log.d(TAG, "lectureId：" + lectureId);

                    // その保存した授業IDを使って特有の授業についての通信を呼ぶ
                    addCategoryLecture(univId, facultyName, departmentName, lectureId, type);


                } else {
                    error.setValue("授業作成失敗: " + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {



                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }



    // ここから何行かは多分新しく追加せなあかんやつやと思います、どれ特有にせよ授業追加成功したら合図を出すような感じです
    // ※俺のやつから取って来てるのもあるから@NonNullとかそのままです。うっとおしかったら消してください
    private void addCategoryLecture(String univId, String facultyName, String departmentName, String lectureId, String type) {

        // 大学特有の場合
        if("university".equals(type)) {
            categoryResource.putUnivLectures(univId, lectureId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "大学特有の科目追加成功");
                        // 大学特有の科目追加が成功したので合図出す
                        finishAddLecture.setValue(true);
                    } else {
                        Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    Log.e(TAG, "ネットワークエラーが発生しました", throwable);
                }
            });

            // 学部特有の場合
        } else if ("faculty".equals(type)) {
            categoryResource.addLecture(univId, facultyName, lectureId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "学部特有の授業追加成功");
                        // 学部特有の科目追加が成功したので合図出す
                        finishAddLecture.setValue(true);
                    } else {
                        Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    Log.e(TAG, "ネットワークエラーが発生しました", throwable);
                }
            });

            // 学科特有の場合
        } else if ("department".equals(type)) {
            categoryResource.addLecture(univId, facultyName, departmentName, lectureId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "学科特有の授業追加成功");
                        // 学科特有の科目追加が成功したので合図出す
                        finishAddLecture.setValue(true);
                    } else {
                        Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    Log.e(TAG, "ネットワークエラーが発生しました", throwable);
                }
            });
        }
    }

    // 授業IDから授業1件を取得する


    public void loadLecture(String Id) {

        error.setValue(null);

        lectureResource.getLecture(Id).enqueue(new Callback<Lecture>() {

            @Override
            public void onResponse(Call<Lecture> call, Response<Lecture> response) {



                if (response.isSuccessful() && response.body() != null) {

                    lecture.setValue(response.body());

                } else {
                    error.setValue("授業取得失敗: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Lecture> call, Throwable t) {


                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }




}
