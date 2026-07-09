package com.example.javatea_client.resources;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.models.Question;
import com.example.javatea_client.models.University;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryResource {
    /**
    * 【全般】質問の取得
    */
    @GET("categories/general/questions")
    Call<Set<Question>> getGeneralQuestions(

    );

    /**
     * 【全般】質問の追加
     */
    @FormUrlEncoded
    @PUT("categories/general/questions/{qid}")
    Call<Void> addGeneralQuestion(
        @Path("qid") String qid
    );

    /**
     * 大学一覧の取得
     */
    @GET("categories/universities")
    Call<Collection<University>> getAllUnivId(
            @Query("from") String from,
            @Query("to") String to
    );

    /**
     * 大学の新規作成
     */
    @FormUrlEncoded
    @POST("categories/universities")
    Call<String> postNewUnivId(
            @Field("name") String name,
            @Field("kana") String kana
    );

    /**
     * 大学ID取得
     */
    @GET("categories/universities/{univ-id}")
    Call<HashMap<String, String>> getUnivInfo(
            @Path("univ-id") String univId
    );

    /**
     * 指定された大学IDの名前変更
     */
    @FormUrlEncoded
    @PUT("categories/universities/{univ-id}/name")
    Call<Void> updateUnivName(
            @Path("univ-id") String univId,
            @Field("name") String name
    );

    /**
     * 指定された大学IDのカナ変更
     */
    @FormUrlEncoded
    @PUT("categories/universities/{univ-id}/kana")
    Call<Void> updateUnivKana(
            @Path("univ-id") String univId,
            @Field("kana") String kana
    );

    /**
     * 大学全般に関する質問の取得
     */

    @GET("categories/universities/{univ-id}/general/questions")
    Call<Set<Question>> getUniversityGeneralQuestions(
            @Path("univ-id") String univId
    );

    /**
     * 大学全般に関する質問の追加
     */
    @PUT("categories/universities/{univ-id}/general/questions/{qid}")
    Call<Void> addUniversityGeneralQuestions(
            @Path("univ-id") String univId,
            @Path("qid") String qid
    );

    /**
     * 大学全般に属する科目一覧(ID)の取得
     */
    @GET("categories/universities/{univ-id}/lectures")
    Call<Collection<Lecture>> getUniversityLectures(
            @Path("univ-id") String univId
    );

    /**
     * 大学全般の質問の追加
     */
    @PUT("categories/universities/{univ-id}/lectures/{lecture-id}")
    Call<Void> putUnivLectures(
            @Path("univ-id") String univId,
            @Path("lecture-id") String lectId
    );

    /**
     * 大学特有の授業の質問を取得
     */
    @GET("categories/universities/{univ-id}/lectures/{lecture-id}/questions")
    Call<Set<Question>> getUniversityLectureQuestions(
            @Path("univ-id") String univId,
            @Path("lecture-id") String lectureId
    );

    /**
     * 大学特有の授業の質問を追加
     */
    @PUT("categories/universities/{univ-id}/lectures/{lecture-id}/questions/{qid}")
    Call<Void> addUniversityLectureQuestions(
            @Path("univ-id") String univId,
            @Path("lecture-id") String lectureId,
            @Path("qid") String qid
    );

    /**
     * 学部一覧を取得
     */
    @GET("categories/universities/{univ-id}/faculties")
    Call<List<String>> getFaculty(
            @Path("univ-id") String univId
    );

    /**
     * 学部の追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}")
    Call<Void> addFaculty(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName
    );

    /**
     * 科目一覧の取得
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/lectures")
    Call<Collection<Lecture>> getFacultyLectures(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName
    );

    /**
     * 科目の追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}/lectures/{lecture-id}")
    Call<Void> addLecture(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("lecture-id") String lectureId
    );

    /**
     * 学部科目の質問の取得
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/lectures/{lecture-id}/questions")
    Call<Void> getFacultyQuestions(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("lecture-id") String lectureId
    );

    /**
     * 学部科目の質問の追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}/lectures/{lecture-id}/questions/{qid}")
    Call<Void> addFacultyQuestion(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("lecture-id") String lectureId,
            @Path("qid") String qid
    );

    /**
     * 学科一覧取得
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/departments")
    Call<List<String>> getDepartments(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName
    );

    /**
     * 学科の追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}")
    Call<Void> addDepartment(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName
    );

    /**
     * 各学科特有の授業取得
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/lectures")
    Call<Collection<Lecture>> getDepartmentLectures(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName
    );

    /**
     * 学科特有の授業IDを追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/lectures/{lecture-id}")
    Call<Void> addLecture(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName,
            @Path("lecture-id") String lectureId
    );


    /**
     * 学科科目の質問の取得
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/lectures/{lecture-id}/questions")
    Call<Set<Question>> getDepartmentQuestions(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName,
            @Path("lecture-id") String lectureId
    );

    /**
     * 学科科目の質問の追加
     */
    @PUT("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/lectures/{lecture-id}/questions/{qid}")
    Call<Void> addDepartmentQuestions(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName,
            @Path("lecture-id") String lectureId,
            @Path("qid") String qid
    );

    /**
     * 検索に使うもの
     */
    @GET("categories/universities/{univ-id}/all-questions")
    Call<Set<Question>> getAllUniversityQuestions(
            @Path("univ-id") String univId
    );

    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/all-questions")
    Call<Set<Question>> getAllFacultyQuestions(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName
    );

    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/all-questions")
    Call<Set<Question>> getAllDepartmentQuestions(
            @Path("univ-id") String univId,
            @Path("faculty-name") String facultyName,
            @Path("department-name") String departmentName
    );

}
