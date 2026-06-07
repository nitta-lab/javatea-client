package com.example.javatea_client.models;

import java.util.HashMap;
import java.util.Set;

public class Faculty {
    private final String faculty_name;
    private final HashMap<String, Department> departments;
    private final HashMap<String, Lecture> lecturesInFaculty;
    public Faculty(String faculty_name) {
        this.faculty_name = faculty_name;
        this.departments = new HashMap<>();
        this.lecturesInFaculty = new HashMap<>();
    }

    public String getFacultyName() {
        return faculty_name;
    }

    public Department getDepartment(String department_name) {
        if(!departments.containsKey(department_name)) {
            return null;
        }
        return this.departments.get(department_name);
    }

    public Set<String> getDepartments() {
        return departments.keySet();
    }

    public Lecture getLecture(String lectureId) {
        if(!lecturesInFaculty.containsKey(lectureId)) {
            return null;
        }
        return lecturesInFaculty.get(lectureId);
    }

    public HashMap<String, Lecture> getLectures() {
        return this.lecturesInFaculty;
    }
}
