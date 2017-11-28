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
//2. add another weighting for faculty preference to either teaching in the morning or afternoon:
//      ex: Pij = {1.5 fits preference, 1 has no preference, .5 doesnt fit preference
//      0 for not fitting preference would 0 out the sum of the section to room fit when it is possibly
//      an ok fit so .5 is more appropriate
//3. currently the main schedule can be viewed from each section: contains the room and times being taught
//      as well as the professor teaching. Possibly add section string to professors and rooms to view what
//      sections are occupying their time slots

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

    //happiness of section type (lap, computer) to room fit
    private static final double SECTION_ROOM_HAPPY = 1.5; //same type
    private static final double SECTION_ROOM_OK = 1; //no section preference
    private static final double SECTION_ROOM_MAD = 0; //room does not fill sections needs

    //decision of room being assigned
    private static final int SECTION_ASSIGNED = 1;
    private static final int SECTION_NOT_ASSIGNED = 0;


    public static void main(String[] args) {



        professorList = getProfessorList();
        courseList = getCourseList();
        roomList = getRoomList();
        //all sums based off: room to course happiness{1.5,1,0} * (course population/room size) * decision{1,0}
        double ObjectiveSum = 0.0; //objective value to be maximized

        /*********Outputs*************/
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            System.out.println("output.txt invalid file");
        }
        //outputPCR();
        double totalCourseSum = 0.0; //sum of all sections within a course

        //each course in course list
        for (Course c : courseList) {
            double totalSectionsSum = 0.0; //sum of all sections within a course

            System.out.println("IN COURSE "+c.getCourseCode());
            System.out.println("COURSE CREDITS: "+c.getCredits()+" SIZE: "+c.getCourseSize()+" TYPE: "+c.getCourseType());

            //each section within a course
            for (Section s : c.getCourseSections()) {

                Professor p = s.getProfessorAssigned(); //professor teaching the section
                String professorScheduleType = (p.getAvailableDayTimes().get(0).getDay() == "Monday") ? "MWF" : "TR";
                double bestSectionSum = 0.0; //best section fit
                Room bestRoom = null;
                System.out.println("IN SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode());
                System.out.println("THE PROFESSOR TEACHING SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode()+" IS "+p.getProfessorName());
                System.out.println("SIZE OF DAY TIMES "+p.getAvailableDayTimes().size());
                System.out.println("PROFESSOR SCHEDULE TYPE  "+professorScheduleType);

                //only for MWF professor temporarily holds best time slot for section to room
                List<DayTimes> bestMWF = new ArrayList<DayTimes>();
                bestMWF.add(new DayTimes("Monday"));
                bestMWF.add(new DayTimes("Wednesday"));
                bestMWF.add(new DayTimes("Friday"));
                //only for TR professor temporarily holds best time slot for section to room
                List<DayTimes> bestTR = new ArrayList<DayTimes>();
                bestTR.add(new DayTimes("Tuesday"));
                bestTR.add(new DayTimes("Thursday"));


                for (Room r : roomList) {
                    //happiness = {1.5: correct type, 1: no preference, 0 doesn't fit}: must have enough seats as well
                    double Hij = LinearProgramming.happinessRoomVal(r, c);
                    double roomEfficiency = c.getCourseSize() / r.getSeatingCapacity(); // (course size/room seating)
                    List<DayTimes> roomDays = r.getDayList();
                    System.out.println("HAPPINESS WAS CHANGED TO: "+Hij+" FOR "+r.getRoomType()+" "+c.getCourseType());
                    System.out.println("ROOM EFFICIENCY IS: COURSE"+c.getCourseSize() +"ROOM" +r.getSeatingCapacity());
                    System.out.println("ROOM EFFICIENCY IS: "+roomEfficiency+" FOR "+r.getRoomType()+" "+c.getCourseType());

                    System.out.println("ROOOM DAYS "+roomDays.toString());

                    //okay type and enough seats: doesnt take into account availability yet
                    if (Hij >= 1) {

                        // 1 credit and MWF schedule
//                        if (c.getCredits() == 1 && professorScheduleType == "MWF") {
//                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//                                System.out.println("IN MWF WITH 1 CREDIT");
//                                //put professor into list for ease of passing to LP methods
//                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//                                List<DayTimes> professorDays = new ArrayList<>();
//                                professorDays.add(d);
//
//                                System.out.println(p.getProfessorName()+" IS AVAILABLE DAYS "+d.getDay());
//
//                                //each "TimeLength" within a single a day: 15 minutes each for professor availability
//                                for (int j = 0; j < d.getDayTimes().size(); j++) {
//
//                                   //check if professor and room are both available during this time
//                                    boolean isHourAvailableForProfessor = LinearProgramming.isTimesAvailable(j, professorDays, c.getCredits());
//                                    boolean isHourAvailableForRoom = LinearProgramming.isTimesAvailable(j, roomDays, c.getCredits());
//                                    System.out.println("return okay");
//                                    //professor is available for one hour + 15 minutes before and after
//                                    if (isHourAvailableForProfessor && isHourAvailableForRoom) {
//                                        double sectionSum = Hij * roomEfficiency;
//                                        if (sectionSum > bestSectionSum) {
//                                            bestSectionSum = sectionSum;
//                                            bestMWF = LinearProgramming.clearAllTimeSlots(bestMWF);
//                                            bestMWF = LinearProgramming.assignTimeSlots(bestMWF, j, c.getCredits(), d.getDay());
//                                            bestRoom = r;
//                                            System.out.println("*********************"+"FIT HAS BEEN FOUND ******************");
//                                        }
//                                    }
//
//                                } //end times
//                            } //end days
//                        } //end 1 credit and MWF schedule

                        // 1 credit and TR schedule
                        if (c.getCredits() == 1 && professorScheduleType == "TR") {
                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {

                                //put professor into list for ease of passing to LP methods
                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                                List<DayTimes> professorDays = new ArrayList<>();
                                professorDays.add(d);

                                //each "TimeLength" within a single a day: 15 minutes each for professor availability
                                System.out.println("DAY: ***************"+ d.getDay());
                                for (int j = 0; j < d.getDayTimes().size(); j++) {
                                    System.out.println("DAY TIME: "+ d.getDayTimes().get(j).toString());

                                    //check if professor and room are both available during this time
                                    boolean isHourAvailableForProfessor = LinearProgramming.isProfessorTimesAvailable(j, professorDays, c.getCredits());
                                    boolean isHourAvailableForRoom = LinearProgramming.isRoomTimesAvailable(j, roomDays, c.getCredits(), d.getDay());
                                    System.out.println("HOURS AVAILABLE: "+ j+" PROFESSOR OK TO TEACH: "+isHourAvailableForProfessor+"ROOM OK TO BE FILLED: "+isHourAvailableForRoom);
                                    System.out.println();
                                    System.out.println();

                                    //professor is available for one hour + 15 minutes before and after
                                    if (isHourAvailableForProfessor && isHourAvailableForRoom) {
                                        double sectionSum = Hij * roomEfficiency;
                                        if (sectionSum > bestSectionSum) {
                                            bestSectionSum = sectionSum;
                                            bestTR = LinearProgramming.clearAllTimeSlots(bestTR);
                                            bestTR = LinearProgramming.assignTimeSlots(bestTR, j, c.getCredits(), d.getDay());
                                            bestRoom = r;
                                        }
                                    }

                                } //end times
                            } //end days
                        } //end 1 credit and TR schedule

                        // 3 credits and MWF schedule
//                        if (c.getCredits() == 3 && professorScheduleType == "MWF") {
//                            System.out.println("IN MWF WITH 3 CREDITS");
//                            //put professor into list for ease of passing to LP methods
//                            List<DayTimes> professorDays = new ArrayList<>();
//                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//                                professorDays.add(d);
//                                System.out.println(p.getProfessorName()+" IS AVAILABLE DAYS "+d.getDay());
//                            }
//
//                            //each "TimeLength" within a MONDAY: 15 minutes each for professor availability
//                            for (int j = 0; j < professorDays.get(0).getDayTimes().size(); j++) {
//                                //System.out.println("DAY TIME: "+ d.getDayTimes().get(j).toString());
//
//                                //check if professor and room are both available during this time
//                                boolean isHourAvailableForProfessor = LinearProgramming.isTimesAvailable(j, professorDays, c.getCredits());
//                                boolean isHourAvailableForRoom = LinearProgramming.isTimesAvailable(j, roomDays, c.getCredits());
//
//
//                                //professor is available for one hour + 15 minutes before and after
//                                if (isHourAvailableForProfessor && isHourAvailableForRoom) {
//                                    double sectionSum = Hij * roomEfficiency;
//                                    if (sectionSum > bestSectionSum) {
//                                        bestSectionSum = sectionSum;
//                                        bestMWF = LinearProgramming.clearAllTimeSlots(bestMWF);
//                                        bestMWF = LinearProgramming.assignTimeSlots(bestMWF, j, c.getCredits(), professorDays.get(0).getDay());
//                                        bestRoom = r;
//                                    }
//                                }
//
//                            } //end times in Monday
//                        } //end 3 credit and MWF schedule


                        // 3 credits and TR schedule
//                        if (c.getCredits() == 3 && professorScheduleType == "TR") {
//
//                            //put professor into list for ease of passing to LP methods
//                            List<DayTimes> professorDays = new ArrayList<>();
//                            for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
//                                DayTimes d = p.getAvailableDayTimes().get(i); //individual day
//                                professorDays.add(d);
//                                //System.out.println(p.getProfessorName()+" IS AVAILABLE DAYS "+d.getDay());
//                            }
//
//                            //each "TimeLength" within TUESDAY: 15 minutes each for professor availability
//                            for (int j = 0; j < professorDays.get(0).getDayTimes().size(); j++) {
//                                //System.out.println("DAY TIME: "+ d.getDayTimes().get(j).toString());
//
//                                //check if professor and room are both available during this time
//                                boolean isHourAvailableForProfessor = LinearProgramming.isTimesAvailable(j, professorDays, c.getCredits());
//                                boolean isHourAvailableForRoom = LinearProgramming.isTimesAvailable(j, roomDays, c.getCredits());
//
//                                System.out.println("return okay");
//
//                                //professor is available for one hour + 15 minutes before and after
//                                if (isHourAvailableForProfessor && isHourAvailableForRoom) {
//                                    double sectionSum = Hij * roomEfficiency;
//                                    if (sectionSum > bestSectionSum) {
//                                        bestSectionSum = sectionSum;
//                                        bestTR = LinearProgramming.clearAllTimeSlots(bestTR);
//                                        bestMWF = LinearProgramming.assignTimeSlots(bestTR, j, c.getCredits(), professorDays.get(0).getDay());
//                                        bestRoom = r;
//                                    }
//                                }
//
//                            } //end times in tuesday
//                        } //end 3 credit and TR schedule

                    } //end if happy

                    //**** found best time fit for particular room (assuming all constraints met); there may be better room fits for section
                } //end roomlist

                //************ best section to room fit and time has been found!!!.... hopefully!

                //    IF MWF time slots have been filled use best MWF times ELSE use TR best times
                List<DayTimes> bestTimes = new ArrayList<DayTimes>();
                bestTimes = (professorScheduleType == "MWF")? bestMWF: bestTR;

                //    ASSIGN section the best room and ASSIGN day time slots when being taught
                s.setRoomAssigned(bestRoom);
                s.setDayTimeAssigned(bestTimes);
                //    ASSIGN professors' time slots with current section slots, add section to teaching list
                //p.setAvailableDayTimes(bestTimes);
                p.getSectionsTaught().add(s);

                //assign best times to the best room fit
               // bestRoom.setDayList(bestTimes);


//
//    DISPLAY current assigned section, best room fit, and faculty info //no conflicts should exist
                totalSectionsSum += bestSectionSum;

            } // end sectionlist

            totalCourseSum += totalSectionsSum;
        }// end course list

        ObjectiveSum += totalCourseSum;
        outputPCR();
    }//end main



    private static void outputPCR() {
        professorList.forEach(System.out::println);
        System.out.println();
        courseList.forEach(System.out::println);
        System.out.println();
        roomList.forEach(System.out::println);
    }



    private static List<Course> getCourseList() {

        List<Course> courseList = new ArrayList<Course>();
        char[] sectionIds = {'A', 'B', 'C', 'D'};
        String[] courseName = {"EGR101", "EGR102", "EGR103", "EGR104", "EGR105"};
        String[] courseTypes = {"computer", "lab", "projector", "standard"};

        //EGR101
        List<Section> sectionList101 = new ArrayList<Section>();
        sectionList101.add(new Section(sectionIds[0], courseName[0]+String.valueOf(sectionIds[0]), professorList.get(0)));
//        sectionList101.add(new Section(sectionIds[1], courseName[0]+String.valueOf(sectionIds[1]), professorList.get(1)));
//        sectionList101.add(new Section(sectionIds[2], courseName[0]+String.valueOf(sectionIds[2]), professorList.get(2)));
//        sectionList101.add(new Section(sectionIds[3], courseName[0]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[0], 32, 1, courseTypes[1], sectionList101));
        //public Course(String id, int size, int credits, String type, List<Section> sections){
/*
        //EGR102
        List<Section> sectionList102 = new ArrayList<Section>();
        sectionList102.add(new Section(sectionIds[0], courseName[1]+String.valueOf(sectionIds[0]), professorList.get(4)));
        sectionList102.add(new Section(sectionIds[1], courseName[1]+String.valueOf(sectionIds[1]), professorList.get(5)));
        sectionList102.add(new Section(sectionIds[2], courseName[1]+String.valueOf(sectionIds[2]), professorList.get(6)));
        sectionList102.add(new Section(sectionIds[3], courseName[1]+String.valueOf(sectionIds[3]), professorList.get(5)));
        courseList.add(new Course(courseName[1], 50, 3, courseTypes[1], sectionList102));

        //EGR103
        List<Section> sectionList103 = new ArrayList<Section>();
        sectionList103.add(new Section(sectionIds[0], courseName[2]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList103.add(new Section(sectionIds[1], courseName[2]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList103.add(new Section(sectionIds[2], courseName[2]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList103.add(new Section(sectionIds[3], courseName[2]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[2], 15, 3, courseTypes[2], sectionList103));

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
        courseList.add(new Course(courseName[4], 40, 3, courseTypes[2], sectionList105));
*/
        return courseList;
    }

    private static List<Room> getRoomList() {
        //add random rooms
        //start with 5 rooms

        String buildings = "ABCDEFGHIJ";
        String roomNums = "1234567890";
        String[] roomTypes = {"computer", "lab", "projector", "standard"};

        List<Room> roomList = new ArrayList<Room>();

        Room a = new Room("100J", "EGR", 33, "lab");
        Room b = new Room("215E", "EGR", 35, "computer");
        roomList.add(a);
        //roomList.add(b);

//        int roomSize = 10;
//        for (int i = 0; i < 5; i++) {
//            StringBuilder roomNumBuilder = new StringBuilder();
//            Random rnd = new Random();
//
//            //building
//            int index = (int) (rnd.nextFloat() * buildings.length());
//            String building = String.valueOf(buildings.charAt(index));
//            //room number
//            for (int j = 0; j < 3; j++) {
//                index = (int) (rnd.nextFloat() * roomNums.length());
//                roomNumBuilder.append(roomNums.charAt(index));    //set roomNum = to this if u want room num to be 3 digits long
//            }
//            String roomNum = roomNumBuilder.toString();
//            //room type
//            String roomType = roomTypes[ThreadLocalRandom.current().nextInt(0, 3 + 1)];
//
//            //course size
//            roomSize += 10;
//            roomList.add(new Room(roomNum, building, roomSize, roomType));
//        }



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

        //for testing purposes only set first hour of professors day filled
        Im.getAvailableDayTimes().get(0).getDayTimes().get(0).setTimeFilled(true);
        Im.getAvailableDayTimes().get(0).getDayTimes().get(1).setTimeFilled(true);
        Im.getAvailableDayTimes().get(0).getDayTimes().get(2).setTimeFilled(true);
        Im.getAvailableDayTimes().get(0).getDayTimes().get(3).setTimeFilled(true);

        //professors.add(Corso);

//        professors.add(Clement);
//        professors.add(Jones);
//        professors.add(Han);
        professors.add(Im);
//        professors.add(Kolta);
//        professors.add(Perkins);

        return professors;
    }

}
