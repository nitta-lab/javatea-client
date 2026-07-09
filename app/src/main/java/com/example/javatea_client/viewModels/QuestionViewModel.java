package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.Question;
import com.example.javatea_client.resources.QuestionResource;

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
    public QuestionViewModel(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.questionResource = retrofit.create(QuestionResource.class);
    }
}