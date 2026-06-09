package com.example.javatea_client.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.resources.CategoryResource;

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
    private final Retrofit retrofit;
    private final CategoryResource categoryResource;
    private final MutableLiveData<String> University;
    private final MutableLiveData<String> Faculty;
    private final MutableLiveData<String> Department;


    public CategoryViewModel() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.categoryResource = retrofit.create(CategoryResource.class);
        this.University = new MutableLiveData<>();
        this.Faculty = new MutableLiveData<>();
        this.Department = new MutableLiveData<>();
    }

    public Retrofit getRetrofit() { return retrofit; }

    public MutableLiveData<String> getFaculty() { return Faculty; }
    public MutableLiveData<String> getDepartment() { return Department; }

    public void postNewUnivId(String name, String kana){
        Call<String> call = categoryResource.postNewUnivId(name, kana);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    University.setValue(response.body());
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                System.out.println("NetWorkError" + throwable);
            }
        });
    }



}
