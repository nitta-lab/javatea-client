package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Question;
import com.example.javatea_client.resources.QuestionResource;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//MutableLiveDataは書き換え可能なデータ。LiveDataは書き換え可能でも書き換え不可能でもよい。基本MutableLiveDataでよい。
//immutableLiveData?は書き換え不可。
public class QuestionViewModel extends ViewModel {
    private Retrofit retrofit;
    private final QuestionResource questionResource; //サーバー呼び出す用のリソース
    private static final String TAG = "QuestionViewModel";
    private String questionId;
    private final MutableLiveData<Question> question = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> uid = new MutableLiveData<>();
    public QuestionViewModel(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.questionResource = retrofit.create(QuestionResource.class);
    }

    public LiveData<Question> getCurrentQuestion(){ return question; }

    //質問の作成
    public void createQuestion(String title, String body, String uid, List<String> tags, String viewPermission, String resPermission, String lectureId, String token){
        questionResource.createQuestion(title, body, uid, tags, viewPermission, resPermission, lectureId, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null){
                    questionId = response.body();
                    Log.d(TAG, "質問の作成成功　questionId：" + questionId);
                    getQuestion(questionId, uid, token);
                } else {
                    String errorCode = "サーバーエラーが発生しました　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    //質問の取得
    public void getQuestion(String qid, String uid, String token){
        questionResource.getQuestion(qid, uid, token).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null){
                    question.setValue(response.body());
                    Log.d(TAG, "質問取得成功");
                } else {
                    String errorCode = "サーバーエラーが発生しました　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    //質問タイトルを取得
    public void getTitle(String qid, String requesterUid, String token){
        questionResource.getTitle(qid, requesterUid, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null){
                    title.setValue(response.body());
                    Log.d(TAG, "質問タイトル取得成功");
                } else {
                    String errorCode = "サーバーエラーが発生しました　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }

    //ユーザーIDを取得
    public void getUid(String qid, String requesterUid, String token){
        questionResource.getUid(qid,requesterUid,token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null){
                    uid.setValue(response.body());
                    Log.d(TAG, "ユーザーID取得成功");
                } else {
                    String errorCode = "サーバーエラーが発生しました　コード：" + response.code();
                    Log.w(TAG, errorCode);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e(TAG, "ネットワークエラーが発生しました", throwable);
            }
        });
    }
}