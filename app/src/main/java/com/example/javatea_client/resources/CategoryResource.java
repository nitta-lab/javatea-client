package com.example.javatea_client.resources;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryResource {
    /**
     * 大学一覧の取得
     */
    @GET("categories/universities")
    Call<List<String>> getAllUnivId();

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
    Call<HashMap<String, String>> addYear(
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
     * 大学全般に属する科目一覧(ID)の取得
     */
    @GET("categories/universities/{univ-id}/lectures")
    Call<List<String>> getUnivLectures(
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
    Call<List<String>> getLectures(
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
     * 各学科特有の授業追加
     */
    @GET("categories/universities/{univ-id}/faculties/{faculty-name}/departments/{department-name}/lectures")
    Call<List<String>> getLectures(
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
}
