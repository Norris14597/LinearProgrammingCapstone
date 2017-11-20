package com.norris.course_scheduling;

/**
 * Created by jennasowers on 11/8/17.
 */
public class TimeLength {

    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;

    private boolean isTimeFilled;

    public TimeLength(int sh, int sm, int eh, int em) {
        this.startTimeHour = sh;
        this.startTimeMinute = sm;
        this.endTimeHour = eh;
        this.endTimeMinute = em;
        this.isTimeFilled = false;
    }

    public void setTrue(int x) {
        if (x == 1)
            isTimeFilled = true;
        else
            isTimeFilled = false;
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

    public boolean isTimeFilled() {
        return isTimeFilled;
    }

    public void setTimeFilled(boolean timeFilled) {
        isTimeFilled = timeFilled;
    }

    public String toString() {
        return "\n\t\tstarttime: "+String.format("%02d",startTimeHour)+":"+String.format("%02d",startTimeMinute)+"   |   "+"endtime: "+String.format("%02d",endTimeHour)+":"+String.format("%02d",endTimeMinute)+"   |   "+isTimeFilled;
    }
}
