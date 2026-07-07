package com.example.javatea_client;

import android.app.Application;
import android.content.SharedPreferences;

public class Javatea extends Application {
    private String token;
    private String userId;
    private String password;
    private String view;
    private String univId;
    private String university;
    private String faculty;
    private String department;
    private String grade;

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

    public String getView() {return view;}

    public void setView(String view) {
        this.view = view;
        saveUserData();
    }

    public String getUnivId() {return univId;}

    public void setUnivId(String univId) {
        this.univId = univId;
        saveUserData();
    }

    public String getUniversity() {return university;}

    public void setUniversity(String university) {
        this.university = university;
        saveUserData();
    }

    public String getFaculty() {return faculty;}

    public void setFaculty(String faculty) {
        this.faculty = faculty;
        saveUserData();
    }

    public String getDepartment() {return department;}

    public void setDepartment(String department) {
        this.department = department;
        saveUserData();
    }

    public String getGrade() {return grade;}

    public void setGrade(String grade) {
        this.grade = grade;
        saveUserData();
    }

    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.putString("userId", userId);
        editor.putString("password", password);
        editor.putString("view", view);
        editor.putString("univId", univId);
        editor.putString("university", university);
        editor.putString("faculty", faculty);
        editor.putString("department", department);
        editor.putString("grade", grade);
        editor.apply();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        token = prefs.getString("token", "");
        userId = prefs.getString("userId", "");
        password = prefs.getString("password", "");
        view = prefs.getString("view", "");
        univId = prefs.getString("univId", "");
        university = prefs.getString("university", "");
        faculty = prefs.getString("faculty", "");
        department = prefs.getString("department", "");
        grade = prefs.getString("grade", "");
    }
}
