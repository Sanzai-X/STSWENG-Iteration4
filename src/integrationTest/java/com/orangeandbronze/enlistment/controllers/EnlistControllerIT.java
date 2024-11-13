package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.jdbc.core.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import static com.orangeandbronze.enlistment.controllers.UserAction.*;
import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
class EnlistControllerIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    private final static String TEST = "TEST";

    @Container
    private final PostgreSQLContainer container =
            new PostgreSQLContainer("postgres:14")
                    .withDatabaseName(TEST)
                    .withUsername(TEST)
                    .withPassword(TEST);

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:14:///" + TEST);
        registry.add("spring.datasource.password", () -> TEST);
        registry.add("spring.datasource.username", () -> TEST);
    }

    @Test
    void enlist_student_in_section() throws Exception {
        // Given in teh DB: a student record and a section record
        String sqlInsertStudent = """
                    INSERT INTO student (student_number, firstname, lastname)
                    VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sqlInsertStudent, DEFAULT_STUDENT_NUMBER,"firstname", "lastname");
        // insert room
        String roomName = "Room101";
        jdbcTemplate.update("INSERT INTO room (name, capacity) VALUES (?,?)", roomName, 10 );
        // insert subject
        jdbcTemplate.update("INSERT INTO subject (subject_id) VALUES (?)", DEFAULT_SUBJECT_ID );
        // insert section
        String sqlInsertSection = """
                INSERT INTO section (section_id, number_of_students, days, start_time,
                end_time, room_name, subject_subject_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sqlInsertSection, DEFAULT_SECTION_ID, 0, Days.MTH.ordinal(),
                LocalTime.of(9, 0), LocalTime.of(10,0),
                roomName, DEFAULT_SUBJECT_ID);
        // When the POST method on path "/enlist" is invoked

        // with the "sectionId" parameter matching the section ID in the student record
        // and with UserAction parameter "ENLIST"
        // and a student object is in session corresponding to the student record
        Student student = studentRepository.findById(DEFAULT_STUDENT_NUMBER).orElseThrow();
        mockMvc.perform(post("/enlist").sessionAttr("student", student)
                .param("sectionId", DEFAULT_SECTION_ID).param("userAction", ENLIST.name()));
        // Then a new record in the student_sections table should be created
        // containing the corresponding studentNumber and setionId
        String sqlAssert = """
                    SELECT COUNT(*) FROM student_sections WHERE  student_student_number = ?
                    AND sections_section_id = ?
                """;
        int recordCount = jdbcTemplate.queryForObject(sqlAssert, Integer.class,
                DEFAULT_STUDENT_NUMBER, DEFAULT_SECTION_ID);
        assertEquals(1,recordCount);
    }

    @Test
    void cancel_student_in_section() throws Exception {
        // Given in teh DB: a student record and a section record
        String sqlInsertStudent = """
                INSERT INTO student (student_number, firstname, lastname)
                VALUES (?, ?, ?)
            """;
        jdbcTemplate.update(sqlInsertStudent, DEFAULT_STUDENT_NUMBER, "firstname", "lastname");

        // Insert room
        String roomName = "Room101";
        jdbcTemplate.update("INSERT INTO room (name, capacity) VALUES (?, ?)", roomName, 10);

        // Insert subject
        jdbcTemplate.update("INSERT INTO subject (subject_id) VALUES (?)", DEFAULT_SUBJECT_ID);

        // Insert section
        String sqlInsertSection = """
            INSERT INTO section (section_id, number_of_students, days, start_time,
            end_time, room_name, subject_subject_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sqlInsertSection, DEFAULT_SECTION_ID, 1, Days.MTH.ordinal(),
                LocalTime.of(9, 0), LocalTime.of(10, 0),
                roomName, DEFAULT_SUBJECT_ID);

        // Enlist student in the section to set up the cancel action
        jdbcTemplate.update("INSERT INTO student_sections (student_student_number, sections_section_id) VALUES (?, ?)",
                DEFAULT_STUDENT_NUMBER, DEFAULT_SECTION_ID);

        // with the "sectionId" parameter matching the section ID in the student record
        // and with UserAction parameter "CANCEL"
        // and a student object is in session corresponding to the student record
        Student student = studentRepository.findById(DEFAULT_STUDENT_NUMBER).orElseThrow();
        mockMvc.perform(post("/enlist").sessionAttr("student", student)
                .param("sectionId", DEFAULT_SECTION_ID).param("userAction", CANCEL.name()));

        // Then the record in the student_sections table should be deleted
        // Ensure that no record exists in the student_sections table with this student and section
        String sqlAssert = """
                SELECT COUNT(*) FROM student_sections
                WHERE student_student_number = ?
                AND sections_section_id = ?
            """;
        int recordCount = jdbcTemplate.queryForObject(sqlAssert, Integer.class,
                DEFAULT_STUDENT_NUMBER, DEFAULT_SECTION_ID);
        assertEquals(0, recordCount); // Expecting 0 since the student should have been removed
    }

    private final static int FIRST_STUDENT_NO = 11;
    private final static int NUMBER_OF_STUDENTS = 5;
    private final static int LAST_STUDENT_NUMBER = FIRST_STUDENT_NO + NUMBER_OF_STUDENTS - 1;

    private void insertManyStudents() {
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = FIRST_STUDENT_NO; i <= LAST_STUDENT_NUMBER; i++) {
            batchArgs.add(new Object[]{i, "firstname", "lastname"});
        }
        jdbcTemplate.batchUpdate("INSERT INTO student(student_number, firstname, lastname) VALUES (?, ?, ?)", batchArgs);
    }

    private void insertNewDefaultSectionWithCapacity(int capacity) {
        final String roomName = "roomName";
        jdbcTemplate.update("INSERT INTO room (name, capacity) VALUES (?, ?)", roomName, capacity);
        jdbcTemplate.update("INSERT INTO subject (subject_id) VALUES (?)", DEFAULT_SUBJECT_ID);
        jdbcTemplate.update(
                "INSERT INTO section (section_id, number_of_students, days, start_time, end_time, room_name, subject_subject_id, version)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                DEFAULT_SECTION_ID, 0, Days.MTH.ordinal(), LocalTime.of(9, 0), LocalTime.of(10, 0), roomName, DEFAULT_SUBJECT_ID, 0);
    }

    private void assertNumberOfStudentsSuccessfullyEnlistedInDefaultSection(int expectedCount) {
        assertAll(
                () -> {
                    int numStudentSectionAssociations = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM student_sections WHERE sections_section_id = ?", Integer.class, DEFAULT_SECTION_ID);
                    assertEquals(expectedCount, numStudentSectionAssociations, "count in student_sections");
                },
                () -> {
                    int numStudentsInSectionField = jdbcTemplate.queryForObject("SELECT number_of_students FROM section WHERE section_id = ?",
                            Integer.class, DEFAULT_SECTION_ID);
                    assertEquals(expectedCount, numStudentsInSectionField, "count in section");
                }
        );
    }

    @Test
    void enlist_concurrent_separate_section_instances_representing_same_record_students_beyond_capacity() throws Exception {
        // Given a single section record with capacity 1, and several student records
        insertManyStudents();
        final int capacity = 1;
        insertNewDefaultSectionWithCapacity(capacity);
        // When each student enlists in that same section at the same time
        startEnlistmentThreads();
        // Then only one student should be able to list
        assertNumberOfStudentsSuccessfullyEnlistedInDefaultSection(capacity);
    }


    @Test
    void enlist_concurrently_same_section_enough_capacity() throws Exception {
        insertManyStudents();
        insertNewDefaultSectionWithCapacity(NUMBER_OF_STUDENTS);
        startEnlistmentThreads();
        assertNumberOfStudentsSuccessfullyEnlistedInDefaultSection(NUMBER_OF_STUDENTS);
    }



    private void startEnlistmentThreads() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = FIRST_STUDENT_NO; i <= LAST_STUDENT_NUMBER; i++) {
            final int studentNo = i;
            new EnslistmentThread(studentRepository.findById(studentNo).orElseThrow(() ->
                    new NoSuchElementException("No student w/ student num " + studentNo + " found in DB.")),
                    latch, mockMvc).start();
        }
        latch.countDown();
        Thread.sleep(5000); // wait time to allow all the threads to finish
    }

    private static class EnslistmentThread extends Thread {
        private final Student student;
        private final CountDownLatch latch;
        private final MockMvc mockMvc;

        public EnslistmentThread(Student student, CountDownLatch latch, MockMvc mockMvc) {
            this.student = student;
            this.latch = latch;
            this.mockMvc = mockMvc;
        }

        @Override
        public void run() {
            try {
                latch.await(); // The thread keeps waiting till it is informed
                mockMvc.perform(post("/enlist").sessionAttr("student", student)
                        .param("sectionId", DEFAULT_SECTION_ID).param("userAction", ENLIST.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
