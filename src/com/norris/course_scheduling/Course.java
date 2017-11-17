package com.norris.course_scheduling;


import java.util.List;

public class Course {

    private String courseCode;
    private int courseSize;
    private int credits;
    private String courseType;
    private List<Section> courseSections;


    public Course(String id, int size, int credits, String type, List<Section> sections){
        this.courseCode = id;
        this.courseSize = size;
        this.credits = credits;
        this.courseType = type;

        this.courseSections = sections;
    }

    public String getCourseId() {
        return courseCode;
    }

    public void setCourseId(String courseId) {
        this.courseCode = courseId;
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
        return "COURSE******** Code: "+this.courseCode+" Size: "+this.courseSize+" Type: "+this.courseType+" Credits: "+this.credits+" Section:\n"+
                this.courseSections.toString()+'\n';
    }
}
