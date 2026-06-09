package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.University;
import com.example.javatea_client.resources.CategoryResource;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*
    ＜大学の値か名前の変更あるもの＞
    name(大学の名前)の変更
    public void setName(String name){ this.name = name; }
    kana(大学の読み仮名)の変更
    public void setKana(String kana) { this.kana = kana; }
    学部を作成
    public Faculty createFaculty(String faculty_name) {
        //学部が重複していたら、その学部を返す。
        if (this.faculties.containsKey(faculty_name)){
            return null;
        }
        faculties.put(faculty_name, new Faculty(faculty_name));
        return faculties.get(faculty_name);
    }
    大学全般科目の追加
    public void addLecture(String lecture_id, Lecture lecture) { lectures.put(lecture_id, lecture); }

    ＜学部の値か名前の変更あるもの＞
    学科の作成、追加
    public Department createDepartment(String department_name){
        //学科が既に存在していた場合はnullを返す
        if (departments.containsKey(department_name)){
            return null;
        }
        this.departments.put(department_name, new Department(department_name));
        return departments.get(department_name);
    }
    学部全般科目の追加
    public void addLecture(String lectureId, Lecture lecture) { lecturesInFaculty.put(lectureId, lecture); }

    ＜学科の値か名前の変更あるもの＞
    学科特有の科目の追加
    public void addLecture(String lectureId, Lecture lecture) { lecturesInDepartment.put(lectureId, lecture); }
 */

public class CategoryViewModel extends ViewModel {
    private final CategoryResource categoryResource;

    // 画面に表示するための大学データ(オブジェクト)を保持するためのLiveData
    private final MutableLiveData<University> currentUniversity = new MutableLiveData<>();
    // 新規作成時に発行された大学IDを保持するためのLiveData
    private final MutableLiveData<String> createdUnivId = new MutableLiveData<>();


    private final MutableLiveData<String> university = new MutableLiveData<>();
    private final MutableLiveData<String> faculty = new MutableLiveData<>();
    private final MutableLiveData<String> department = new MutableLiveData<>();

    // ログ用のタグ
    private static final String TAG = "CategoryViewModel";


    // デプロイしてないから今はlocalhostで IntelliJ IDEA とこっち、どちらも起動させる
    public CategoryViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("localhost8080:/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.categoryResource = retrofit.create(CategoryResource.class);
    }

    // 外部には書き換え不可能なLiveDataとして公開する(だからMutableつかない？)
    public LiveData<University> getCurrentUniversity() { return currentUniversity; }
    public LiveData<String> getCreatedUnivId() { return createdUnivId; }


    public LiveData<String> getFaculty() { return faculty; }
    public LiveData<String> getDepartment() { return department; }

    public void postNewUnivId(String name, String kana){
        categoryResource.postNewUnivId(name, kana).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    // こっちは内部からなのでMutableの方にセットする
                    createdUnivId.setValue(response.body());
                    Log.d(TAG, "通信成功：" + response.code());
                } else {
                    String errorCode = "サーバーエラーが発生しました　　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void getUnivInfo(String univId) {
        categoryResource.getUnivInfo(univId).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<HashMap<String, String>> call, @NonNull Response<HashMap<String, String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    HashMap<String, String> map = response.body();

                    University university = new University(
                            map.get("univ-id"),
                            map.get("name"),
                            map.get("kana")
                    );

                    currentUniversity.setValue(university);
                    Log.d(TAG, "通信成功：" + response.code());
                } else {
                    String errorCode = "サーバーエラーが発生しました　　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HashMap<String, String>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void updateUnivName(String univId, String name) {
        categoryResource.updateUnivName(univId, name).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "大学名の変更成功");
                    // ローカル側を変更
                    getUnivInfo(univId);
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
