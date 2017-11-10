package com.norris.course_scheduling;

/**
 * Created by jennasowers on 11/8/17.
 */
public class Time {

    private final double[] startTimes = {8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5,
            12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17};
    private int delete; //delete this thing

    public Time() {

    }

    public double[] getStartTimes() {
        return startTimes;
    }
}
