package com.norris.course_scheduling;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CourseSchedulingMain {

    private String[] totalSchedule = new String[]{"M,T,W,R,F", "M", "T", "W", "R", "F", "M,W,F", "W,F", "T,R" };

    private static List<Course> courseList;
    private static List<Room> roomList;

    public static void main(String[] args) {

        courseList = getCourseList();
        roomList = getRoomList();

        courseList.forEach(System.out::println);
        roomList.forEach(System.out::println);

    }

    private static List<Course> getCourseList() {

        char[] sectionIds = {'A', 'B', 'C', 'D'};
        //add sections A-D
        List<Section> sectionList = new ArrayList<Section>();

        List<Course> courseList = new ArrayList<Course>();
        for (int i = 0; i < 3; i++) {
            sectionList.add(new Section(sectionIds[i], 0)); 
        }
        for (int i = 0; i < 5; i++) {
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String ints = "1234567890";
            StringBuilder courseTitle = new StringBuilder();
            Random rnd = new Random();
            while (courseTitle.length() < 6) { // length of the random string.

                if (courseTitle.length() < 3) {
                    int index = (int) (rnd.nextFloat() * chars.length());
                    courseTitle.append(chars.charAt(index));
                }
                else {
                    int index = (int) (rnd.nextFloat() * ints.length());
                    courseTitle.append(ints.charAt(index));
                }
            }

            String courseCode = courseTitle.toString();
            //course size
            int randomCourseSize = ThreadLocalRandom.current().nextInt(10, 50 + 1);
            courseList.add(new Course(courseCode, randomCourseSize, 3, sectionList));
        }
        return courseList;
    }

    private static List<Room> getRoomList() {
        //add random rooms
        //start with 5 rooms

        String buildings = "ABCDEFGHIJ";
        String roomNums = "1234567890";
        String[] roomTypes = {"computer", "lab", "projector"};

        List<Room> roomList = new ArrayList<Room>();

        for (int i = 0; i < 5; i++) {
            StringBuilder roomNumBuilder = new StringBuilder();
            Random rnd = new Random();

            //building
            int index = (int) (rnd.nextFloat() * buildings.length());
            String building = String.valueOf(buildings.charAt(index));
            //room number
            for (int j = 0; j < 3; j++) {
                index = (int) (rnd.nextFloat() * roomNums.length());
                roomNumBuilder.append(roomNums.charAt(index));    //set roomNum = to this if u want room num to be 3 digits long
            }
            String roomNum = roomNumBuilder.toString();
            //room type
            String roomType = roomTypes[ThreadLocalRandom.current().nextInt(0, 2 + 1)];

            //course size
            int randomRoomSize = ThreadLocalRandom.current().nextInt(10, 50 + 1);
            roomList.add(new Room(roomNum, building, randomRoomSize, roomType));
        }

        return roomList;
    }
}
