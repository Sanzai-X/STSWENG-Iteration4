package com.orangeandbronze.enlistment.domain;

import static com.orangeandbronze.enlistment.domain.TestUtils.*;

public class SectionBuilder {
    // Defaults
    private String sectionId = DEFAULT_SECTION_ID;
    private Subject subject = DEFAULT_SUBJECT;
    private Schedule schedule = MTH830to10;
    private Room room = newDefaultRoom();
    private int noStudents = 0;

    public SectionBuilder() {}

    public SectionBuilder(String sectionId) {
        this.sectionId = sectionId;
    }

    public SectionBuilder sectionId(String sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public SectionBuilder subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public SectionBuilder subject(String subjectId) {
        this.subject = new Subject(subjectId);
        return this;
    }

    public SectionBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public SectionBuilder room(Room room) {
        this.room = room;
        return this;
    }

    public SectionBuilder room(String roomName) {
        this.room = newRoom(roomName);
        return this;
    }

    public SectionBuilder room(String roomName, int capacity) {
        this.room = new Room(roomName, capacity);
        return this;
    }

    public SectionBuilder room(int capacity) {
        this.room = newRoom(capacity);
        return this;
    }

    public SectionBuilder noStudents(int num) {
        this.noStudents = num;
        return this;
    }

    public Section build() {
        return new Section(sectionId, subject, schedule, room, noStudents);
    }
}