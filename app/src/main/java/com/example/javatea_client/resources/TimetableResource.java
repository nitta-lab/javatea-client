package com.example.javatea_client.resources;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TimetableResource {

    /**
     * ユーザが登録している年度一覧を取得
     */
    @GET("users/{uid}/timetable")
    Call<List<Integer>> getTimetableYears(
            @Path("uid") String uid,
            @Query("token") String token
    );

    /**
     * 指定年度の授業一覧を取得
     */
    @GET("users/{uid}/timetable/{year}")
    Call<List<String>> getTimetableLectures(
            @Path("uid") String uid,
            @Path("year") int year,
            @Query("token") String token
    );

    /**
     * 年度を追加
     */
    @PUT("users/{uid}/timetable/{year}")
    Call<Void> addYear(
            @Path("uid") String uid,
            @Path("year") int year,
            @Query("token") String token
    );

    /**
     * 授業を追加
     */
    @PUT("users/{uid}/timetable/{year}/{lecture-id}")
    Call<Void> addLecture(
            @Path("uid") String uid,
            @Path("year") int year,
            @Path("lecture-id") String lectureId,
            @Query("token") String token
    );

    /**
     * 授業を削除
     */
    @DELETE("users/{uid}/timetable/{year}/{lecture-id}")
    Call<Void> deleteLecture(
            @Path("uid") String uid,
            @Path("year") int year,
            @Path("lecture-id") String lectureId,
            @Query("token") String token
    );
}