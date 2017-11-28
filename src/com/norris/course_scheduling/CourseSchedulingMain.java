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

//*********** CURRENT UNDERSTANDING OF ENGINEERING REQUIREMENTS *************** //
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
//1. at least 15 minutes between classes
//2. no faculty assigned to more than one class (section) at a time
//3. no room holding more than one class (section) at a time
//4. course type (ex: needs projector) must be assigned to a room that fits that constraint with happiness of 1.5
//5. courses with no specified type (ex: standard) can be assigned to any type with happiness of 1


public class CourseSchedulingMain {

    private static List<Course> courseList;
    private static List<Room> roomList;
    private static List<Professor> professorList;

    public static void main(String[] args) {

        professorList = getProfessorList();
        courseList = getCourseList();
        roomList = getRoomList();



        /*********Outputs*************/
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            System.out.println("output.txt invalid file");
        }

        //view added professors, courses and rooms
        //professorList.forEach(System.out::println);
        //System.out.println();
        //courseList.forEach(System.out::println);
        //System.out.println();
        //roomList.forEach(System.out::println);
        /*****************************/

        //all sums based off: room to course happiness{1.5,1,0} * (course population/room size) * decision{1,0}
        //double ObjectiveSum = 0.0; //objective value to be maximized
        //double totalCourseSum = 0.0; //sum of all sections within a course
        //double bestSectionSum = 0.0; //best section fit
        double sectionSum = 0.0; //individual sum of each section within a course

        //each course in course list
        for (Course c : courseList) {
            System.out.println("IN COURSE "+c.getCourseCode());
            System.out.println("COURSE CREDITS: "+c.getCredits()+" SIZE: "+c.getCourseSize()+" TYPE: "+c.getCourseType());

            //each section within a course
            for (Section s : c.getCourseSections()) {
                System.out.println("IN SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode());

                double Hij = 0; //happiness of courseType to roomType: 1.5 happy, 1 no preference, 0 not happy
                int xij = 0; // 1 if section assigned to room, 0 otherwise
                Professor p = s.getProfessorAssigned(); //professor teaching the section
                System.out.println("THE PROFESSOR TEACHING SECTION "+s.getSectionID()+" WITHIN COURSE "+c.getCourseCode()+" IS "+p.getProfessorName());

                String[] daysProfessorAvailable = p.getAvailableDaysNames(); //either MWF or TR schedule
                for (int i = 0; i < daysProfessorAvailable.length; i++) {
                    System.out.println(p.getProfessorName()+" IS AVAILABLE "+daysProfessorAvailable[i]);
                }

                // each "DayTimes" a professor is available = each day with all times in a day
                System.out.println("SIZE OF DAY TIMES "+p.getAvailableDayTimes().size());
                //for testing purposes only set first hour of professors day filled
                p.getAvailableDayTimes().get(0).getDayTimes().get(0).setTimeFilled(true);
                p.getAvailableDayTimes().get(0).getDayTimes().get(1).setTimeFilled(true);
                p.getAvailableDayTimes().get(0).getDayTimes().get(2).setTimeFilled(true);
                p.getAvailableDayTimes().get(0).getDayTimes().get(3).setTimeFilled(true);

                for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
                    DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                    System.out.println(p.getProfessorName()+" IS AVAILABLE DAYS "+d.getDay());
                    //*********** GOOD UP TO HERE GUYS ****************** //

                    //each "TimeLength" within a single a day: 15 minutes each
                    for (int j = 0; j < d.getDayTimes().size(); j++) {
                        System.out.println("DAY TIME: "+ d.getDayTimes().get(j).toString());


                        //1 credit: TR ONE HOUR (4 times + 1 on each end) = 6 slots
                        //1 credit: MWF ONE HOUR (4 times + 1 on each end) = 6 slots
                        //3 credits: TR 1.5 HOUR (6 + 1 each end) * 2 = 16 slots
                        //3 credits: MWF 1 HOUR (4 times + 1 each end) * 3 = 18 slots
                    }

                }

            }

        }
    }


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

    //also, I left it random courses on my side.
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
        courseList.add(new Course(courseName[0], 30, 3, courseTypes[0], sectionList101));
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

        Room a = new Room("100J", "EGR", 33, "standard");
        Room b = new Room("215E", "EGR", 35, "computer");
        roomList.add(a);
        roomList.add(b);

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

        professors.add(Corso);
//        professors.add(Clement);
//        professors.add(Jones);
//        professors.add(Han);
//        professors.add(Im);
//        professors.add(Kolta);
//        professors.add(Perkins);

        return professors;
    }

}


