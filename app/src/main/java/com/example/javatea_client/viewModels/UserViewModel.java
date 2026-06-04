package com.example.javatea_client.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.User;
import com.example.javatea_client.resources.UserResource;

import retrofit2.Callback;
import retrofit2.Retrofit;

public class UserViewModel extends ViewModel {

    private Retrofit retrofit;
    private static UserResource userResource;
    private static final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> token = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private static final MutableLiveData<String> error = new MutableLiveData<>();

    public UserViewModel(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://nitta-lab-www.is.konan-u.ac.jp/javatea/")
                .build();
                this.userResource = retrofit.create(UserResource.class);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getToken() {
        return token;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public static LiveData<String> getError() {
        return error;
    }

}
