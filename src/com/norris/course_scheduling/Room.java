package com.norris.course_scheduling;


import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomNum;
    private String building;
    private int seatingCapacity;
    private String roomType;
    private List<DayTimes> dayList; //holds M-F from 8-5pm

    public Room(String roomNum, String buildingNum, int seatingCapacity, String roomType) {
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

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getRoomType() { return roomType; }

    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String toString () {
        return "ROOM******** Number: "+this.roomNum+this.building+" Seating: "+this.seatingCapacity
                +" Type: "+ this.roomType+" TIME: \n"+dayList.toString()+'\n';
    }
}
