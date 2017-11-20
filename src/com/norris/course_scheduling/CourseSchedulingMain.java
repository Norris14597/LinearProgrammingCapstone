package com.norris.course_scheduling;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Time;
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
        roomList.get(3).fill("Monday", 0, 7);

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


        professorList.forEach(System.out::println);
        System.out.println();


        courseList.forEach(System.out::println);
        System.out.println();


        roomList.forEach(System.out::println);
        /*****************************/

        //each course in course list
        for (Course c : courseList) {
            //course info
            int coursePop = c.getCourseSize();
            int timeSlotsNeeded = c.getCredits() * 4; // total credits * 4 = total 15 minutes slots
            String courseType = c.getCourseType();
            double bestSectionSum = 0; //best fit for room to section

            //each section within a course
            for (Section s : c.getCourseSections()) {
                double sectionSum = 0; //individual sum
                double Hij = 0; //happiness of courseType to roomType: 1.5 happy, 1 no preference, 0 not happy
                int xij = 0; // 1 if section assigned to room, 0 otherwise
                double courseToRoomSeating = 0; //course population to room seating ratio
                Professor p = s.getProfessorAssigned(); //professor teaching the section

                //MWF schedule for professor
                if (p.getAvailableDayTimes().get(0).getDay() == "Monday") {

                    int timeIndex = 0; //start at 8:00AM
                    int dayIndex = 0; //start on monday

                    // 8:00 AM to 5:00 PM for professor availability on each day
                    for (TimeLength professorTime : p.getAvailableDayTimes().get(dayIndex).getDayTimes()) {

                        // hour available; at least an hour before end of day; 3 days available at the same time for professor
                        if (timeIndex + 4 <= 36 && timeSlotsNeeded == 12 &&
                                new CourseSchedulingMain().isProfessorTimeAvailable(timeIndex, p.getAvailableDayTimes())) {

                            //go through each room then view time if a good fit
                            for (Room r : roomList) {

                                boolean isRoomAvailable = new CourseSchedulingMain().isRoomTimeAvailable(timeIndex, r.getDayList(), "MWF");
                                Hij = new CourseSchedulingMain().happinessRoomVal(r, c);

                                sectionSum = c.getCourseSize()/r.getSeatingCapacity()*Hij;

                                if (isRoomAvailable && Hij >= 1 && sectionSum > bestSectionSum) {

                                    new CourseSchedulingMain().assignSectionToRoom(); //unnassign previous set section
                                    new CourseSchedulingMain().assignSectionToRoom();
                                }

                            }


                            //find if same times available in room
                        }
                        //increment time and days 36 15 minute timelengths in each day
                        timeIndex++;
                        if (timeIndex == 36) {
                            dayIndex++;
                            timeIndex = 0;
                        }
                    }
                }
                //TR schedule for professor
                else if (p.getAvailableDayTimes().get(0).getDay() == "Tuesday") {

                }
            }
        }
    }

    //courseToRoomSeating = coursePop / r.getSeatingCapacity();
    //r.getDayList().get(roomDayIndex).getDayTimes().get(timeIndex);
    // courseToRoomSeating = coursePop / r.getSeatingCapacity();
//    int roomDayIndex = 0;
//                                switch (p.getAvailableDayTimes().get(dayIndex).getDay()) {
//        case "Monday":
//            roomDayIndex = 0;
//            break;
//        case "Tuesday":
//            roomDayIndex = 1;
//            break;
//        case "Wednesday":
//            roomDayIndex = 2;
//            break;
//        case "Thursday":
//            roomDayIndex = 3;
//            break;
//        case "Friday":
//            roomDayIndex = 4;
//            break;
//        default:
//            roomDayIndex = 0;
//    }



    //int roomTimeIndex = (sTimeHour - 8) * 4 + sTimeMinute / 15; //time index within day for rooms
    private void assignSectionToRoom() {

    }
    private double happinessRoomVal(Room r, Course c) {
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

    private boolean isProfessorTimeAvailable(int timeIndex, List<DayTimes> days) {

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

    private boolean isRoomTimeAvailable(int timeIndex, List<DayTimes> days, String type) {

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

    private static List<Course> getCourseList() {

        List<Course> courseList = new ArrayList<Course>();
        char[] sectionIds = {'A', 'B', 'C', 'D'};
        String[] courseName = {"EGR101", "EGR102", "EGR103", "EGR104", "EGR105"};
        String[] courseTypes = {"computer", "lab", "projector", "basic"};

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
        courseList.add(new Course(courseName[3], 15, 3, courseTypes[3], sectionList104));

        //EGR105
        List<Section> sectionList105 = new ArrayList<Section>();
        sectionList105.add(new Section(sectionIds[0], courseName[4]+String.valueOf(sectionIds[0]), professorList.get(0)));
        sectionList105.add(new Section(sectionIds[1], courseName[4]+String.valueOf(sectionIds[1]), professorList.get(1)));
        sectionList105.add(new Section(sectionIds[2], courseName[4]+String.valueOf(sectionIds[2]), professorList.get(2)));
        sectionList105.add(new Section(sectionIds[3], courseName[4]+String.valueOf(sectionIds[3]), professorList.get(3)));
        courseList.add(new Course(courseName[4], 15, 3, courseTypes[2], sectionList105));

        return courseList;
    }

    private static List<Room> getRoomList() {
        //add random rooms
        //start with 5 rooms

        String buildings = "ABCDEFGHIJ";
        String roomNums = "1234567890";
        String[] roomTypes = {"computer", "lab", "projector", "basic"};

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
