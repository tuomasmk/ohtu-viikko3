package ohtu;

import java.util.ArrayList;

public class Submission {
    private int week;
    private int hours;
    private ArrayList<Integer> exercises;
    private String course;
    private Course c;

    public Course getC() {
        return c;
    }

    public void setC(Course c) {
        this.c = c;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public ArrayList<Integer> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Integer> exercises) {
        this.exercises = exercises;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
    
    public String excercisesTostring() {
        String result = "";
        for (int i = 0; i < exercises.size() - 1; i++) {
            result += exercises.get(i) + ", ";
        }
        if (exercises.size() > 0) {
            result += exercises.get(exercises.size()-1);
        }
        return result;
    }

    @Override
    public String toString() {
        return "" + week;
    }
    
}