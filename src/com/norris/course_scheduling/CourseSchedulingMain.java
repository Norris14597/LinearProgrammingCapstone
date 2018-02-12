package com.norris.course_scheduling;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

//*********** POSSIBLE WAYS OF IMPROVEMENT *************** //
//1. the first sections within the course lists will get priority on taking the first time slots. Possible
//      solution is to loop through the algorithm with every possible ordering of each section in order to
//      maximize the objective funciton further.

//*********** ENGINEERING SCHEDULING REQUIREMENTS *************** //
//1. professor can choose either a MWF or TR schedule type
//2. classes are either 1 or 3 credits
//3. 3 unit classes are either MWF (1 hour) or TR (1.5 hour) based
//4. 1 unit classes can be either MTWRF (1 hour)
//5. classes can start during an 15 minute period of any hour
//      1 credit: TR ONE HOUR (4 times + 1 on each end) = 6 slots
//      1 credit: MWF ONE HOUR (4 times + 1 on each end) = 6 slots
//      3 credits: TR 1.5 HOUR (6 + 1 each end) * 2 = 16 slots
//      3 credits: MWF 1 HOUR (4 times + 1 each end) * 3 = 18 slots

//********** CONSTRAINTS ***************//
//1. no faculty assigned to more than one class (section) at a time
//2. no room holding more than one class (section) at a time
//3. course type (ex: needs projector) must be assigned to a room that fits that constraint with happiness of 1.5
//4. at least 15 minutes between faculties end of class to next assigned class
//5. at least 15 minutes between any end of one class to the start of another class in any room
//6. section size must be less than or equal to room size assigned (efficiency weighting)


public class CourseSchedulingMain {

    private static List<Course> courseList;
    private static List<Room> roomList;
    private static List<Professor> professorList;

    private static List<Course> missedCourses = new ArrayList<>();
    private static List<Course> filledCourses = new ArrayList<>();

    public static void main(String[] args) {

        professorList = getProfessorList();
        courseList = getCourseList();
        roomList = getRoomList();

        //all sums based off: room to course happiness{1.5,1,0} * (course population/room size) * decision{1,0}
        double ObjectiveSum = 0.0; //objective value to be maximized
        double totalCourseSum = 0.0; //sum of all sections within a course

        /*********Outputs*************/
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            System.out.println("output.txt invalid file");
        }

        //each course in course list
        for (Course c : courseList) {
            double totalSectionsSum = 0.0; //sum of all sections within a course


            //each section within a course
            for (Section s : c.getCourseSections()) {
                boolean isSectionAssigned = false;

                Professor p = s.getProfessorAssigned(); //professor teaching the section
                String professorScheduleType = (p.getAvailableDayTimes().get(0).getDay() == "Monday") ? "MWF" : "TR";
                double bestSectionSum = 0.0; //best section fit
                Room bestRoom = new Room(); //best room fit

                //only for MWF professor temporarily holds best time slot for section to room
                List<DayTimes> bestMWF = new ArrayList<DayTimes>();
                bestMWF.add(new DayTimes("Monday"));
                bestMWF.add(new DayTimes("Wednesday"));
                bestMWF.add(new DayTimes("Friday"));
                //only for TR professor temporarily holds best time slot for section to room
                List<DayTimes> bestTR = new ArrayList<DayTimes>();
                bestTR.add(new DayTimes("Tuesday"));
                bestTR.add(new DayTimes("Thursday"));

                //each room in room list
                for (Room r : roomList) {
                    List<DayTimes> roomDays = r.getDayList();//room times for the week
                    //happiness = {1.5: correct type, 1: no preference, 0 doesn't fit}: must have enough seats as well
                    double Hij = LinearProgramming.happinessRoomVal(r, c);
                    double roomEfficiency = c.getCourseSize() / r.getSeatingCapacity(); // (course size/room seating)

                    System.out.println("In section: "+s.getSectionID()+" with "+p.getProfessorName()+" in room: "
                            +r.getBuilding()+r.getRoomNum());
                    //okay type and enough seats
                    if (Hij >= 1) {
                        System.out.println(r.getBuilding()+r.getRoomNum()+" fits "+s.getSectionID());

                        // 1 credit and MWF professor schedule
                        if (c.getCredits() == 1 && professorScheduleType.equals("MWF")) {
                            //each day in professors schedule
                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {

                                //store professor times
                                DayTimes d = p.getAvailableDayTimes().get(i); //individual daytimes of day within loop
                                List<DayTimes> professorDays = new ArrayList<>();
                                professorDays.add(d);

                                //each "TimeLength" within a single day for professor: 15 minutes each
                                for (int j = 0; j < d.getDayTimes().size(); j++) {

                                   //professor and room available during hour of day
                                    boolean isHourAvailableForRoom = LinearProgramming.isRoomTimesAvailable(j, roomDays, c.getCredits(), d.getDay());
                                    boolean isHourAvailableForProfessor = LinearProgramming.isProfessorTimesAvailable(j, professorDays, c.getCredits());

                                    //professor is available for one hour + 15 minutes before and after
                                    if (isHourAvailableForProfessor && isHourAvailableForRoom) {
                                        double sectionSum = Hij * roomEfficiency;
                                        if (sectionSum > bestSectionSum) {
                                            System.out.println("Both professor and room available at: "+i+": "+j+" sum is: "+sectionSum);
                                            bestSectionSum = sectionSum;
                                            bestMWF = LinearProgramming.clearAllTimeSlots(bestMWF);
                                            bestMWF = LinearProgramming.assignTimeSlots(bestMWF, j, c.getCredits(), d.getDay());
                                            bestRoom = r;
                                            isSectionAssigned = true;
                                        }
                                    }

                                } //end times
                            } //end days
                        } //end 1 credit and MWF schedule

                        // 1 credit and TR schedule
                        if (c.getCredits() == 1 && professorScheduleType.equals("TR")) {
                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {

                                //put professor into list for ease of passing to LP methods
                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                                List<DayTimes> professorDays = new ArrayList<>();
                                professorDays.add(d);

                                for (int j = 0; j < d.getDayTimes().size(); j++) {

                                    //check if professor and room are both available during this time
                                    boolean isHourAvailableForProfessor = LinearProgramming.isProfessorTimesAvailable(j, professorDays, c.getCredits());
                                    boolean isHourAvailableForRoom = LinearProgramming.isRoomTimesAvailable(j, roomDays, c.getCredits(), d.getDay());

                                    //professor is available for one hour + 15 minutes before and after
                                    if (isHourAvailableForProfessor && isHourAvailableForRoom) {

                                        double sectionSum = Hij * roomEfficiency;
                                        if (sectionSum > bestSectionSum) {
                                            System.out.println("Both professor and room available at: "+i+": "+j+" sum is: "+sectionSum);
                                            System.out.println();
                                            bestSectionSum = sectionSum;
                                            bestTR = LinearProgramming.clearAllTimeSlots(bestTR);
                                            bestTR = LinearProgramming.assignTimeSlots(bestTR, j, c.getCredits(), d.getDay());
                                            bestRoom = r;
                                            isSectionAssigned = true;
                                        }
                                    }

                                } //end times
                            } //end days
                        } //end 1 credit and TR schedule

                        // 3 credits and MWF schedule
                        if (c.getCredits() == 3 && professorScheduleType.equals("MWF")) {

                            List<DayTimes> professorDays = new ArrayList<>();
                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                                professorDays.add(d);
                            }


                            for (int j = 0; j < professorDays.get(0).getDayTimes().size(); j++) {

                                //check if professor and room are both available during this time
                                boolean isHourAvailableForProfessor = LinearProgramming.isProfessorTimesAvailable(j, professorDays, c.getCredits());
                                boolean isHourAvailableForRoom = LinearProgramming.isRoomTimesAvailable(j, roomDays, c.getCredits(), professorDays.get(0).getDay());

                                //professor is available for one hour + 15 minutes before and after
                                if (isHourAvailableForProfessor && isHourAvailableForRoom) {
                                    double sectionSum = Hij * roomEfficiency;
                                    if (sectionSum > bestSectionSum) {
                                        System.out.println("Both professor and room available at: "+j+" sum is: "+sectionSum);
                                        bestSectionSum = sectionSum;
                                        bestMWF = LinearProgramming.clearAllTimeSlots(bestMWF);
                                        bestMWF = LinearProgramming.assignTimeSlots(bestMWF, j, c.getCredits(), professorDays.get(0).getDay());
                                        bestRoom = r;
                                        isSectionAssigned = true;
                                    }
                                }

                            } //end times in Monday
                        } //end 3 credit and MWF schedule


                        // 3 credits and TR schedule
                        if (c.getCredits() == 3 && professorScheduleType.equals("TR")) {

                            List<DayTimes> professorDays = new ArrayList<>();
                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                                professorDays.add(d);
                            }

                            for (int j = 0; j < professorDays.get(0).getDayTimes().size(); j++) {

                                //check if professor and room are both available during this time
                                boolean isHourAvailableForProfessor = LinearProgramming.isProfessorTimesAvailable(j, professorDays, c.getCredits());
                                boolean isHourAvailableForRoom = LinearProgramming.isRoomTimesAvailable(j, roomDays, c.getCredits(), professorDays.get(0).getDay());


                                //professor is available for one hour + 15 minutes before and after
                                if (isHourAvailableForProfessor && isHourAvailableForRoom) {
                                    double sectionSum = Hij * roomEfficiency;
                                    if (sectionSum > bestSectionSum) {
                                        System.out.println("Both professor and room available at: "+j+" sum is: "+sectionSum);
                                        bestSectionSum = sectionSum;
                                        bestTR = LinearProgramming.clearAllTimeSlots(bestTR);
                                        bestMWF = LinearProgramming.assignTimeSlots(bestTR, j, c.getCredits(), professorDays.get(0).getDay());
                                        bestRoom = r;
                                        isSectionAssigned = true;
                                    }
                                }

                            } //end times in tuesday
                        } //end 3 credit and TR schedule

                    } //end if happy
                    //**** found best time fit for particular room (assuming all constraints met); there may be better room fits for section
                } //end roomlist

                //************ best section to room fit and time has been found!!!.... hopefully!
                if (isSectionAssigned) {
                    //DISPLAY current assigned section, best room fit, and faculty info //no conflicts should exist

                    //    IF MWF time slots have been filled use best MWF times ELSE use TR best times
                    List<DayTimes> bestTimes = (professorScheduleType == "MWF") ? bestMWF : bestTR;

                    for (DayTimes d : bestTimes) {
                        for (int i = 0; i < bestTimes.size(); i++) {
                            for (int j = 1; j < d.getDayTimes().size(); j++) {
                                if (d.getDayTimes().get(j - 1).isTimeFilled() && j == 1) {
                                    bestTimes.get(i).getDayTimes().get(j-1).setCoursePlaceHolder("Course: " + s.getSectionID());
                                    bestTimes.get(i).getDayTimes().get(j).setCoursePlaceHolder("Professor: " + s.getProfessorAssigned());
                                } else if (d.getDayTimes().get(j).isTimeFilled() && !d.getDayTimes().get(j - 1).isTimeFilled()) {
                                    bestTimes.get(i).getDayTimes().get(j).setCoursePlaceHolder("Course: " + s.getSectionID());
                                    bestTimes.get(i).getDayTimes().get(j+1).setCoursePlaceHolder("Professor: " + s.getProfessorAssigned());
                                }
                            }
                        }
                    }

                    //    ASSIGN section the best room and ASSIGN day time slots when being taught
                    System.out.println("Assigning course: " + s.getSectionID() + " size: " + c.getCourseSize() + " to: "
                            + bestRoom.getBuilding() + bestRoom.getRoomNum() + " with seating: " + bestRoom.getSeatingCapacity());
                   // System.out.println("At time: \n" + bestTimes);
                   // System.out.println("At " + bestRoom.getBuilding() + bestRoom.getRoomNum() + " time: \n" + bestRoom.getDayList());
                    System.out.println("with professor: " + p.getProfessorName());
                    //System.out.println(p.getAvailableDayTimes());
                    System.out.println();
                    System.out.println();

                    s.setRoomAssigned(bestRoom);
                    s.setDayTimeAssigned(bestTimes);
                    //    ASSIGN professors' time slots with current section slots, add section to teaching list
                    p.setAvailableDayTimes(bestTimes);
                    p.addDayTimes(bestTimes);
                    p.getSectionsTaught().add(s);
                    //assign best times to the best room fit

                    bestRoom.addDayTimes(bestTimes);

                }
                totalSectionsSum += bestSectionSum;
            } // end sectionlist

            totalCourseSum += totalSectionsSum;
        }// end course list

        ObjectiveSum += totalCourseSum;

//        for (Course c : courseList) {
//            try {
//                for (int i = 0; i < c.getCourseSections().size(); i++)
//                if (c.getCourseSections().get(i).getRoomAssigned().getRoomNum() != "") {
//                    filledCourses.add(c);
//                }
//            } catch (NullPointerException e) {
//                missedCourses.add(c);
//            }
//        }

        //outputPCR();
    }//end main



    private static void outputPCR() {
        System.out.println();
        System.out.println("************** LIST OF PROFESSORS **************");
        professorList.forEach(System.out::println);
        System.out.println();
        System.out.println("*************** LIST OF COURSES ****************");
        courseList.forEach(System.out::println);
        System.out.println();
        System.out.println("**************** LIST OF ROOMS *****************");
        roomList.forEach(System.out::println);
        System.out.println();

        System.out.println("************* COURSES NOT ASSIGNED *************");
        missedCourses.forEach(System.out::println);

    }


    private static List<Course> getCourseList() {

        List<Course> courseList = new ArrayList<Course>();
        char[] sectionIds = {'A', 'B', 'C', 'D'};
        String[] courseName = {"EGR101", "EGR102", "EGR103", "EGR104", "EGR105", "EGR106", "EGR107", "EGR108",
            "EGR109", "EGR110", "EGR111", "EGR112", "EGR113", "EGR114"};
        String[] courseTypes = {"computer", "lab", "projector", "standard"};

        //EGR101
        List<Section> sectionList101 = new ArrayList<Section>();
        sectionList101.add(new Section(sectionIds[0], courseName[0]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList101.add(new Section(sectionIds[1], courseName[0]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList101.add(new Section(sectionIds[2], courseName[0]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList101.add(new Section(sectionIds[3], courseName[0]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[0], 32, 3, courseTypes[3], sectionList101));

        //EGR102
        List<Section> sectionList102 = new ArrayList<Section>();
        sectionList102.add(new Section(sectionIds[0], courseName[1]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList102.add(new Section(sectionIds[1], courseName[1]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList102.add(new Section(sectionIds[2], courseName[1]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList102.add(new Section(sectionIds[3], courseName[1]+String.valueOf(sectionIds[3]), professorList.get(5)));
        courseList.add(new Course(courseName[1], 50, 3, courseTypes[3], sectionList102));

        //EGR103
        List<Section> sectionList103 = new ArrayList<Section>();
        sectionList103.add(new Section(sectionIds[0], courseName[2]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList103.add(new Section(sectionIds[1], courseName[2]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList103.add(new Section(sectionIds[2], courseName[2]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList103.add(new Section(sectionIds[3], courseName[2]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[2], 15, 3, courseTypes[3], sectionList103));

        //EGR104
        List<Section> sectionList104 = new ArrayList<Section>();
        sectionList104.add(new Section(sectionIds[0], courseName[3]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList104.add(new Section(sectionIds[1], courseName[3]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList104.add(new Section(sectionIds[2], courseName[3]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList104.add(new Section(sectionIds[3], courseName[3]+String.valueOf(sectionIds[3]), professorList.get(5)));
        courseList.add(new Course(courseName[3], 20, 3, courseTypes[3], sectionList104));

        //EGR105
        List<Section> sectionList105 = new ArrayList<Section>();
        sectionList105.add(new Section(sectionIds[0], courseName[4]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList105.add(new Section(sectionIds[1], courseName[4]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList105.add(new Section(sectionIds[2], courseName[4]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList105.add(new Section(sectionIds[3], courseName[4]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[4], 40, 3, courseTypes[3], sectionList105));

        //EGR106
        List<Section> sectionList106 = new ArrayList<Section>();
        sectionList106.add(new Section(sectionIds[0], courseName[5]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList106.add(new Section(sectionIds[1], courseName[5]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList106.add(new Section(sectionIds[2], courseName[5]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList106.add(new Section(sectionIds[3], courseName[5]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[5], 50, 3, courseTypes[3], sectionList106));

        //EGR107
        List<Section> sectionList107 = new ArrayList<Section>();
        sectionList107.add(new Section(sectionIds[0], courseName[6]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList107.add(new Section(sectionIds[1], courseName[6]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList107.add(new Section(sectionIds[2], courseName[6]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList107.add(new Section(sectionIds[3], courseName[6]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[6], 10, 3, courseTypes[3], sectionList107));//course name, peopel, credits, type,sections

        //EGR108
        List<Section> sectionList108 = new ArrayList<Section>();
        sectionList108.add(new Section(sectionIds[0], courseName[7]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList108.add(new Section(sectionIds[1], courseName[7]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList108.add(new Section(sectionIds[2], courseName[7]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList108.add(new Section(sectionIds[3], courseName[7]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[7], 40, 1, courseTypes[3], sectionList108));//course name, peopel, credits, type,sections

        //EGR109
        List<Section> sectionList109 = new ArrayList<Section>();
        sectionList109.add(new Section(sectionIds[0], courseName[8]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList109.add(new Section(sectionIds[1], courseName[8]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList109.add(new Section(sectionIds[2], courseName[8]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList109.add(new Section(sectionIds[3], courseName[8]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[8], 30, 3, courseTypes[3], sectionList109));//course name, peopel, credits, type,sections

        //EGR110
        List<Section> sectionList110 = new ArrayList<Section>();
        sectionList110.add(new Section(sectionIds[0], courseName[9]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList110.add(new Section(sectionIds[1], courseName[9]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList110.add(new Section(sectionIds[2], courseName[9]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList110.add(new Section(sectionIds[3], courseName[9]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[9], 30, 1, courseTypes[3], sectionList110));//course name, peopel, credits, type,sections

        //EGR111
        List<Section> sectionList111 = new ArrayList<Section>();
        sectionList111.add(new Section(sectionIds[0], courseName[10]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList111.add(new Section(sectionIds[1], courseName[10]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList111.add(new Section(sectionIds[2], courseName[10]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList111.add(new Section(sectionIds[3], courseName[10]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[10], 33, 3, courseTypes[3], sectionList111));//course name, peopel, credits, type,sections

        //EGR112
        List<Section> sectionList112 = new ArrayList<Section>();
        sectionList112.add(new Section(sectionIds[0], courseName[11]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList112.add(new Section(sectionIds[1], courseName[11]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList112.add(new Section(sectionIds[2], courseName[11]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList112.add(new Section(sectionIds[3], courseName[11]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[11], 50, 3, courseTypes[3], sectionList112));//course name, peopel, credits, type,sections

        //EGR113
        List<Section> sectionList113 = new ArrayList<Section>();
        sectionList113.add(new Section(sectionIds[0], courseName[12]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList113.add(new Section(sectionIds[1], courseName[12]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList113.add(new Section(sectionIds[2], courseName[12]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList113.add(new Section(sectionIds[3], courseName[12]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[12], 30, 3, courseTypes[3], sectionList113));//course name, peopel, credits, type,sections

        //EGR114
        List<Section> sectionList114 = new ArrayList<Section>();
        sectionList114.add(new Section(sectionIds[0], courseName[13]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList114.add(new Section(sectionIds[1], courseName[13]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList114.add(new Section(sectionIds[2], courseName[13]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList114.add(new Section(sectionIds[3], courseName[13]+String.valueOf(sectionIds[3]), professorList.get(7)));
        courseList.add(new Course(courseName[13], 30, 3, courseTypes[3], sectionList114));//course name, peopel, credits, type,sections


        return courseList;
    }

    private static List<Room> getRoomList() {
        List<Room> roomList = new ArrayList<Room>();

        Room a = new Room("215E", "EGR", 33, "standard");
        Room b = new Room("215A", "EGR", 40, "standard");
        Room c = new Room("100J", "EGR", 55, "standard");
        Room test = new Room("TTT", "EGR", 100, "standard");

        //testing room
        for(int i = 0; i < 5; i++){
            for (int j = 0; j < 52; j++) {
                test.getDayList().get(i).getDayTimes().get(j).setTimeFilled(true);
            }
        }

        roomList.add(a);
        roomList.add(b);
        roomList.add(c);
        roomList.add(test);

        return roomList;
    }
    private static List<Professor> getProfessorList() {

        List<Professor> professors = new ArrayList<>();
        Professor Corso = new Professor("Anthony Corso", new String[] {"Monday", "Wednesday", "Friday"});
        Professor Clement = new Professor("Larry Clement", new String[] {"Monday", "Wednesday", "Friday"});
        Professor Jones = new Professor("Creed Jones", new String[] {"Monday", "Wednesday", "Friday"});
        Professor Han = new Professor("Mi Kyung Han", new String[] {"Monday", "Wednesday", "Friday"});
        Professor Im = new Professor("Kyungsoo Im", new String[] {"Tuesday", "Thursday"});
        Professor Kolta = new Professor("Michael Kolta", new String[] {"Monday", "Wednesday", "Friday"});
        Professor Perkins = new Professor("Arlene Louise Perkins", new String[] {"Tuesday", "Thursday"});

        Professor MrTest = new Professor("Mr TestMWF", new String[] {"Tuesday", "Thursday"});


        professors.add(Corso);
        professors.add(Clement);
        professors.add(Jones);
        professors.add(Han);
        professors.add(Im);
        professors.add(Kolta);
        professors.add(Perkins);
        professors.add(MrTest);

        return professors;
    }

}
