package com.norris.course_scheduling;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LinearProgramming {


    public static void assignSectionToRoom(Section s, Room r, int day, int time, String scheduleType, boolean f) {

        switch (scheduleType) {
            case "MWF":
                int k = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        r.getDayList().get(day+k).getDayTimes().get(time+j).setTimeFilled(f);
                        s.getDayTimeAssigned().get(day+k).getDayTimes().get(time+j).setTimeFilled(f);
                    }
                    k += 2;
                }
                break;
            case "TR":
                k = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 6; j++) {
                        //I think this overflows if it tries to assign 2 TR classes at once
                        r.getDayList().get(day+k).getDayTimes().get(time+j).setTimeFilled(f);
                        s.getDayTimeAssigned().get(day+k).getDayTimes().get(time+j).setTimeFilled(f);
                    }
                    k += 2;
                }
                break;
        }
    }

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

    //checks if all time slots are available for faculty
    public static boolean isProfessorTimesAvailable(int timeIndex, List<DayTimes> days, int credits) {

        //size of days 1 / 1 credit = check 1 hour with surrounding minutes for single day
        //size of days 3 / 3 credits = check 1 hour with surrounding minutes for all three days
        //size of days 2 / 3 credits = check 1.5 hour with  surrounding minutes for both days

        //one credit on any day
        if (days.size() == 1 && credits == 1) {
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 4 < 36) { // not beginning and not end: take 6 slots
                return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled());
            }
            else if (timeIndex == 0) { //beginning of day take 5 slots
                return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled());
            }
            else if (timeIndex + 4 == 36) { //end of day check five slots
                return (!days.get(0).getDayTimes().get(timeIndex - 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled());
            }
            else { //33, 34, 35
                //there's not an hour left in the day so no class can be started here
                return false;
            }
        }
        else if (days.size() == 3 && credits == 3) {
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 4 < 36) {
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
            else if (timeIndex == 0) {
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
            else if (timeIndex + 4 == 36) {
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
            else {
                //there's not an hour left in the day so no class can be started here
                return false;
            }

        }
        else if (days.size() == 2 && credits == 3) {
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 6 < 36) {
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
            else if (timeIndex == 0) {
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
            else if (timeIndex + 6 == 36) {
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
            else {
                //there's not 1.5 hours left in the day so no class can be started here
                return false;
            }

        }
        else
            return false;

    }

    public static boolean isRoomTimesAvailable(int timeIndex, List<DayTimes> days, int credits, String day) {

        int index = 0;
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



        //one credit on any day
        //size of days 5 / 1 credit = check 1 hour with day and time index for single day
        if (credits == 1) {
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 4 < 36) { // not beginning and not end: take 6 slots
                return (!days.get(index).getDayTimes().get(timeIndex - 1).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 4).isTimeFilled());
            }
            else if (timeIndex == 0) { //beginning of day take 5 slots
                return (!days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 4).isTimeFilled());
            }
            else if (timeIndex + 4 == 36) { //end of day check five slots
                return (!days.get(index).getDayTimes().get(timeIndex - 1).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(index).getDayTimes().get(timeIndex + 3).isTimeFilled());
            }
            else { //33, 34, 35
                //there's not an hour left in the day so no class can be started here
                return false;
            }
        }
        else if (credits == 3 && day == "Monday") {
            //          if monday check 3 days MWF with 0, 2, 4 day indexes for one hour
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 4 < 36) {
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
            else if (timeIndex == 0) {
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
            else if (timeIndex + 4 == 36) {
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
            else {
                //there's not an hour left in the day so no class can be started here
                return false;
            }

        }
        else if (credits == 3 && day == "Tuesday") {
            //          if tuesday check 2 days TR with 1, 3 day indexes for 1.5 hours
            //first time isn't start of day
            if (timeIndex > 0 && timeIndex + 6 < 36) {
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
            else if (timeIndex == 1) {
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
            else if (timeIndex + 6 == 36) {
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
            else {
                //there's not 1.5 hours left in the day so no class can be started here
                return false;
            }

        }
        else
            return false;

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
