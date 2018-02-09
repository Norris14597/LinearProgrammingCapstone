package com.norris.course_scheduling;

public class CourseConflict {
    private String courseID;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private boolean courseOverlap = false;

    public CourseConflict(String courseID, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute) {
            this.courseID = courseID;
            this.startTimeHour = startTimeHour;
            this.startTimeMinute = startTimeMinute;
            this.endTimeHour = endTimeHour;
            this.endTimeMinute = endTimeMinute;
    }

    //rewrite this with minute checking
    public boolean doesCourseOverlap(CourseConflict addedCourse) {
        int course1 = this.courseID.charAt(3);
        int course2 = addedCourse.courseID.charAt(3);
        if ((course1 == 50 && course2 == 50) //No sophomore overlap
                || (course1 == 51 && course2 == 51) //No junior overlap
                || (course1 == 52 && course2 == 52) //No senior overlap
                || (course1 == 50 && course2 == 51) || (course1 == 51 && course2 == 50) //No sophomore/junior overlap
                || (course1 == 50 && course2 == 52) || (course1 == 52 && course2 == 50) //No sophomore/senior overlap
                || (course1 == 51 && course2 == 52) || (course1 == 52 && course2 == 51) //No junior/senior overlap
                ) {
            if (this.startTimeHour >= addedCourse.startTimeHour && this.startTimeHour <= addedCourse.endTimeHour) {
                if (this.startTimeMinute >= addedCourse.startTimeMinute && this.startTimeMinute <= addedCourse.endTimeMinute)
                    courseOverlap = true;
            }
            if (this.endTimeHour >= addedCourse.endTimeHour && this.endTimeHour <= addedCourse.endTimeHour) {
                if (this.endTimeMinute >= addedCourse.endTimeMinute && this.endTimeMinute <= addedCourse.endTimeMinute)
                    courseOverlap = true;
            }
            else
                courseOverlap = false;
        } else
            courseOverlap = false;
        return courseOverlap;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }
}
