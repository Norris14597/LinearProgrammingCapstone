package com.norris.course_scheduling;


public class Room {

    private String roomNum;
    private String building;
    private int seatingCapacity;
    private String roomType;

    public Room(String roomNum, String buildingNum, int seatingCapacity, String roomType) {
        this.roomNum = roomNum;
        this.building = buildingNum;
        this.seatingCapacity = seatingCapacity;
        this.roomType = roomType;
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
                +" Room Type: "+ this.roomType;
    }
}
