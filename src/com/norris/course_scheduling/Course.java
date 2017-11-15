package com.norris.course_scheduling;


import java.util.List;

public class Course {

    private String courseId;
    private int courseSize;
    private int credits;
    private List<Section> courseSections;


    public Course(String id, int size, int credits, List<Section> sections){
        this.courseId = id;
        this.courseSize = size;
        this.credits = credits;
        this.courseSections = sections;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getCourseSize() {
        return courseSize;
    }

    public void setCourseSize(int courseSize) {
        this.courseSize = courseSize;
    }

    public int getCredits() { return credits; }

    public void setCredits(int credits) { this.credits = credits; }

    public List<Section> getCourseSections() {
        return courseSections;
    }

    public void setCourseSections(List<Section> courseSections) {
        this.courseSections = courseSections;
    }

    public String toString () {
        return "COURSE******** ID: "+this.courseId+" Size: "+this.courseSize+" Credits: "+this.credits+" Section: "+
                this.courseSections.toString();
    }
}
