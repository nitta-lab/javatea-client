package com.example.javatea_client.resources;

import com.example.javatea_client.models.Answer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.*;
public interface AnswerResource {

    //answerを全取得
    @GET("/{qid}/answers")
    Call<HashMap<String, Answer>> getAnswers(
            @Path("qid") String qid,
            @Query("uid") String uid,
            @Query("token") String token
    );

    //answerを作成
    @POST("/{qid}/answers")
    Call<Answer> createAnswer(
            @Path("qid") String qid,
            @Field("uid") String uid,
            @Field("body") String body,
            @Field("token") String token,
            @Field("name") String name
    );

    //answerを一つ取得
    @GET("/{qid}/answers/{aid}")
    Call<Answer> getAnswer(
            @Path("qid") String qid,
            @Path("aid") String aid,
            @Query("uid") String uid,
            @Query("token") String token
    );
}
