package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import jakarta.transaction.Transactional;
import org.hibernate.*;
import org.junit.jupiter.api.*;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.*;

import static com.orangeandbronze.enlistment.controllers.UserAction.CANCEL;
import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class EnlistControllerTest {

    @Test
    void enlistOrCancel_enlist_student_in_section() {
        // Given the EnlistController w/ a student in session

        // and param sectionId to enlist and UserAction "ENLIST"
        EnlistController controller = new EnlistController();
        StudentRepository studentRepository = mock(StudentRepository.class);
        SectionRepository sectionRepository = mock(SectionRepository.class);

        String sectionId = DEFAULT_SECTION_ID;
        Student student = mock(Student.class);
        Section section = newDefaultSection();
        when(sectionRepository.findById(DEFAULT_SECTION_ID)).thenReturn(Optional.of(section));
        controller.setSectionRepo(sectionRepository);
        controller.setStudentRepo(studentRepository);
        EntityManager entityManager = mock(EntityManager.class);
        Session session = mock(Session.class);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        controller.setEntityManager(entityManager);
        // When enlistOrCancel is called
        String returnVal =  controller.enlistOrCancel(sectionId, UserAction.ENLIST, student);
        // Then
        assertAll(
            // fetch the section object from the SectionRepository
            () -> verify(sectionRepository).findById(sectionId),
            () -> verify(session).update(student),
            // student will enlist in section
            () -> verify(student).enlist(section),
            // StudentRepository should save student
            () -> verify(studentRepository).save(student),
            // SectionRepository should save section
            // refresh (redirect) the
            ()-> assertEquals("redirect:enlist", returnVal)
        );



    }

}
