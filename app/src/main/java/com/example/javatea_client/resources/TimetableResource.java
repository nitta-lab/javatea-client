package com.example.javatea_client.resources;

import com.example.javatea_client.models.Lecture;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TimetableResource {

    //ユーザが時間割登録している年度一覧を取得
    @GET("users/{uid}/timetable")
    Call<TreeMap<Integer, HashSet<Lecture>>> getTimetable(
            @Path("uid") String uid,
            @Query("token") String token
    );

    //指定年度に登録されている授業一覧を取得
    @GET("users/{uid}/timetable/{year}")
    Call<List<Lecture>> getLectures(
            @Path("uid") String uid,
            @Path("year") int year,
            @Query("token") String token
    );

    //時間割の年度を追加
    @FormUrlEncoded
    @PUT("users/{uid}/timetable/{year}")
    Call<Void> addYear(
            @Path("uid") String uid,
            @Path("year") int year,
            @Field("token") String token
    );

    //時間割に授業を追加
    @FormUrlEncoded
    @PUT("users/{uid}/timetable/{year}/{lecture-id}")
    Call<Void> addLecture(
            @Path("uid") String uid,
            @Path("year") int year,
            @Path("lecture-id") String lectureId,
            @Field("token") String token
    );

    //時間割から授業を削除
    @DELETE("users/{uid}/timetable/{year}/{lecture-id}")
    Call<Void> removeLecture(
            @Path("uid") String uid,
            @Path("year") int year,
            @Path("lecture-id") String lectureId,
            @Query("token") String token
    );
}