package com.example.javatea_client.models;

import java.util.ArrayList;
import java.util.List;

public class Timetable {

    // 年度を保存する変数
    // 例：2026
    private int year;

    // その年度に登録されている授業IDの一覧
    // 例：["lecture-id001", "lecture-id002"]
    private List<String> lectureIds;

    /**
     * 引数なしコンストラクタ
     * Timetableを作成したときに呼ばれる
     */
    public Timetable() {

        // 空のリストを作成
        // これをしないと lectureIds が null になる
        this.lectureIds = new ArrayList<>();
    }

    /**
     * 引数ありコンストラクタ
     * 年度と授業ID一覧をまとめて設定できる
     */
    public Timetable(int year, List<String> lectureIds) {

        // 受け取った年度を保存
        this.year = year;

        // 受け取った授業ID一覧を保存
        this.lectureIds = lectureIds;
    }

    /**
     * yearを取得するGetter
     */
    public int getYear() {
        return year;
    }

    /**
     * yearを設定するSetter
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * lectureIdsを取得するGetter
     */
    public List<String> getLectureIds() {
        return lectureIds;
    }

    /**
     * lectureIdsを設定するSetter
     */
    public void setLectureIds(List<String> lectureIds) {
        this.lectureIds = lectureIds;
    }
}