package com.norris.course_scheduling;

import java.util.List;

/**
 * Created by jennasowers on 11/20/17.
 */
public class LinearProgramming {

    //unnassigns the last section saved
//    public static void unassignLastSectionToRoom(Section s, Room r, int day, int time, String scheduleType) {
//
//        switch(scheduleType) {
//            case "MWF":
//                int k = 0;
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 4; j++) {
//                        r.getDayList().get(day+k).getDayTimes().get(time+j).setTimeFilled(false);
//                        s.getDayTimeAssigned().get(day+k).getDayTimes().get(time+j).setTimeFilled(false);
//                        k += 2;
//                    }
//                }
//                break;
//            case "TR":
//                k = 1;
//                for (int i = 0; i < 2; i++) {
//                    for (int j = 0; j < 6; j++) {
//                        r.getDayList().get(day+k).getDayTimes().get(time+j).setTimeFilled(false);
//                        s.getDayTimeAssigned().get(day+k).getDayTimes().get(time+j).setTimeFilled(false);
//                        k += 2;
//                    }
//                }
//                break;
//        }

  //  }

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

    public static boolean isProfessorTimeAvailable(int timeIndex, List<DayTimes> days) {

        switch (days.get(0).getDay()) {
            case "Monday":
                return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled());
            case "Tuesday":
                return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 4).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 5).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled());
            default:
                return false;
        }

    }

    public static boolean isRoomTimeAvailable(int timeIndex, List<DayTimes> days, String type) {

        switch (type) {
            case "MWF":
                return (!days.get(0).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(0).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(2).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(4).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(4).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(4).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(4).getDayTimes().get(timeIndex + 3).isTimeFilled());
            case "TR":
                return (!days.get(1).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 4).isTimeFilled()
                        && !days.get(1).getDayTimes().get(timeIndex + 5).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex + 1).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex + 2).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex + 3).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex + 4).isTimeFilled()
                        && !days.get(3).getDayTimes().get(timeIndex + 5).isTimeFilled());
            default:
                return false;
        }

    }
}
