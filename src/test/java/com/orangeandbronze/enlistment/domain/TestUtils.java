package com.orangeandbronze.enlistment.domain;

import java.time.*;
import java.util.*;

import static com.orangeandbronze.enlistment.domain.Days.*;

public class TestUtils {
    public static final Schedule MTH830to10 = new Schedule(MTH, new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)));
    public static final Schedule TF830to10 = new Schedule(TF, new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)));
    public static final Schedule TF10to1130 = new Schedule(TF, new Period(LocalTime.of(10, 0), LocalTime.of(11, 30)));
    public static final String DEFAULT_SECTION_ID = "DefaultSection";
    public static final String DEFAULT_SUBJECT_ID = "defaultSubject";
    public static final String DEFAULT_ROOM_NAME = "DefaultRoom";
    public static final Subject DEFAULT_SUBJECT = new Subject(DEFAULT_SUBJECT_ID);
    public static final Faculty DEFAULT_FACULTY = new Faculty(0, "John", "Doe");
    public static final int DEFAULT_STUDENT_NUMBER = 10;
    public static final int DEFAULT_FACULTY_NUMBER = 1000;
//    public static final Faculty DEFAULT_FACULTY = new Faculty(DEFAULT_FACULTY_NUMBER);

//    public static Faculty newFaculty(int facultyNumber) {
//        return new Faculty(facultyNumber);
//    }

    public static Student newStudent(int studentNumber, Collection<Section> sections, Collection<Subject> subjectsTaken) {
        return new Student(studentNumber, "firstname", "lastname", sections, subjectsTaken);
    }

    public static Student newStudent(int studentNumber, Collection<Section> sections) {
        return newStudent(studentNumber, sections, Collections.emptyList());
    }

    public static Student newStudent(int studentNumber) {
        return newStudent(studentNumber, Collections.emptyList());
    }
    /**
     * Return Student with studentNumber "1", no enlisted sections, no taken subjects
     **/
    public static Student newDefaultStudent() {
        return newStudent(DEFAULT_STUDENT_NUMBER);
    }

    public static Section newDefaultSection() {
        return new SectionBuilder().build();
    }

    public static Room newDefaultRoom() {
        return new Room(DEFAULT_ROOM_NAME, 10);
    }

    public static Room newRoom(String roomName) {
        return new Room(roomName, 10);
    }

    public static Room newRoom(int capacity) {
        return new Room(DEFAULT_ROOM_NAME, capacity);
    }

}
