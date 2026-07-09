package com.example.javatea_client.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.javatea_client.resources.QuestionResource;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QuestionViewModel extends ViewModel {
    //サーバー呼び出す用のリソース
    private final QuestionResource questionResource;
    public QuestionViewModel(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea-server/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.questionResource = retrofit.create(QuestionResource.class);
    }
}