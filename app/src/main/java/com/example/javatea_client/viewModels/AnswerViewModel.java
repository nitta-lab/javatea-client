package com.example.javatea_client.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Answer;
import com.example.javatea_client.resources.AnswerResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.*;

public class AnswerViewModel extends ViewModel {

    private final Retrofit retrofit;
    private final AnswerResource answerResource;

    private final MutableLiveData<HashMap<String, Answer>> answers = new MutableLiveData<>();

    private final MutableLiveData<Answer> answer = new MutableLiveData<>();

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);

    public AnswerViewModel(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.answerResource = retrofit.create(AnswerResource.class);
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<Boolean> getCreateSuccess() {
        return createSuccess;
    }

    public void resetCreateSuccess() {
        createSuccess.setValue(false);
    }

    public LiveData<HashMap<String,Answer>> getAnswers(){
        return answers;
    }

    public LiveData<Answer> getAnswer(){
        return answer;
    }

    public LiveData<String> getError(){return error;}

    public void loadAnswers(String qid,String uid,String token){
        loading.setValue(true);
        answerResource.getAnswers(qid,uid,token)
                .enqueue(new Callback<HashMap<String, Answer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Answer>> call, Response<HashMap<String, Answer>> response) {
                        loading.setValue(false);
                        if(response.isSuccessful()&&response.body() != null){
                            answers.setValue(response.body());
                        }else{
                            if(response.code() == 404){
                                error.setValue("ユーザが存在しません");
                            }else if(response.code() == 401){
                                error.setValue("認証エラー");
                            }else{
                                error.setValue("answerの取得に失敗 " + response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Answer>> call, Throwable throwable) {
                        loading.setValue(false);
                        error.setValue("エラー: " + throwable.getMessage());
                    }
                });
    }

    public void createAnswer(String qid,String uid,String body,String token,String name){
        loading.setValue(true);
        createSuccess.setValue(false);
        answerResource.createAnswer(qid,uid,body,token,name)
                .enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
                        loading.setValue(false);
                        if(response.isSuccessful()&&response.body() != null){
                            answer.setValue(response.body());
                            createSuccess.setValue(true);
                        }else{
                            createSuccess.setValue(false);
                            if(response.code() == 404){
                                error.setValue("ユーザが存在しません");
                            }else if(response.code() == 401){
                                error.setValue("認証エラー");
                            }else{
                                error.setValue("answerの作成に失敗 " + response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable throwable) {
                        loading.setValue(false);
                        createSuccess.setValue(false);
                        error.setValue("エラー: " + throwable.getMessage());
                    }
                });
    }

    public void loadAnswer(String qid,String aid,String uid,String token){
        loading.setValue(true);
        answerResource.getAnswer(qid,aid,uid,token)
                .enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
                        loading.setValue(false);
                        if(response.isSuccessful()&&response.body() != null){
                            answer.setValue(response.body());
                        }else{
                            if(response.code() == 404){
                                error.setValue("ユーザが存在しません");
                            }else if(response.code() == 401){
                                error.setValue("認証エラー");
                            }else{
                                error.setValue("answerの作成に失敗 " + response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable throwable) {
                        loading.setValue(false);
                        error.setValue("エラー: " + throwable.getMessage());
                    }
                });
    }
}
