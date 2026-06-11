package com.example.javatea_client.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javatea_client.models.User;
import com.example.javatea_client.resources.UserResource;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
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

    //ユーザー作成
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

    //ログイン
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

    //ユーザーの大学名を取得
    public String getUniversity(String uid, String token) {
        Call<String> call = userResource.getUniversity(uid, token);
        try {
            Response<String> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return response.body();
            } else {
                System.out.println(response.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ユーザーの大学名を登録
    public void setUniversity(String uid, String university, String token) {
        loading.setValue(true);
        userResource.setUniversity(uid, university, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    // LiveData に更新
                    User currentUser = user.getValue();
                    if (currentUser != null) {
                        currentUser.setUniversity(response.body());
                        user.setValue(currentUser);
                    }
                } else {
                    error.setValue("大学名の設定に失敗: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading.setValue(false);
                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }

    //ユーザの学部を取得
    public String getFaculty(String uid, String token) {
        Call<String> call = userResource.getFaculty(uid, token);
        try {
            Response<String> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return response.body();
            } else {
                System.out.println(response.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ユーザの学部を登録
    public void setFaculty(String uid, String faculty, String token) {
        loading.setValue(true);
        userResource.setFaculty(uid, faculty, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    // LiveData に更新
                    User currentUser = user.getValue();
                    if (currentUser != null) {
                        currentUser.setFaculty(response.body());
                        user.setValue(currentUser);
                    }
                } else {
                    error.setValue("学部の設定に失敗: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading.setValue(false);
                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }

    //ユーザの学科を取得
    public String getDepartment(String uid, String token) {
        Call<String> call = userResource.getDepartment(uid, token);
        try {
            Response<String> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return response.body();
            } else {
                System.out.println(response.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ユーザの学科を登録
    public void setDepartment(String uid, String department, String token) {
        loading.setValue(true);
        userResource.setDepartment(uid, department, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    // LiveData に更新
                    User currentUser = user.getValue();
                    if (currentUser != null) {
                        currentUser.setDepartment(response.body());
                        user.setValue(currentUser);
                    }
                } else {
                    error.setValue("学部の設定に失敗: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading.setValue(false);
                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }

    //ユーザの学年を取得
    public Integer getGrade(String uid, String token) {
        Call<Integer> call = userResource.getGrade(uid, token);
        try {
            Response<Integer> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return response.body();
            } else {
                System.out.println(response.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ユーザの学年を登録
    public void setGrade(String uid, int grade, String token) {
        loading.setValue(true);
        userResource.setGrade(uid, grade, token).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    // LiveData に更新
                    User currentUser = user.getValue();
                    if (currentUser != null) {
                        currentUser.setGrade(response.body());
                        user.setValue(currentUser);
                    }
                } else {
                    error.setValue("学部の設定に失敗: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                loading.setValue(false);
                error.setValue("通信エラー: " + t.getMessage());
            }
        });
    }

    //ユーザのニックネームを取得
    public String getName(String uid, String token) {
        Call<String> call = userResource.getName(uid, token);
        try {
            Response<String> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return response.body();
            } else {
                System.out.println(response.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ユーザーの存在確認
    public boolean checkUser(String uid) {
        Call<User> call = userResource.getUser(uid);
        try {
            Response<User> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println(response.code());
                return true;
            } else {
                System.out.println(response.code());
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
