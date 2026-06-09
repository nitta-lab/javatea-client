package com.example.javatea_client.resources;
import com.example.javatea_client.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserResource {

    //アカウント登録
    @FormUrlEncoded
    @PUT("/users/{uid}")
    Call<User> createUser(
            @Path("uid") String uid,
            @Field("name") String name,
            @Field("pw") String pw
    );

    //ログイン
    @FormUrlEncoded
    @POST("/users/{uid}/login")
    Call<String> login(
            @Path("uid") String uid,
            @Field("pw") String pw
    );

    //ユーザの大学名を取得
    @GET("/users/{uid}/university")
    Call<String> getUniversity(
            @Path("uid") String uid,
            @Query("token") String token
    );

    //ユーザの大学名を登録
    @FormUrlEncoded
    @PUT("/users/{uid}/university")
    Call<String> setUniversity(
            @Path("uid") String uid,
            @Field("university") String university,
            @Field("token") String token
    );

    //ユーザの学部を取得
    @GET("/users/{uid}/faculty")
    Call<String> getFaculty(
            @Path("uid") String uid,
            @Query("token") String token
    );

    //ユーザの学部を登録
    @FormUrlEncoded
    @PUT("/users/{uid}/faculty")
    Call<String> setFaculty(
            @Path("uid") String uid,
            @Field("faculty") String faculty,
            @Field("token") String token
    );

    //ユーザの学科を取得
    @GET("/users/{uid}/department")
    Call<String> getDepartment(
            @Path("uid") String uid,
            @Query("token") String token
    );

    //ユーザの学科を登録
    @FormUrlEncoded
    @PUT("/users/{uid}/department")
    Call<String> setDepartment(
            @Path("uid") String uid,
            @Field("department") String department,
            @Field("token") String token
    );

    //ユーザの学年を取得
    @GET("/users/{uid}/grade")
    Call<Integer> getGrade(
            @Path("uid") String uid,
            @Query("token") String token
    );

    //ユーザの学年を登録
    @FormUrlEncoded
    @PUT("/users/{uid}/grade")
    Call<Integer> setGrade(
            @Path("uid") String uid,
            @Field("grade") int grade,
            @Field("token") String token
    );

    //ユーザのニックネームを取得
    @GET("/users/{uid}/name")
    Call<String> getName(
            @Path("uid") String uid,
            @Query("token") String token
    );
}
