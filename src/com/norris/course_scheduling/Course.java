package com.norris.course_scheduling;


import java.util.List;

public class Course {

    private String courseCode;
    private double courseSize;
    private int credits;
    private String courseType;
    private List<Section> courseSections;


    public Course(String id, double size, int credits, String type, List<Section> sections){
        this.courseCode = id;
        this.courseSize = size;
        this.credits = credits;
        this.courseType = type;

        this.courseSections = sections;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseId) {
        this.courseCode = courseId;
    }

    public double getCourseSize() {
        return courseSize;
    }

    public void setCourseSize(double courseSize) {
        this.courseSize = courseSize;
    }

    public int getCredits() { return credits; }

    public void setCredits(int credits) { this.credits = credits; }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public List<Section> getCourseSections() {
        return courseSections;
    }

    public void setCourseSections(List<Section> courseSections) {
        this.courseSections = courseSections;
    }

    public String toString () {
        return "COURSE******** Code: "+courseCode+" Size: "+courseSize+" Credits: "+credits+" "+
                " Type: "+courseType+courseSections.toString();
    }
}
