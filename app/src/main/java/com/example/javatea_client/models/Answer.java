package com.example.javatea_client.models;

public class Answer {
    private String aid;
    private String body;
    private String uid;
    private String name;

    // JSONからAnswerを生成するための引数なしコンストラクタ
    public Answer() {
    }

    // 既存のコンストラクタ
    public Answer(String aid, String body, String uid, String name) {
        this.aid = aid;
        this.body = body;
        this.uid = uid;
        this.name = name;
    }

    // getter
    public String getAid() {
        return aid;
    }

    public String getBody() {
        return body;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    // setter
    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }
}