package com.example.javatea_client.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Department {
    private final String department_name;
    private final HashMap<String, Lecture> lectureInDepartment;

    private Set<Question> allQuestions;

    public Department(String department_name) {
        this.department_name = department_name;
        this.lectureInDepartment = new HashMap<>();
        this.allQuestions = new HashSet<>();
    }

    public String getDepartmentName() {
        return department_name;
    }

    public Lecture getLecture(String lectureId) {
        if(!lectureInDepartment.containsKey(lectureId)) {
            return null;
        }
        return lectureInDepartment.get(lectureId);
    }

    public HashMap<String, Lecture> getLectures() {
        return lectureInDepartment;
    }

    public Set<Question> getAllQuestions() {
        return allQuestions;
    }
}
