package com.example.javatea_client.models;

import java.util.HashMap;
import java.util.Set;

public class University {
    private final String univ_id;
    private String name;
    private String kana;
    private final HashMap<String, Lecture> lectures = new HashMap<>();
    private final HashMap<String, Faculty> faculties = new HashMap<>();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public Faculty createFaculty(String faculty_name) {
        if(this.faculties.containsKey(faculty_name)){
            return null;
        }
        faculties.put(faculty_name, new Faculty(faculty_name));
        return faculties.get(faculty_name);
    }

    public Faculty getFaculty(String faculty_name) {
        if(!faculties.containsKey(faculty_name)) {
            return null;
        }
        return faculties.get(faculty_name);
    }

    public Set<String> getFaculties() {
        return faculties.keySet();
    }

    public void addLecture(String lecture_id, Lecture lecture) {
        lectures.put(lecture_id, lecture);
    }

    public Lecture getLecture(String lecture_id) {
        if (!lectures.containsKey(lecture_id)) {
            return null;
        }
        return lectures.get(lecture_id);
    }

    public HashMap<String, Lecture> getLectures() {
        return lectures;
    }
}
