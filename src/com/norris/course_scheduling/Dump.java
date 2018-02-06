package com.norris.course_scheduling;


public class Dump {

    // TR SCHEDULE
//                if (daysProfessorAvailable[0] == "Tuesday") {
//                        System.out.println(p.getProfessorName() + " It's a Tuesday!");
//
//                        double sectionSumOld = 1;
//
//                        for (Room r : roomList) {
//
//                        int dayIndex = 1; //start on tuesday
//                        // all days available for professor
//                        for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//        DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//
//        //all times in each day
//        for (int timeIndex = 0; timeIndex < d.getDayTimes().size(); timeIndex++) {
//
//        // hour available; at least an hour before end of day; 2 days available at the same time for professor
//        if (timeIndex + 6 <= 36 && c.getCredits() * 4 == 12 &&
//        LinearProgramming.isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {
//
//        //go through each room then view time if a good fit
//
//        boolean isRoomAvailable = LinearProgramming.isRoomTimeAvailable(timeIndex, r.getDayList(), "TR");
//        Hij = LinearProgramming.happinessRoomVal(r, c);
//
//        sectionSum = (c.getCourseSize() / r.getSeatingCapacity()) * Hij;
//        if (sectionSum > sectionSumOld) {
//        System.out.println("course size: " + c.getCourseSize() + " room size: " + r.getSeatingCapacity() + " SECTION SUM: " + sectionSum + " happiness: " + Hij + " r type: " + r.getRoomType() + " c type: " + c.getCourseType());
//        sectionSumOld = sectionSum;
//        }
//
//        if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {
//        bestSectionSum = sectionSum;
//        System.out.println("NEW BEST SUM: "+bestSectionSum);
//        //System.out.println(r.getBuilding()+r.getRoomNum()+": "+c.getCourseCode()+": "+Hij);
//
//        //unassign if previously assigned any time slots
//        //will always be true if better sum previously
//        if (timeSaved) {
//        LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "TR", false);
//        System.out.println("Reassigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " TR (time saved)");
//        }
//
//        lastSavedDayIndex = dayIndex;
//        lastSavedTimeIndex = timeIndex;
//        timeSaved = true;
//        bestSectionSum = sectionSum;
//        LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "TR", true);
//        System.out.println("Assigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " TR");
//        }
//        }
//        }
//
//        //System.out.println(dayIndex+":"+timeIndex);
//        }
//        dayIndex++;
//        }
//        }




















//    //each course in course list
//        for (Course c : courseList) {
//        System.out.println("IN COURSE "+c.getCourseCode());
//        System.out.println("COURSE CREDITS: "+c.getCredits()+" SIZE: "+c.getCourseSize()+" TYPE: "+c.getCourseType());
//        //each section within a course
//        for (Section s : c.getCourseSections()) {
//            System.out.println("IN SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode());
//            double Hij = 0; //happiness of courseType to roomType: 1.5 happy, 1 no preference, 0 not happy
//            int xij = 0; // 1 if section assigned to room, 0 otherwise
//            Professor p = s.getProfessorAssigned(); //professor teaching the section
//            System.out.println("THE PROFESSOR TEACHING SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode()+" IS "+p.getProfessorName());
//            String[] daysProfessorAvailable = p.getAvailableDaysNames(); //either MWF or TR schedule
//            for (int i = 0; i < daysProfessorAvailable.length; i++) {
//                System.out.println(p.getProfessorName()+" IS AVAILABLE "+daysProfessorAvailable[i]);
//            }
//
//            //*********** GOOD UP TO HERE GUYS ****************** //
//            //int lastSavedDayIndex = 0; //saves last starting day that meets all requirements
//            //int lastSavedTimeIndex = 0; //saves last starting time that meets all requirements
//            //boolean timeSaved = false;
//
//            //MWF schedule for professor
//            if (daysProfessorAvailable[0] == "Monday") {
//                //int dayIndex = 0; //start on monday
//                //double sectionSumOld = 1;
//
//                // all days available for professor
//                for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//                    DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//                    //all times in each day
//                    for (int timeIndex = 0; timeIndex < d.getDayTimes().size(); timeIndex++) {
//
//                        // hour available; at least an hour before end of day; 3 days available at the same time for professor
//                        if (timeIndex + 4 <= 36 && c.getCredits() * 4 == 12 &&
//                                LinearProgramming.isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {
//
//                            //go through each room then view time if a good fit
//                            for (Room r : roomList) {
//
//                                boolean isRoomAvailable = LinearProgramming.isRoomTimeAvailable(timeIndex, r.getDayList(), "MWF");
//                                Hij = LinearProgramming.happinessRoomVal(r, c);
//
//                                sectionSum = (c.getCourseSize() / r.getSeatingCapacity()) * Hij;
////                                    if (sectionSum > sectionSumOld) {
////                                        System.out.println("course size: " + c.getCourseSize() + " room size: " + r.getSeatingCapacity() + " SECTION SUM: " + sectionSum + " happiness: " + Hij + " r type: " + r.getRoomType() + " c type: " + c.getCourseType());
////                                        sectionSumOld = sectionSum;
////                                    }
////
////                                    if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {
////                                        bestSectionSum = sectionSum;
////                                        System.out.println("NEW BEST SUM: "+bestSectionSum);
////                                        //System.out.println(r.getBuilding()+r.getRoomNum()+": "+c.getCourseCode()+": "+Hij);
////
////                                        //unassign if previously assigned any time slots
////                                        //will always be true if better sum previously
////                                        if (timeSaved) {
////                                            LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "MWF", false);
////                                            System.out.println("Reassigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF (time saved)");
////                                        }
////
////                                        lastSavedDayIndex = dayIndex;
////                                        lastSavedTimeIndex = timeIndex;
////                                        timeSaved = true;
////                                        bestSectionSum = sectionSum;
////                                        LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "MWF", true);
////                                        System.out.println("Assigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF");
////                                    }
//                            }
//                        }
//
//                        //System.out.println(dayIndex+":"+timeIndex);
//                    }
//                    //dayIndex++;
//                }
//            }
//
//
//            //TR schedule for professor
//            //else if (p.getAvailableDayTimes().get(0).getDay() == "Tuesday") {
//
//            // }
//            //totalCourseSum += bestSectionSum;
//            //System.out.println("Total course sum " + totalCourseSum);
//        }
//
//        //ObjectiveSum += totalCourseSum;
//        //System.out.println("Total sum " + ObjectiveSum);
//
//    }
////        professorList.forEach(System.out::println);
////        System.out.println();
////        courseList.forEach(System.out::println);
//
//    //roomList.forEach(System.out::println);
//}




























    //    //each course in course list
//        for (Course c : courseList) {
//        System.out.println("IN COURSE "+c.getCourseCode());
//        System.out.println("COURSE CREDITS: "+c.getCredits()+" SIZE: "+c.getCourseSize()+" TYPE: "+c.getCourseType());
//        //each section within a course
//        for (Section s : c.getCourseSections()) {
//            System.out.println("IN SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode());
//            double Hij = 0; //happiness of courseType to roomType: 1.5 happy, 1 no preference, 0 not happy
//            int xij = 0; // 1 if section assigned to room, 0 otherwise
//            Professor p = s.getProfessorAssigned(); //professor teaching the section
//            System.out.println("THE PROFESSOR TEACHING SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode()+" IS "+p.getProfessorName());
//            String[] daysProfessorAvailable = p.getAvailableDaysNames(); //either MWF or TR schedule
//            for (int i = 0; i < daysProfessorAvailable.length; i++) {
//                System.out.println(p.getProfessorName()+" IS AVAILABLE "+daysProfessorAvailable[i]);
//            }
//
//            //*********** GOOD UP TO HERE GUYS ****************** //
//            //int lastSavedDayIndex = 0; //saves last starting day that meets all requirements
//            //int lastSavedTimeIndex = 0; //saves last starting time that meets all requirements
//            //boolean timeSaved = false;
//
//            //MWF schedule for professor
//            if (daysProfessorAvailable[0] == "Monday") {
//                //int dayIndex = 0; //start on monday
//                //double sectionSumOld = 1;
//
//                // all days available for professor
//                for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//                    DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//                    //all times in each day
//                    for (int timeIndex = 0; timeIndex < d.getDayTimes().size(); timeIndex++) {
//
//                        // hour available; at least an hour before end of day; 3 days available at the same time for professor
//                        if (timeIndex + 4 <= 36 && c.getCredits() * 4 == 12 &&
//                                LinearProgramming.isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {
//
//                            //go through each room then view time if a good fit
//                            for (Room r : roomList) {
//
//                                boolean isRoomAvailable = LinearProgramming.isRoomTimeAvailable(timeIndex, r.getDayList(), "MWF");
//                                Hij = LinearProgramming.happinessRoomVal(r, c);
//
//                                sectionSum = (c.getCourseSize() / r.getSeatingCapacity()) * Hij;
////                                    if (sectionSum > sectionSumOld) {
////                                        System.out.println("course size: " + c.getCourseSize() + " room size: " + r.getSeatingCapacity() + " SECTION SUM: " + sectionSum + " happiness: " + Hij + " r type: " + r.getRoomType() + " c type: " + c.getCourseType());
////                                        sectionSumOld = sectionSum;
////                                    }
////
////                                    if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {
////                                        bestSectionSum = sectionSum;
////                                        System.out.println("NEW BEST SUM: "+bestSectionSum);
////                                        //System.out.println(r.getBuilding()+r.getRoomNum()+": "+c.getCourseCode()+": "+Hij);
////
////                                        //unassign if previously assigned any time slots
////                                        //will always be true if better sum previously
////                                        if (timeSaved) {
////                                            LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "MWF", false);
////                                            System.out.println("Reassigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF (time saved)");
////                                        }
////
////                                        lastSavedDayIndex = dayIndex;
////                                        lastSavedTimeIndex = timeIndex;
////                                        timeSaved = true;
////                                        bestSectionSum = sectionSum;
////                                        LinearProgramming.assignSectionToRoom(s, r, lastSavedDayIndex, lastSavedTimeIndex, "MWF", true);
////                                        System.out.println("Assigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF");
////                                    }
//                            }
//                        }
//
//                        //System.out.println(dayIndex+":"+timeIndex);
//                    }
//                    //dayIndex++;
//                }
//            }
//
//
//            //TR schedule for professor
//            //else if (p.getAvailableDayTimes().get(0).getDay() == "Tuesday") {
//
//            // }
//            //totalCourseSum += bestSectionSum;
//            //System.out.println("Total course sum " + totalCourseSum);
//        }
//
//        //ObjectiveSum += totalCourseSum;
//        //System.out.println("Total sum " + ObjectiveSum);
//
//    }
////        professorList.forEach(System.out::println);
////        System.out.println();
////        courseList.forEach(System.out::println);
//
//    //roomList.forEach(System.out::println);
//}
}
