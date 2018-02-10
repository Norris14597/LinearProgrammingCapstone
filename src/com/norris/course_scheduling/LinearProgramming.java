package com.norris.course_scheduling;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


//time indexes 44 slots
//8: 0 - 3
//9: 4 - 7
//10: 8 - 11
//11: 12 - 15
//12: 16 - 19
//13: 20 - 23
//14: 24 - 27
//15: 28 - 31
//16: 32 - 35
//17: 36 - 39
//18: 40 - 43
//19: 44 - 47
//20: 48 - 51
//44 time slots


public class LinearProgramming {

    public static double happinessRoomVal(Room r, Course c) {
        //section fits roomsize and is proper room type
        if (r.getSeatingCapacity() >= c.getCourseSize() && r.getRoomType() == c.getCourseType()
                && c.getCourseType() != "standard") {
            return 1.5; //happy with room and section type
        }
        else if (r.getSeatingCapacity() >= c.getCourseSize() && c.getCourseType() == "standard") {
            return 1; //no preference for room type
        }
        else {
            return 0; //room did not fit section type
        }
    }

    private static int getDayIndex(String day) {
        int index;
        switch (day) {
            case "Monday":
                index = 0;
                break;
            case "Tuesday":
                index = 1;
                break;
            case "Wednesday":
                index = 2;
                break;
            case "Thursday":
                index = 3;
                break;
            case "Friday":
                index = 4;
                break;
            default:
                index = 0;
        }
        return index;
    }

    private static boolean checkHourOneDayR(int index, int timeIndex, List<DayTimes> days) {
        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
        else if (timeIndex + 3 == 51) { //end day check: 15before + !15after
            return (!days.get(index).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled());
        }
        else if (timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(index).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(index).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
    }

    private static boolean checkHourThreeDayR(int index, int timeIndex, List<DayTimes> days) {
        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
        else if (timeIndex + 3 == 51) { //end day check: 15before + !15after
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 3).isTimeFilled());
        }
        else if (timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(4).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
    }

    private static boolean checkHourHalfTwoDayR(int index, int timeIndex, List<DayTimes> days) {
        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 6).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 6).isTimeFilled());
        }
        else if (timeIndex + 5 == 51) { //end day check: 15before + !15after
            return (!days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 5).isTimeFilled());
        }
        else if (timeIndex == 47 || timeIndex == 48 || timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 6).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(3).getDayTimes().get(timeIndex + 6).isTimeFilled());
        }
    }

    private static boolean checkHourOneDayP(int timeIndex, List<DayTimes> days) {
        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
        else if (timeIndex + 3 == 51) { //end day check: 15before + !15after
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled());
        }
        else if (timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
    }

    private static boolean checkHourThreeDayP(int timeIndex, List<DayTimes> days) {

        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
        else if (timeIndex + 3 == 51) { //end day check: 15before + !15after
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled());
        }
        else if (timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(2).getDayTimes().get(timeIndex + 4).isTimeFilled());
        }
    }

    private static boolean checkHourHalfTwoDayP(int timeIndex, List<DayTimes> days) {

        if (timeIndex == 0) { //beginning day check: !15before + 15after
            return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 6).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 6).isTimeFilled());
        }
        else if (timeIndex + 5 == 51) { //end day check: 15before + !15after
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled());
        }
        else if (timeIndex == 47 || timeIndex == 48 || timeIndex == 49 || timeIndex == 50 || timeIndex == 51) { //not enough time for section
            return false;
        }
        else { //all other cases
            return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(0).getDayTimes().get(timeIndex + 6).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex - 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                    && !days.get(1).getDayTimes().get(timeIndex + 6).isTimeFilled());
        }
    }

    //checks if all time slots are available for faculty
    public static boolean isProfessorTimesAvailable(int timeIndex, List<DayTimes> days, int credits) {

        //one credit on any day only single day passed to method
        if (days.size() == 1 && credits == 1) {
            return checkHourOneDayP(timeIndex, days);
        }
        else if (days.size() == 3 && credits == 3) {
            //first time isn't start of day
            return checkHourThreeDayP(timeIndex, days);
        }
        else if (days.size() == 2 && credits == 3) {
            return checkHourHalfTwoDayP(timeIndex, days);
        }
        else {
            return false;
        }

    }


    //time index in day, all room times, credits, specific day
    public static boolean isRoomTimesAvailable(int timeIndex, List<DayTimes> days, int credits, String day) {

        int index = getDayIndex(day);

        //one credit on any day
        if (credits == 1) {
            return checkHourOneDayR(index, timeIndex, days);

        }
        else if (credits == 3 && day == "Monday") {
            //0, 2, 4 day indexes for one hour(4)
            return checkHourThreeDayR(index, timeIndex, days);
        }
        else if (credits == 3 && day == "Tuesday") {
            // if tuesday check 2 days TR with 1, 3 day indexes for 1.5 hours(6)
            return checkHourHalfTwoDayR(index, timeIndex, days);
        }
        else {
            return false;
        }
    }

    //returns List of either MWF or TR with all times cleared
    public static List<DayTimes> clearAllTimeSlots(List<DayTimes> bestDays) {
        for (DayTimes d: bestDays) {
            for (TimeLength t: d.getDayTimes()) {
                t.setTimeFilled(false);
            }
        }
        return bestDays;
    }

    //assigns time slots to either MWF or TR daytimes list
    public static List<DayTimes> assignTimeSlots(List<DayTimes> bestDays, int timeIndex, int credits, String day) {

        //get day index to assign if for single credit (one day)
        int index = 0;
        switch (day) {
            case "Monday":
                index = 0;
                break;
            case "Tuesday":
                index = 0;
                break;
            case "Wednesday":
                index = 1;
                break;
            case "Thursday":
                index = 1;
                break;
            case "Friday":
                index = 2;
                break;
            default:
                index = 0;
        }

        if (bestDays.size() == 2 && credits == 1) {
            //check tuesday or thursday in day string
            //assign off index of day in bestdays for tuesday or thursday for one hour
            for (int i = 0; i < 4; i++ ) {
                bestDays.get(index).getDayTimes().get(timeIndex+i).setTimeFilled(true);
            }
        }
        else if (bestDays.size() == 2 && credits == 3) {
            //assign for tuesday and thursday for 1.5 hour on both time slots
            //no need to check day string
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++ ) {
                    bestDays.get(i).getDayTimes().get(timeIndex+j).setTimeFilled(true);
                }
            }
        }
        else if (bestDays.size() == 3 && credits == 1) {
            //check monday or wednesday or friday in day string
            //assign off index of day in bestdays for monday or wednesday or friday for one hour
            for (int i = 0; i < 4; i++ ) {
                bestDays.get(index).getDayTimes().get(timeIndex+i).setTimeFilled(true);
            }
        }
        else if (bestDays.size() == 3 && credits == 3) {
            //assign for Monday and Wednesday and Friday for 1 hour on all time slots
            //no need to check day string
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++ ) {
                    bestDays.get(i).getDayTimes().get(timeIndex+j).setTimeFilled(true);
                }
            }
        }
        return bestDays;
    }

}
