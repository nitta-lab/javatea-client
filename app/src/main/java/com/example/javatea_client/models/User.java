package com.example.javatea_client.models;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String uid;
    private String name;
    private String pw;
    private String university;
    private String faculty;
    private String department;
    private int grade;
    private String token;
    private Set<Question> questions = new HashSet<Question>();
    private Set<Question> bestAnswers = new HashSet<Question>();;


    //uidの取得
    public String getUid() {
        return uid;
    }

    //uidの代入
    public void setUid(String uid) {
        this.uid = uid;
    }
    //ユーザ名の取得
    public String getName() {
        return this.name;
    }

    //ユーザ名の代入
    public void setName(String name) {
        this.name = name;
    }

    //パスワードの取得
    public String getPw() {
        return this.pw;
    }

    //パスワードの代入
    public void setPw(String pw) {
        this.pw = pw;
    }

    //所属大学名の取得
    public String getUniversity() {
        return this.university;
    }

    //所属大学名の代入
    public void setUniversity(String university) {
        this.university = university;
    }

    //所属学部の取得
    public String getFaculty() {
        return this.faculty;
    }

    //所属学部の代入
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    //所属学科の取得
    public String getDepartment() {
        return this.department;
    }

    //所属学科の代入
    public void setDepartment(String department) {
        this.department = department;
    }

    //ユーザの学年の取得
    public Integer getGrade() {
        return this.grade;
    }

    //ユーザの学年の代入
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    //UserRepository.javaで生成されたtokenを代入
    public void setToken(String token) {
        this.token = token;
    }

    //UserのTokenを返すメゾット(生成はUserRepository.java)
    public String getToken() {
        return this.token;
    }

    public Set<Question> getQuestions(){ return this.questions; }

    public void setQuestions(Set<Question> questions){ this.questions = questions; }

    public Set<Question> getBestAnswers(){ return this.bestAnswers; }

    public void setBestAnswers(Set<Question> bestAnswers){ this.bestAnswers = bestAnswers; }
}
