package com.example.javatea_client.resources;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

}
