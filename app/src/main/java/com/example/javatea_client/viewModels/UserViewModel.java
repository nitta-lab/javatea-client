package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.User;
import com.example.javatea_client.resources.UserResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
                .baseUrl("http://localhost:8080/")
                //.addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(JacksonConverterFactory.create())
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

    public void createUser(String id, String name ,String password) {
        loading.setValue(true);
        userResource.createUser(id, name, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> c, Response<User> res) {
                loading.setValue(false);
                if (res.isSuccessful() && res.body() != null) {
                    User u = res.body();
                    Log.d("API_RESPONSE", u.getUid());
                    user.setValue(res.body());
                } else {
                    if (res.code() == 409) {
                        error.setValue("このユーザーIDは既に使用されています");
                        user.setValue(null);
                    } else {
                        Log.e("API_RESPONSE", "Response error code: " + res.code());
                        error.setValue("登録失敗: " + res.code());
                        user.setValue(null);
                    }
                }
            }
            @Override
            public void onFailure(Call<User> c, Throwable t) {
                loading.setValue(false);
                error.setValue("エラー: " + t.getMessage());
            }
        });
    }

    public void login(String uid, String pw) {
        loading.setValue(true);
        userResource.login(uid, pw).enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> c,Response<String> res) {
                if (res.isSuccessful()) {
                    token.setValue(res.body());
                } else {
                    error.setValue("ログイン失敗: " + res.code());
                }
            }

            @Override
            public void onFailure(Call<String> c, Throwable t) {
                loading.setValue(false);
                error.setValue("エラー: " + t.getMessage());
            }
        });
    }


}
