package com.example.javatea_client.models;

public class Answer {
    private String aid;
    private String body;
    private String uid;

    //コンストラクタ
    public Answer(String aid, String body,String uid) {
        this.aid = aid;
        this.body = body;
        this.uid = uid;
    }

    public String getAid() {return aid;} //aidを返す(念のため)

    public String getBody() {return body;}

    public String getUid() {return uid;}
}

