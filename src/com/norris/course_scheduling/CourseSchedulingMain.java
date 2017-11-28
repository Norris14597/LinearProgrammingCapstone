package com.norris.course_scheduling;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CourseSchedulingMain {

    //private String[] totalSchedule = new String[]{"M,T,W,R,F", "M", "T", "W", "R", "F", "M,W,F", "W,F", "T,R" };

    private static List<Course> courseList;
    private static List<Room> roomList;
    private static List<Professor> professorList;

    public static void main(String[] args) {

        professorList = getProfessorList();
        courseList = getCourseList();
        roomList = getRoomList();

        //TODO Make this more efficient
        //This is allows us to set one time block's isTimeFilled value to true
        //roomList.get(3).fill("Monday", 0, 7);

        //Suggestion on how to make it more efficient
        //TODO Associate course with room based on type similarity
        //TODO Use the M,T,W,R,F associated with professor to fill course section to room day
        //Change the class times (# of time blocks used) based on day section occurs


        //TODO Give happiness values to professors based on time of day
        // i.e. For a professor that works T-H,  T 8-13 = 1.5, T 14-17 = 0, default = 1;
        // means they want to work mornings on Tuesdays and dont care about time on Thursdays
        //TODO Pair the course section with room day using time blocks (change both to true?)
        //we will decide what time slots are taken by which course sections based on maximizing the happiness coefficient.
        //change isTimeFilled blocks to true when course section is accepted
        //limited to 3/1 credits and TR/ MWF schedule
        //TODO Hard constraint: at least 15 min break between classes
        //make a last filled room time with section class so it can be deleted when reassigned .

        System.out.println();

        /*********Outputs*************/
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            System.out.println("output.txt invalid file");
        }


        //professorList.forEach(System.out::println);
        //System.out.println();


        //courseList.forEach(System.out::println);
        System.out.println();


        //roomList.forEach(System.out::println);
        /*****************************/

        double totalSum = 0.0; //maximize objective value

        double totalCourseSum = 0.0; //sum of each section within a course
        //each course in course list
        for (Room r: roomList) {
            double bestSectionSum = 0.0;
            boolean timeSavedM = false;
            boolean timeSavedT = false;

            for (Course c : courseList) {
                double Hij = LinearProgramming.happinessRoomVal(r, c);
                double sectionSum = (r.getSeatingCapacity() / c.getCourseSize()) * Hij;

                for (Section s : c.getCourseSections()) {
                    Professor p = s.getProfessorAssigned();
                    String[] daysProfessorAvailable = p.getAvailableDaysNames();


                    //boolean timeSaved = false;

                    if (daysProfessorAvailable[0] == "Monday") {
                        int dayIndex = 0;
                        double sectionSumOld = 1.0;

                        for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
                            DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                            //all times in each day
                            for (int timeIndex = 0; timeIndex < d.getDayTimes().size(); timeIndex++) {
                                if (timeIndex + 4 <= 36 && c.getCredits() * 4 == 12 &&
                                        LinearProgramming.isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {

                                    boolean isRoomAvailable = LinearProgramming.isRoomTimeAvailable(timeIndex, r.getDayList(), "MWF");

                                    if (sectionSum > sectionSumOld) {
                                        System.out.println("course size: " + c.getCourseSize() + " room size: " + r.getSeatingCapacity() + " SECTION SUM: " + sectionSum + " happiness: " + Hij + " r type: " + r.getRoomType() + " c type: " + c.getCourseType());
                                        sectionSumOld = sectionSum;
                                    }

                                    if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {
                                        bestSectionSum = sectionSum;
                                        System.out.println("NEW BEST SUM: " + bestSectionSum);
                                        //System.out.println(r.getBuilding()+r.getRoomNum()+": "+c.getCourseCode()+": "+Hij);

                                        //unassign if previously assigned any time slots
                                        //will always be true if better sum previously
                                        if (timeSavedM) {
                                            timeIndex++;
                                            LinearProgramming.assignSectionToRoom(s, r, dayIndex, timeIndex, "MWF", false);
                                            System.out.println("\nAssigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF\n");
                                        }
                                        timeSavedM = true;
                                        bestSectionSum = sectionSum;
                                        LinearProgramming.assignSectionToRoom(s, r, dayIndex, timeIndex, "MWF", true);
                                        System.out.println("\nAssigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " MWF\n");
                                    }
                                }
                            }
                            dayIndex++;
                        }
                    }

                    if (daysProfessorAvailable[0] == "Tuesday") {
                        int dayIndex = 1;
                        double sectionSumOld = 1.0;

                        for (int i = 0; i < p.getAvailableDayTimes().size(); i++) {
                            DayTimes d = p.getAvailableDayTimes().get(i); //individual day
                            //all times in each day
                            for (int timeIndex = 0; timeIndex < d.getDayTimes().size(); timeIndex++) {
                                if (timeIndex + 6 <= 36 && c.getCredits() * 4 == 12 &&
                                        LinearProgramming.isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {
                                    boolean isRoomAvailable = LinearProgramming.isRoomTimeAvailable(timeIndex, r.getDayList(), "TR");
                                    if (sectionSum > sectionSumOld) {
                                        System.out.println("course size: " + c.getCourseSize() + " room size: " + r.getSeatingCapacity() + " SECTION SUM: " + sectionSum + " happiness: " + Hij + " r type: " + r.getRoomType() + " c type: " + c.getCourseType());
                                        sectionSumOld = sectionSum;
                                    }
                                    if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {
                                        bestSectionSum = sectionSum;
                                        System.out.println("NEW BEST SUM: " + bestSectionSum);
                                        //System.out.println(r.getBuilding()+r.getRoomNum()+": "+c.getCourseCode()+": "+Hij);

                                        //unassign if previously assigned any time slots
                                        //will always be true if better sum previously
                                        if (timeSavedT) {
                                            timeIndex++;
                                            LinearProgramming.assignSectionToRoom(s, r, dayIndex, timeIndex, "TR", false);
                                            System.out.println("\nAssigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " TR\n");
                                        }
                                        timeSavedT = true;
                                        bestSectionSum = sectionSum;
                                        LinearProgramming.assignSectionToRoom(s, r, dayIndex, timeIndex, "TR", true);
                                        System.out.println("\nAssigning " + c.getCourseCode() + " taught by " + p + " to classroom " + r.getRoomNum() + " TR\n");
                                    }
                                }
                            }
                            dayIndex++;
                        }
                    }
                }
            }
        }
//        professorList.forEach(System.out::println);
//        System.out.println();
//        courseList.forEach(System.out::println);
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
        sectionList101.add(new Section(sectionIds[1], courseName[0]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList101.add(new Section(sectionIds[2], courseName[0]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList101.add(new Section(sectionIds[3], courseName[0]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[0], 30, 3, courseTypes[0], sectionList101));

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

        return courseList;
    }

    private static List<Room> getRoomList() {
        //add random rooms
        //start with 5 rooms

        String buildings = "ABCDEFGHIJ";
        String roomNums = "1234567890";
        String[] roomTypes = {"computer", "lab", "projector", "standard"};

        List<Room> roomList = new ArrayList<Room>();

       // Room r = new Room("215E", "EGR", 30, "computer");
       // roomList.add(r);

        int roomSize = 10;
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
            String roomType = roomTypes[ThreadLocalRandom.current().nextInt(0, 3 + 1)];

            //course size
            roomSize += 10;
            roomList.add(new Room(roomNum, building, roomSize, roomType));
        }
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
        professors.add(Clement);
        professors.add(Jones);
        professors.add(Han);
        professors.add(Im);
        professors.add(Kolta);
        professors.add(Perkins);

        return professors;
    }

}
