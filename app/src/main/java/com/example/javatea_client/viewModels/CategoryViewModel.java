package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.models.University;
import com.example.javatea_client.resources.CategoryResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CategoryViewModel extends ViewModel {
    // サーバー呼び出す用のリソース
    private final CategoryResource categoryResource;

    // 画面に表示するためのオブジェクトを保持するためのLiveData
    private final MutableLiveData<Collection<University>> currentUniversities = new MutableLiveData<>();
    private final MutableLiveData<University> currentUniversity = new MutableLiveData<>();
    // 新規作成時に発行された大学IDを保持するためのLiveData
    private final MutableLiveData<String> createdUnivId = new MutableLiveData<>();
    // 画面に表示用のLiveData(lecture用)
    private final MutableLiveData<Collection<Lecture>> univLectures = new MutableLiveData<>();

    private final MutableLiveData<List<String>> currentFaculty = new MutableLiveData<>();
    private final MutableLiveData<String> createdFacultyName = new MutableLiveData<>();
    private final MutableLiveData<Collection<Lecture>> facLectures = new MutableLiveData<>();

    private final MutableLiveData<List<String>> currentDepartment = new MutableLiveData<>();
    private final MutableLiveData<String> createdDepartmentName = new MutableLiveData<>();
    private final MutableLiveData<Collection<Lecture>> departLectures = new MutableLiveData<>();

    // 最終的にViewに渡す授業リスト
    private final MutableLiveData<Collection<Lecture>> searchLectureResults = new MutableLiveData<>();
    // サーバーから届いたデータを一時的に保存するリスト, Mutableはいらない
    private Collection<Lecture> universityLectures = null;
    private Collection<Lecture> facultyLectures = null;
    private Collection<Lecture> departmentLectures = null;

    private String semester;
    private String day;
    private int period;

    // 自分の学年
    private int grade;


    // ここから質問関連

    // 学校生活に関する現在の質問
    private final MutableLiveData<Set<Question>> currentUniversityGeneralQuestions = new MutableLiveData<>();
    // 大学全般に関する現在の質問
    private final MutableLiveData<Set<Question>> currentUniversityQuestions = new MutableLiveData<>();
    // 学部全般に関する現在の質問
    private final MutableLiveData<Set<Question>> currentFacultyQuestions = new MutableLiveData<>();
    // 指定された学科に対する現在の質問
    private final MutableLiveData<Set<Question>> currentDepartmentQuestions = new MutableLiveData<>();

    // 検索でいるかもしれないので残しておく
//    private Set<Question> universityQuestions = null;
//    private Set<Question> facultyQuestions = null;
//    private Set<Question> departmentQuestions = null;




    // ログ用のタグ
    private static final String TAG = "CategoryViewModel";


    // デプロイしてないから今はlocalhostで IntelliJ IDEA とこっち、どちらも起動させる
    public CategoryViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.categoryResource = retrofit.create(CategoryResource.class);
    }

    // 外部には書き換え不可能なLiveDataとして公開する(だからMutableつかない？)
    public LiveData<Collection<University>> getCurrentUniversities() { return currentUniversities; }
    public LiveData<University> getCurrentUniversity() {
        return currentUniversity;
    }
    public LiveData<String> getCreatedUnivId() {
        return createdUnivId;
    }
    public LiveData<Collection<Lecture>> getUnivLectures() {
        return univLectures;
    }

    public LiveData<List<String>> getCurrentFaculty() {
        return currentFaculty;
    }
    public LiveData<String> getCreatedFacultyName() {
        return createdFacultyName;
    }
    public LiveData<Collection<Lecture>> getFacLectures() {
        return facLectures;
    }

    public LiveData<List<String>> getCurrentDepartment() {
        return currentDepartment;
    }
    public LiveData<String> getCreatedDepartmentName() {
        return createdDepartmentName;
    }
    public LiveData<Collection<Lecture>> getDepartLectures() {
        return departLectures;
    }

    // 授業に関してActivity側が呼び出すのはこれ
    public LiveData<Collection<Lecture>> getSearchLectureResults() {
        return searchLectureResults;
    }



    // ここから質問関連(LiveData)、これらを呼んでもらう
    public LiveData<Set<Question>> getUniversityGeneralQuestions() {
        return currentUniversityGeneralQuestions;
    }
    public LiveData<Set<Question>> getUniversityQuestions() {
        return currentUniversityQuestions;
    }
    public LiveData<Set<Question>> getFacultyQuestions() {
        return currentFacultyQuestions;
    }
    public LiveData<Set<Question>> getDepartmentQuestions() {
        return currentDepartmentQuestions;
    }


    // 大学特有の授業が届いた時に呼ばれるメソッド
    public void setUniversityLectures(Collection<Lecture> universityLectures) {
        if(universityLectures != null) {
            this.universityLectures = universityLectures;
        } else {
            this.universityLectures = new ArrayList<>();
        }
    }

    public void setFacultyLectures(Collection<Lecture> facultyLectures) {
        if(facultyLectures != null) {
            this.facultyLectures = facultyLectures;
        } else {
            this.facultyLectures = new ArrayList<>();
        }
    }

    public void setDepartmentLectures(Collection<Lecture> departmentLectures) {
        if(departmentLectures != null) {
            this.departmentLectures = departmentLectures;
        } else {
            this.departmentLectures = new ArrayList<>();
        }
    }


    // 大学特有、学部特有、学科特有の授業が揃っているか確認し、統合するメソッド
    private List<Lecture> checkLectureCombine() {
        List<Lecture> allCombinedLectures = new ArrayList<>();
        if(universityLectures != null) {
            allCombinedLectures.addAll(universityLectures);
        }
        if(facultyLectures != null) {
            allCombinedLectures.addAll(facultyLectures);
        }
        if(departmentLectures != null) {
            allCombinedLectures.addAll(departmentLectures);
        }
        return allCombinedLectures;
    }

    // 通信呼んでもらう用
    public void callSearchLectures(String univId, String facultyName, String departmentName, String semester, String day, int period, int grade) {
        this.semester = semester;
        this.day = day;
        this.period = period;
        this.grade = grade;

        loadDepartmentLectures(univId, facultyName, departmentName);
    }

    // 条件をViewからもらい、そのあと統合したリストから検索するメソッド
    public void searchLectures(String semester, String day, int period, int grade) {
        // 情報がそろってない場合はreturn返す
        if(universityLectures == null || facultyLectures == null || departmentLectures == null) {
            return;
        }

        List<Lecture> allLectures = checkLectureCombine();

        List<Lecture> filteredList = new ArrayList<>();
        for(Lecture lecture : allLectures) {
            boolean matchSemester = false;
            if(lecture.getSemester().equals(semester)){
                matchSemester = true;
            }else if((semester.equals("前期")||semester.equals("後期"))&&lecture.getSemester().equals("通年")){
                matchSemester = true;
            }
            boolean matchDay = lecture.getDay().equals(day);
            boolean matchPeriod = (lecture.getPeriod() == period);

            boolean matchGrade = false;
            int lectureGrade = lecture.getGrade();
            if(grade >= lectureGrade) {
                matchGrade = true;
            }

            if(matchSemester && matchDay && matchPeriod && matchGrade) {
                filteredList.add(lecture);
            }
        }
        searchLectureResults.setValue(filteredList);
    }


    public void getAllUnivId(String from, String to){
        categoryResource.getAllUnivId(from, to).enqueue(new Callback<Collection<University>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<University>> call, @NonNull Response<Collection<University>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentUniversities.setValue(response.body());
                    Log.d(TAG, "大学一覧を取得成功");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<University>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void postNewUnivId(String name, String kana) {
        categoryResource.postNewUnivId(name, kana).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // こっちは内部からなのでMutableの方にセットする
                    createdUnivId.setValue(response.body());
                    Log.d(TAG, "大学IDの発行成功：" + response.code());
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
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
                if (response.isSuccessful() && response.body() != null) {
                    HashMap<String, String> map = response.body();

                    University university = new University(
                            map.get("univ-id"),
                            map.get("name"),
                            map.get("kana")
                    );

                    currentUniversity.setValue(university);
                    Log.d(TAG, "指定された大学IDの情報取得成功");
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
                if (response.isSuccessful()) {
                    // ローカル側を変更
                    getUnivInfo(univId);
                    Log.d(TAG, "大学名の変更成功");
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

    public void updateUnivKana(String univId, String kana) {
        categoryResource.updateUnivKana(univId, kana).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // ローカル側を変更
                    getUnivInfo(univId);
                    Log.d(TAG, "大学名のカナの変更成功");
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

    // 大学全般の授業一覧を取得する, 同時に統合するためのリストにセットする
    public void loadUniversityLectures(String univId) {
        categoryResource.getUniversityLectures(univId).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    univLectures.setValue(response.body());
                    setUniversityLectures(response.body());
                    searchLectures(semester, day, period, grade);
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                setUniversityLectures(null);
                searchLectures(semester, day, period, grade);
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }


    public void putUnivLectures(String univId, String lectId) {
        categoryResource.putUnivLectures(univId, lectId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    loadUniversityLectures(univId);
                    Log.d(TAG, "大学特有の科目追加成功");
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

    public void getFaculty(String univId) {
        categoryResource.getFaculty(univId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    currentFaculty.setValue(response.body());
                    Log.d(TAG, "学部一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void addFaculty(String univId, String facultyName) {
        categoryResource.addFaculty(univId, facultyName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    getFaculty(univId);
                    Log.d(TAG, "学部追加成功");
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

    // 学部特有
    public void loadFacultyLectures(String univId, String facultyName) {
        categoryResource.getFacultyLectures(univId, facultyName).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if(response.isSuccessful() && response.body() != null) {
//                    facLectures.setValue(response.body());
                    setFacultyLectures(response.body());
                    loadUniversityLectures(univId);
                    Log.d(TAG, "学部特有の授業一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                setFacultyLectures(null);
                loadUniversityLectures(univId);
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 学部特有
    public void addLecture(String univId, String facultyName, String lectureId) {
        categoryResource.addLecture(univId, facultyName, lectureId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    loadFacultyLectures(univId, facultyName);
                    Log.d(TAG, "学部特有の授業追加成功");
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

    public void getDepartments(String univId, String facultyName) {
        categoryResource.getDepartments(univId, facultyName).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    currentDepartment.setValue(response.body());
                    Log.d(TAG, "学科の一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void addDepartment(String univId, String facultyName, String departmentName) {
        categoryResource.addDepartment(univId, facultyName, departmentName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    getDepartments(univId, facultyName);
                    Log.d(TAG, "学科の追加成功");
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

    // 学科特有
    public void loadDepartmentLectures(String univId, String facultyName, String departmentName) {
        categoryResource.getDepartmentLectures(univId, facultyName, departmentName).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if(response.isSuccessful() && response.body() != null){
//                    departLectures.setValue(response.body());
                    setDepartmentLectures(response.body());
                    loadFacultyLectures(univId, facultyName);
                    Log.d(TAG, "学科特有の授業一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                setDepartmentLectures(null);
                loadFacultyLectures(univId, facultyName);
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 学科特有
    public void addLecture(String univId, String facultyName, String departmentName, String lectureId) {
        categoryResource.addLecture(univId, facultyName, departmentName, lectureId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    loadDepartmentLectures(univId, facultyName, departmentName);
                    Log.d(TAG, "学科特有の授業追加成功");
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

    // Q＆A関連
    public void universityLectures(String univId) {
        categoryResource.getUniversityLectures(univId).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    univLectures.setValue(response.body());
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void facultyLectures(String univId, String facultyName) {
        categoryResource.getFacultyLectures(univId, facultyName).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    facLectures.setValue(response.body());
                    Log.d(TAG, "学部特有の授業一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    public void departmentLectures(String univId, String facultyName, String departmentName) {
        categoryResource.getDepartmentLectures(univId, facultyName, departmentName).enqueue(new Callback<Collection<Lecture>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<Lecture>> call, @NonNull Response<Collection<Lecture>> response) {
                if(response.isSuccessful() && response.body() != null){
                    departLectures.setValue(response.body());
                    Log.d(TAG, "学科特有の授業一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<Lecture>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 全般に関する質問



    // 学校生活に対する質問
    public void universityGeneralQuestions(String univId) {
        categoryResource.getUniversityGeneralQuestions(univId).enqueue(new Callback<Set<Question>>() {
            @Override
            public void onResponse(@NonNull Call<Set<Question>> call, @NonNull Response<Set<Question>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentUniversityGeneralQuestions.setValue(response.body());
                    Log.d(TAG, "学校生活に対する質問一覧取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Set<Question>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 大学全般の質問
    public void universityQuestions(String univId, String lectureId) {
        categoryResource.getUniversityLectureQuestions(univId, lectureId).enqueue(new Callback<Set<Question>>() {
            @Override
            public void onResponse(@NonNull Call<Set<Question>> call, @NonNull Response<Set<Question>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentUniversityQuestions.setValue(response.body());
                    Log.d(TAG, "大学全般の質問取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Set<Question>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 学部全般の質問
    public void facultyQuestions(String univId, String facultyName, String lectureId) {
        categoryResource.getFacultyQuestions(univId, facultyName, lectureId).enqueue(new Callback<Set<Question>>() {
            @Override
            public void onResponse(@NonNull Call<Set<Question>> call, @NonNull Response<Set<Question>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentFacultyQuestions.setValue(response.body());
                    Log.d(TAG, "学部全般の質問取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Set<Question>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    // 学科に対する質問
    public void departmentQuestions(String univId, String facultyName, String departmentName, String lectureId) {
        categoryResource.getDepartmentQuestions(univId, facultyName, departmentName, lectureId).enqueue(new Callback<Set<Question>>() {
            @Override
            public void onResponse(@NonNull Call<Set<Question>> call, @NonNull Response<Set<Question>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentDepartmentQuestions.setValue(response.body());
                    Log.d(TAG, "指定した学科に対する質問取得成功：" + response.body().size() + "件");
                } else {
                    Log.w(TAG, "サーバーエラーが発生しました　　コード：" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Set<Question>> call, @NonNull Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }


//    // ここから検索関連(メソッド)
//    // 大学特有の授業が届いた時に呼ばれるメソッド
//    public void setUniversityQuestions(Set<Question> universityQuestions) {
//        if(universityQuestions != null) {
//            this.universityQuestions = universityQuestions;
//        } else {
//            this.universityQuestions = new HashSet<>();
//        }
//    }
//
//    public void setFacultyQuestions(Set<Question> facultyQuestions) {
//        if(facultyQuestions != null) {
//            this.facultyQuestions = facultyQuestions;
//        } else {
//            this.facultyQuestions = new HashSet<>();
//        }
//    }
//
//    public void setDepartmentQuestions(Set<Question> departmentQuestions) {
//        if(departmentQuestions != null) {
//            this.departmentQuestions = departmentQuestions;
//        } else {
//            this.departmentQuestions = new HashSet<>();
//        }
//    }


}