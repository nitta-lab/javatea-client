package com.example.javatea_client;

import android.app.Application;
import android.content.SharedPreferences;

public class Javatea extends Application {
    private String token;
    private String userId;
    private String password;

    @Override
    public void onCreate() {
        super.onCreate();
        loadUserData();
    }

    public String getToken() {return token;}

    public void setToken(String token) {
        this.token = token;
        saveUserData();
    }

    public String getUserId() {return userId;}

    public void setUserId(String userId) {
        this.userId = userId;
        saveUserData();
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
        saveUserData();
    }

    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.putString("userId", userId);
        editor.putString("password", password);
        editor.apply();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        token = prefs.getString("token", "");
        userId = prefs.getString("userId", "");
        password = prefs.getString("password", "");
    }
}
