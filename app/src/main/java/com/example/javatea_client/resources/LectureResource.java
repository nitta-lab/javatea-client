package com.example.javatea_client.resources;

import com.example.javatea_client.models.Lecture;
import com.example.javatea_client.viewModels.LectureViewModel;

import retrofit2.Call;
import retrofit2.http.*;

public interface LectureResource {

    //授業作成
    @FormUrlEncoded
    @POST("lectures")
    Call<String> createLecture(
            @Field("name") String name,
            @Field("grade") int grade,
            @Field("semester") String semester,
            @Field("frame") int frame,
            @Field("day") String day,
            @Field("period") int period
    );

    //授業情報取得
    @GET("lectures/{lecture-id}")
    Call<Lecture> getLecture(
            @Path("lecture-id") String lectureId
    );

    //name
    //授業の名前取得
    @GET("lectures/{lecture-id}/name")
    Call<String> getLectureName(
            @Path("lecture-id") String lectureId
    );

    //授業名の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/name")
    Call<Void> putLectureName(
            @Path("lecture-id") String lectureId,
            @Field("name") String name
    );

    //grade
    //受講可能学年の取得
    @GET("lectures/{lecture-id}/grade")
    Call<Integer> getLectureGrade(
            @Path("lecture-id") String lectureId
    );

    //受講可能学年の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/grade")
    Call<Void> putLectureGrade(
            @Path("lecture-id") String lectureId,
            @Field("grade") int grade
    );

    //semester
    //学期区分の取得
    @GET("lectures/{lecture-id}/semester")
    Call<String> getLectureSemester(
            @Path("lecture-id") String lectureId
    );

    //学期区分の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/semester")
    Call<Void> putLectureSemester(
            @Path("lecture-id") String lectureId,
            @Field("semester") String semester
    );

    //frame
    //コマ数の取得
    @GET("lectures/{lecture-id}/frame")
    Call<Integer> getLectureFrame(
            @Path("lecture-id") String lectureId
    );

    //コマ数の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/frame")
    Call<Void> putLectureFrame(
            @Path("lecture-id") String lectureId,
            @Field("frame") int frame
    );

    //day
    //開講曜日の取得
    @GET("lectures/{lecture-id}/day")
    Call<String> getLectureDay(
            @Path("lecture-id") String lectureId
    );

    //開講曜日の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/day")
    Call<Void> putLectureDay(
            @Path("lecture-id") String lectureId,
            @Field("day") String day
    );

    //period
    //開講時間の取得
    @GET("lectures/{lecture-id}/period")
    Call<Integer> getLecturePeriod(
            @Path("lecture-id") String lectureId
    );

    //開講時間の変更
    @FormUrlEncoded
    @PUT("lectures/{lecture-id}/period")
    Call<Void> putLecturePeriod(
            @Path("lecture-id") String lectureId,
            @Field("period") int period
    );
}
