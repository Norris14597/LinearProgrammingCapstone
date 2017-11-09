package com.norris.course_scheduling;


public class RoomTimePair {

    private Room room;
    private int timeDurationMinutes;
    private double startTime;
    private static final double[] allStartTimes = {8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5,
            12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17};

    public RoomTimePair(Room room, int timeDurationMinutes, double startTime) {
        this.room = room;
        this.timeDurationMinutes = timeDurationMinutes;
        this.startTime = startTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getTimeDurationMinutes() {
        return timeDurationMinutes;
    }

    public void setTimeDurationMinutes(int timeDurationMinutes) {
        this.timeDurationMinutes = timeDurationMinutes;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public static double[] getAllStartTimes() {
        return allStartTimes;
    }
}
