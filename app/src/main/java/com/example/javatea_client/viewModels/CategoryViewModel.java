package com.example.javatea_client.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Department;
import com.example.javatea_client.models.Faculty;
import com.example.javatea_client.models.University;
import com.example.javatea_client.resources.CategoryResource;

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
    private final MutableLiveData<University> University = new MutableLiveData<>();
    private final MutableLiveData<Faculty> Faculty = new MutableLiveData<>();
    private final MutableLiveData<Department> Department = new MutableLiveData<>();


    public CategoryViewModel(Retrofit retrofit) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        CategoryResource categoryResource = retrofit.create(CategoryResource.class);
    }

    public Retrofit getRetrofit() { return retrofit; }

    public MutableLiveData<University> getUniversity() { return University; }
    public MutableLiveData<Faculty> getFaculty() { return Faculty; }
    public MutableLiveData<Department> getDepartment() { return Department; }


}
