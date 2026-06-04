package com.example.javatea_client.models;


import com.example.javatea_client.viewModels.LectureViewModel;

public class Lecture  {

    private String name;
    private int grade;
    private String semester;
    private int frame;
    private String day;
    private int period;

    //コンストラクタ
    public Lecture() {}

    public Lecture(String name, int grade, String semester, int frame, String day, int period) {
        this.name = name;
        this.grade = grade;
        this.semester = semester;
        this.frame = frame;
        this.day = day;
        this.period = period;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public int getFrame() { return frame; }
    public void setFrame(int frame) { this.frame = frame; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }
}