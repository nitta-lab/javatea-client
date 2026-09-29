package com.example.javatea_client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class University {
    // univ_idとidで名前がそろってなかったため追加
    @JsonProperty("id")
    private String univ_id;

    private String name;
    private String kana;

    private final HashMap<String, Lecture> lectures = new HashMap<>();

//    private final HashMap<String, Faculty> faculties = new HashMap<>();

    // Listの型に修正
    private List<String> faculties;

    private final Set<Question> questions = new HashSet<>();
    private final Set<Question> allQuestions = new HashSet<>();


    // 空のUniversityがないと新しく設定できないためある
    private University(){};
    public University(String univ_id, String name, String kana) {
        this.univ_id = univ_id;
        this.name = name;
        this.kana = kana;
    }
    public String getId() {
        return univ_id;
    }

    public String getName() {
        return name;
    }

    public String getKana() {
        return kana;
    }

//    public Faculty getFaculty(String faculty_name) {
//        if(!faculties.containsKey(faculty_name)) {
//            return null;
//        }
//        return faculties.get(faculty_name);
//    }
//
//    public Set<String> getFaculties() {
//        return faculties.keySet();
//    }

    public Lecture getLecture(String lecture_id) {
        if (!lectures.containsKey(lecture_id)) {
            return null;
        }
        return lectures.get(lecture_id);
    }

    public HashMap<String, Lecture> getLectures() {
        return lectures;
    }

    public String getFaculty(String faculty_name) {
        if(faculties == null) {
            return null;
        }
        return faculty_name;
    }

    public List<String> getFaculties() {
        return faculties;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Set<Question> getAllQuestions() { return allQuestions; }
}
