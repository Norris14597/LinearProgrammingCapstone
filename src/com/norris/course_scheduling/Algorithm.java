package com.norris.course_scheduling;

/**
 *
 */
public class Algorithm {

    /*
****************************** LINEAR PROGRAMMING ALGORITHM ***************************************

READ list of rooms
READ list of courses with sections
READ list of professors

//  if improvement 1 is implemented this can be maximized
CREATE courses sum = 0 // total objective function sum for all courses

FOREACH course in course list
    CREATE course sum = 0 // sum of all sections
    READ course credits // 3 or 1
    READ course type (project, lab)

    FOREACH section in course
        READ professor assigned to section
        READ professor's schedule type (MWF or TR)

        CREATE best section sum = 0
        CREATE best room = null
        //only for MWF professor temporarily holds best time slot for section to room
        CREATE best MWF days with time slots for possible section assigned
        //only for TR professor temporarily holds best time slot for section to room
        CREATE best TR days with time slots for possible section assigned

        FOREACH room in room list
            COMPUTE happiness = {1.5: correct type, 1: no preference, 0 doesn't fit}
            COMPUTE efficiency = course size / room size


            IF section is happy with room // room fits type (and is big enough)

                // 1 credit and MWF schedule
                IF course credits = 1 and professors schedule type = MWF {
                    FOREACH day in professors schedule
                        FOREACH time in a day
                            //professor is available
                            IF time slot and slots for next hour + one slot on each side is available
                                // room is available (sections should have gaps naturally)
                                IF the same slots are available during same day for the room (including 15 minute gap)
                                    COMPUTE section sum = happiness * efficiency
                                    IF section sum > best section sum
                                        ASSIGN best section sum = section sum
                                        CLEAR MWF time slots // all slots
                                        ASSIGN time slots to M or W or F //current day
                                        ASSIGN best room = current room
                                    END IF // best section sum
                                END IF // room time slots available
                            END IF //professor time slots available
                        END FOREACH //time
                    END FOREACH //day

                }
                ELSE IF course credits = 1 and professors schedule type = TR {
                     FOREACH day in professors schedule
                        FOREACH time in a day
                            //professor is available
                            IF time slot and slots for next hour + one slot on each side is available
                                // room is available (sections should have gaps naturally)
                                IF the same slots are available during same day for the room (including 15 minute gap)
                                    COMPUTE section sum = happiness * efficiency
                                    IF section sum > best section sum
                                        ASSIGN best section sum = section sum
                                        CLEAR TR time slots // all slots
                                        ASSIGN time slots to T or R //current day
                                        ASSIGN best room = current room
                                    END IF // best section sum
                                END IF // room time slots available
                            END IF //professor time slots available
                        END FOREACH //time
                    END FOREACH //day

                }
                ELSE IF course credits = 3 and professors schedule type = MWF {
                    FOREACH time in day Monday
                        //professor is available
                        IF time slot and slots for next hour + one slot on each side is available for today (Monday) and W and F
                            // room is available (sections should have gaps naturally)
                            IF the same slots are available during same day (Monday) for the room and W and F (including 15 minute gap)
                                COMPUTE section sum = happiness * efficiency
                                IF section sum > best section sum
                                    ASSIGN best section sum = section sum
                                    CLEAR MWF time slots // all slots
                                    ASSIGN time slots to MWF //same slots to every day
                                    ASSIGN best room = current room
                                END IF // best section sum
                            END IF // room time slots available
                        END IF //professor time slots available
                    END FOREACH //time in monday
                }
                ELSE IF course credits = 3 and professors schedule type = TR {
                    FOREACH time in day Tuesday
                        //professor is available
                        IF time slot and slots for next 1.5 hour + one slot on each side is available for today (Tuesday) and R
                            // room is available (sections should have gaps naturally)
                            IF the same slots are available during same day (Tuesday) for the room and R (including 15 minute gap)
                                COMPUTE section sum = happiness * efficiency
                                IF section sum > best section sum
                                    ASSIGN best section sum = section sum
                                    CLEAR TR time slots // all slots
                                    ASSIGN time slots to TR //same slots to every day
                                    ASSIGN best room = current room
                                END IF // best section sum
                            END IF // room time slots available
                        END IF //professor time slots available
                    END FOREACH //time in tuesday
                }

            ENDIF //section happy with room: room type and enough seating

            //found best time fit for particular room (assuming all constraints met); there may be better room fits for section
        END FOREACH //rooms

        //************ best section to room fit and time has been found!!!.... hopefully!

        IF MWF time slots have been filled use best MWF times ELSE use TR best times

            // update section info
            ASSIGN section the best room and ASSIGN day time slots when being taught
            // only save filled slots not why (which section teaching)
            ASSIGN professors' time slots with MWF slots selected for teaching the current section
            // only save filled slots not why (which section in room)
            ASSIGN best room time slots to MWF slots selected for holding the current section

         END IF //time slots filled

         DISPLAY current assigned section, best room fit, and faculty info //no conflicts should exist
         sections sum = sections sum + best section sum
    END FOREACH //sections

    course sum = course sum + sections sum
END FOREACH //courses

courses sum = courses sum + sections sum



 */
}
