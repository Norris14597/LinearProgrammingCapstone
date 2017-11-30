package com.norris.course_scheduling;


import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomNum;
    private String building;
    private double seatingCapacity;
    private String roomType;
    private List<DayTimes> dayList; //holds M-F from 8-5pm

    public Room(String roomNum, String buildingNum, double seatingCapacity, String roomType) {
        this.roomNum = roomNum;
        this.building = buildingNum;
        this.seatingCapacity = seatingCapacity;
        this.roomType = roomType;

        dayList = new ArrayList<DayTimes>();
        dayList.add(new DayTimes("Monday"));
        dayList.add(new DayTimes("Tuesday"));
        dayList.add(new DayTimes("Wednesday"));
        dayList.add(new DayTimes("Thursday"));
        dayList.add(new DayTimes("Friday"));
    }

    public Room() {

    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public double getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(double seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getRoomType() { return roomType; }

    public void setRoomType(String roomType) { this.roomType = roomType; }

    public List<DayTimes> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayTimes> dayList) {
        this.dayList = dayList;
    }

    public void fill(String x, int y, int z){
        dayList.get(y).fillDay(x,z);
    }

    public void addDayTimes(List<DayTimes> dayTimesToAdd) {
        for (DayTimes d : dayList) {
            for (TimeLength t: d.getDayTimes()) {

                for (DayTimes dayToAdd: dayTimesToAdd) {
                    for (TimeLength timeAdd: dayToAdd.getDayTimes()) {
                        //same day and same time and time to add is filled set day time
                        if (d.getDay() == dayToAdd.getDay() && t.getStartTimeHour() == timeAdd.getStartTimeHour()
                                && t.getStartTimeMinute() == timeAdd.getStartTimeMinute() && timeAdd.isTimeFilled()) {
                            t.setTimeFilled(true);
                        }
                    }
                }


            }
        }
    }

    public String toString () {
        return "ROOM******** Number: "+this.roomNum+this.building+" Seating: "+this.seatingCapacity
                +" Type: "+ this.roomType+" TIME: \n"+dayList.toString()+'\n';
    }
}
