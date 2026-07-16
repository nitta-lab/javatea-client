package com.example.javatea_client.models;

public class Answer {
    private String aid;
    private String body;
    private String uid;
    private String name;

    //コンストラクタ
    public Answer(String aid, String body,String uid,String name) {
        this.aid = aid;
        this.body = body;
        this.uid = uid;
        this.name = name;
    }

    public String getAid() {return aid;} //aidを返す(念のため)

    public String getBody() {return body;}

    public String getUid() {return uid;}

    public String getName(){return name;}
}

