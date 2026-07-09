package com.example.javatea_client.resources;

import com.example.javatea_client.models.Question;

import java.util.*;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuestionResource {
    //質問を新しく作成する
    @FormUrlEncoded
    @POST("/questions")
    Call<String> createQuestion(
            @Field("title") String title,
            @Field("body") String body,
            @Field("uid") String uid,
            @Field("tags") List<String> tags,
            @Field("view-permission") String viewPermission,
            @Field("res-permission") String resPermission
    );

    //質問情報の取得
    @GET("/questions/{qid}")
    Call<Question> getQuestion(
            @Path("qid") String qid,
            @Query("uid") String requesterUid,
            @Query("token") String token
    );

    //質問タイトルを取得する
    @GET("/questions/{qid}/title")
    Call<String> getTitle(
            @Path("qid") String qid,
            @Query("uid") String requesterUid,
            @Query("token") String token
    );

    //ユーザーIDを取得する
    @GET("/questions/{qid}/uid")
    Call<String> getUid(
            @Path("qid") String qid,
            @Query("uid") String requesterUid,
            @Query("token") String token
    );
}