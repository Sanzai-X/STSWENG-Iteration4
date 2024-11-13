# University Enlistment System

- [X] A student is identified by his/her student number, which is non-negative integer
- [X] Student enlists in one or more sections The student may have already previously enlisted in other sections
- [X] A section is identified by its section ID, which is alphanumeric
- [X] A student cannot enlist in the same section more than once
- [X] The system makes sure that the student cannot enlist in any section that has a conflict with previously enlisted sections
- [X] A section is in conflict with another section if the schedules are in conflict
- [X] Schedules are as follows:
  - [X] Days:
      - [X] Mon/Thu, Tue/Fri, Wed/Sat
  - [X] Periods:
      - [X] 8:30am-10am, 10am-11:30, 11:30am-1pm, 1pm-2:30pm, 2:30pm-4pm, 4pm-5:30pm
- [X] A section has a room
- [X] A room is identified by its room name, which is alphanumeric
- [X] A room has a capacity
- [X] Section enlistment may not exceed the capacity of its room
- [X] Avoid race condition when where number of students may exceed section capacity when multiple students enlist at the same time.
  Make sure lock is held for the minimum amount of time.
- [X] Student can cancel an enlisted section
- [X] A section has a subject. (just 1 subject, not 'block section')
- [X] A subject is identified by its alphanumeric Subject ID.
- [X] A student cannot enlist in two sections with the same subject.
- [X] A subject may or may not have one or more prerequisite subjects.
- [X] A student may not enlist in a section if the student has not yet taken the prerequisite subjects.

## Change Request   
- [X] A student may not enlist in a section if its schedule overlaps with the schedule of any of its currently enlisted sections.
- [X] Periods may be of any duration of 30-min increments, w/in the hours of 8:30am - 5:30pm.
- [X] Periods may begin and may end at the top of each hour (9:00, 10:00, 11:00...) or at the bottom of each hour (9:30, 10:30, 11:30...).
- [X] End of a period may not be on or before the start of the period.
  
### Examples
* Valid Periods:
  * 8:30am - 9:00am
  * 9:00am - 12:00nn
  * 2:30pm - 4:30pm
  * 9:00am - 10:30am
* Invalid Periods:
  * 8:45am - 10:15am
    * Does not start at top or bottom of the hour
  * 12:00pm - 12:02pm
    * Not a 30 minute increment
  * 4:00pm - 3:00pm
    * Start time is after end time
  * 4:30pm - 6:00pm
    * End time is after 5:30pm
    
- [X] Sections cannot have the same room if their schedules are overlapping.
- [X] Student should have firstname and lastname

- [ ] A section has an instructor, a member of the faculty, identified by facultyNumber. Faculty number must be non-negative.
- [ ] Faculty should have lastname & firstname
- [ ] Two sections cannot share the same instructor if their schedules overlap