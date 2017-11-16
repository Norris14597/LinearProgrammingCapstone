package com.norris.course_scheduling;


import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomNum;
    private String building;
    private int seatingCapacity;
    private String roomType;
    private List<Day> dayList; //holds M-F from 8-5pm

    public Room(String roomNum, String buildingNum, int seatingCapacity, String roomType) {
        this.roomNum = roomNum;
        this.building = buildingNum;
        this.seatingCapacity = seatingCapacity;
        this.roomType = roomType;

        dayList = new ArrayList<Day>();
        dayList.add(new Day("Monday"));
        dayList.add(new Day("Tuesday"));
        dayList.add(new Day("Wednesday"));
        dayList.add(new Day("Thursday"));
        dayList.add(new Day("Friday"));
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
        return "ROOM******** Room Number: "+this.roomNum+" Building: "+this.building+" Seating: "+this.seatingCapacity
                +" Room Type: "+ this.roomType+" TIME: "+dayList.toString()+'\n';
    }
}
